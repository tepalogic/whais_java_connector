package net.whais.Client;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;



class CommunicationFrame
{
    CommunicationFrame (Socket s, byte userId, int maxFrameSize, String database, byte[] key) throws IOException
    {
        if (maxFrameSize < _c.MIN_FRAME_SIZE)
            throw new ConnException(CmdResult.INVALID_ARGS, "The maximum communication frame size value is invalid.");

        this.serverCookie    = 0;
        this.randomGenerator = new Random ();
        this.server          = s;
        this.iStream         = s.getInputStream ();
        this.oStream         = s.getOutputStream ();
        this.rawFrame        = ByteBuffer.allocate (_c.MIN_FRAME_SIZE)
                                         .order(ByteOrder.LITTLE_ENDIAN);
        this.expectedFrameId = 0;
        this.pendingCommand  = _c.CMD_INVALID;

        this.readRawFrame ();
        if (this.rawFrame.get (_c.FRAME_TYPE_OFF) != _c.FRAME_TYPE_AUTH_CLNT)
            throw new ConnException("Did not received the authentication frame.");

        final byte cipherType = this.rawFrame.get (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_ENC_OFF);
        switch (cipherType)
        {
            case _c.FRAME_ENCTYPE_PLAIN:
                this.cipher = CipherFactory.plainCipher ();
                break;

            case _c.FRAME_ENCTYPE_3K:
            default:

                throw new ConnException ("The communication cipher used by the " +
                        "server is not supported by this client.");
        }

        final byte protocolVer = 1;
        if ((this.rawFrame.getInt (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_VER_OFF) &
                protocolVer) == 0)

        {
            throw new ConnException("The communication protocol version used " +
                    "by server is not supported by this client.");
        }

        int serverFrameSize = this.rawFrame.getInt (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_SIZE_OFF);
        serverFrameSize &= 0x0000FFFF;

        if (maxFrameSize < serverFrameSize)
            serverFrameSize = maxFrameSize;

        if (serverFrameSize != this.rawFrame.capacity ())
            this.rawFrame = ByteBuffer.allocate (serverFrameSize).order(ByteOrder.LITTLE_ENDIAN);

        //Prepare the answer the authentication frame response
        this.rawFrame.put (_c.FRAME_ENCTYPE_OFF, _c.FRAME_ENCTYPE_PLAIN);
        this.rawFrame.put (_c.FRAME_TYPE_OFF, _c.FRAME_TYPE_AUTH_CLNT_RSP);
        this.rawFrame.putInt (_c.FRAME_ID_OFF, 0);
        this.rawFrame.putInt (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_VER_OFF, protocolVer);
        this.rawFrame.put (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_USR_OFF, userId);
        this.rawFrame.put (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_ENC_OFF, this.cipher.type ());
        this.rawFrame.putShort (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_SIZE_OFF, (short) serverFrameSize);

        this.rawFrameSize = this.cipher.prepareAuthResponse (this.rawFrame, database, key);

        this.rawFrame.putShort(_c.FRAME_SIZE_OFF, (short) this.rawFrameSize);
        this.oStream.write (this.rawFrame.array (), 0, this.rawFrameSize);

        //Make sure we have a clean status
        this.discardCommandBuffer ();
    }

    final void Close ()
    {
        try {
            this.server.close ();
        } catch (IOException e) {
            //Do noting here! Just ignore it.
        }
    }

    final int maxCmdSize ()
    {
        return this.rawFrame.capacity () - this.cipher.metadataSize ();
    }

    final int availableCmdSize ()
    {
        assert this.rawFrameSize >= this.cipher.metadataSize ();

        return this.maxCmdSize () - this.getCmdLastPosition ();
    }

    final ByteBuffer getCmdBuffer ()
    {
        assert this.rawFrameSize >= this.cipher.metadataSize ();

        this.rawFrame.position (this.cipher.metadataSize ());

        return this.rawFrame;
    }

    final int getCmdLastPosition ()
    {
        assert this.rawFrameSize >= this.cipher.metadataSize ();

        return this.rawFrameSize;
    }

    final void markBufferPositionValid ()
    {
        assert this.rawFrameSize <= this.rawFrame.position ();

        this.rawFrameSize = this.rawFrame.position ();

        assert this.rawFrameSize <= this.rawFrame.capacity ();
        assert this.rawFrameSize >= this.cipher.metadataSize ();
    }

    final void sendCommand (short cmd, boolean waitForAnswer) throws IOException
    {
        this.lastReceivedRsp = _c.CMD_INVALID_RSP;

        final int headerOffset = this.cipher.metadataSize () - _c.PLAIN_HDR_SIZE;

        this.clientCookie = this.randomGenerator.nextInt ();

        this.rawFrame.putInt (headerOffset + _c.PLAIN_CLNT_COOKIE_OFF, this.clientCookie);
        this.rawFrame.putInt (headerOffset + _c.PLAIN_SERV_COOKIE_OFF, this.serverCookie);
        this.rawFrame.putShort (headerOffset + _c.PLAIN_TYPE_OFF, cmd);
        this.rawFrame.putShort (headerOffset + _c.PLAIN_CRC_OFF, this.computeCheckSum ());

        this.writeRawFrame (_c.FRAME_TYPE_NORMAL);

        if (waitForAnswer)
            this.waithForAnswer ((short) (cmd + 1));
    }

    final void sendCommand (short cmd) throws IOException
    {
        this.sendCommand (cmd, true);
    }

    final void flushPendingCommand () throws IOException
    {
        if ( ! this.hasPendingCommands ())
            return ;

        this.sendCommand (this.pendingCommand);

        this.pendingCommand = _c.CMD_INVALID;
    }

    final void discardCommandBuffer ()
    {
        this.rawFrame.position (this.cipher.metadataSize ());
        this.rawFrameSize    = this.rawFrame.position ();
        this.pendingCommand  = _c.CMD_INVALID;
        this.lastReceivedRsp = _c.CMD_INVALID_RSP;
    }

    final boolean hasPendingCommands ()
    {
        assert (this.pendingCommand & 1) == 0;
        assert (this.lastReceivedRsp & 1) == 1;

        return this.pendingCommand != _c.CMD_INVALID;
    }

    final int getPendingCommand ()
    {
        assert (this.pendingCommand & 1) == 0;
        assert (this.lastReceivedRsp & 1) == 1;

        return this.pendingCommand;
    }

    final void setPendingCommand (int command)
    {
        assert (this.pendingCommand & 1) == 0;
        assert (this.lastReceivedRsp & 1) == 1;

        assert command != _c.CMD_INVALID;
        assert command != _c.CMD_INVALID_RSP;

        this.pendingCommand = (short) command;
    }

    final boolean hasCachedReponse ()
    {
        assert (this.pendingCommand & 1) == 0;
        assert (this.lastReceivedRsp & 1) == 1;

        return this.lastReceivedRsp != _c.CMD_INVALID_RSP;
    }

    final int getCachedResponse ()
    {
        assert (this.pendingCommand & 1) == 0;
        assert (this.lastReceivedRsp & 1) == 1;

        return this.lastReceivedRsp;
    }

    final private void waithForAnswer (short response) throws IOException
    {
        assert this.lastReceivedRsp == _c.CMD_INVALID_RSP;

        final int headerOffset = this.cipher.metadataSize () - _c.PLAIN_HDR_SIZE;
        this.readRawFrame ();

        if (this.rawFrame.getShort (headerOffset + _c.PLAIN_CRC_OFF) != this.computeCheckSum ())
            throw new ConnException ("Received a frame with an invalid check sum.");

        else if (this.rawFrame.getInt (headerOffset + _c.PLAIN_CLNT_COOKIE_OFF) != this.clientCookie)
            throw new ConnException ("Received a frame with a different client cookie.");

        this.serverCookie = this.rawFrame.getInt (headerOffset + _c.PLAIN_SERV_COOKIE_OFF);
        if (response != this.rawFrame.getShort (headerOffset + _c.PLAIN_TYPE_OFF))
            throw new ConnException ("Received an unexpected command response.");

        this.lastReceivedRsp = response;
    }

    final private short computeCheckSum ()
    {
        assert this.rawFrameSize >= this.cipher.metadataSize ();

        int result = 0;

        for (int i = this.cipher.metadataSize (); i < this.rawFrameSize; ++i )
        {
            short b = this.rawFrame.get (i);
            b &= 0x00FF; //Sign correction

            result += b;
            result &= 0x0000FFFFF;
        }
        return (short) result;
    }

    final private void readRawFrame () throws IOException
    {
        int bytesRead = 0;
        while (bytesRead < _c.FRAME_HDR_SIZE) {
            final int count = this.iStream.read (this.rawFrame.array (),
                    bytesRead,
                    _c.FRAME_HDR_SIZE - bytesRead);

            if (count < 0)
                throw new ConnException (CmdResult.DROPPED);

            bytesRead += count;
        }

        final int frameId = this.rawFrame.getInt (_c.FRAME_ID_OFF);
        if (this.expectedFrameId != frameId)
            throw new ConnException("Received a communication frame with an invalid frame.");

        switch (this.rawFrame.get (_c.FRAME_TYPE_OFF))
        {
            case _c.FRAME_TYPE_NORMAL:
            case _c.FRAME_TYPE_AUTH_CLNT:
            {
                int expected = this.rawFrame.getInt (_c.FRAME_SIZE_OFF);
                expected &= 0x0000FFFF; //For sign correction.

                if (expected <= _c.FRAME_HDR_SIZE)
                    throw new ConnException ("Received a frame with an invalid size.");

                while (bytesRead < expected) {
                    final int count = this.iStream.read (this.rawFrame.array (),
                            bytesRead,
                            expected - bytesRead);

                    if (count < 0)
                        throw new ConnException (CmdResult.DROPPED);

                    bytesRead += count;
                }
                this.rawFrameSize = expected;
                return ;
            }

            case _c.FRAME_TYPE_SERV_BUSY:
                throw new ConnException ("Server says is too busy.");

            case _c.FRAME_TYPE_TIMEOUT:
                throw new ConnException ("Connection has timeout.");

            case _c.FRAME_TYPE_COMM_NOSYNC:
                throw new ConnException ("Communication with the server is out of synchronization.");

            default:
                throw new ConnException ("Received a frame with an unexpected type.");
        }
    }

    final private void writeRawFrame (byte frameType) throws IOException
    {
        assert this.rawFrameSize >= this.cipher.metadataSize ();

        this.rawFrame.putShort (_c.FRAME_SIZE_OFF, (short) this.rawFrameSize);
        this.rawFrame.put (_c.FRAME_ENCTYPE_OFF, this.cipher.type ());
        this.rawFrame.putInt (_c.FRAME_ID_OFF, this.expectedFrameId++);

        this.oStream.write (this.rawFrame.array (), 0, this.rawFrameSize);
    }

    private final Random          randomGenerator;
    private final InputStream     iStream;
    private final OutputStream    oStream;
    private final Socket          server;
    private final Cipher          cipher;
    private ByteBuffer            rawFrame;
    private int                   rawFrameSize;
    private int                   expectedFrameId;
    private int                   clientCookie;
    private int                   serverCookie;
    private short                 lastReceivedRsp;
    private short                 pendingCommand;
}
