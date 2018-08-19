/**
 * Copyright 2016-2018 Iulian Popa (popaiulian@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package net.whais.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

class CommunicationFrame
{
    CommunicationFrame(Socket s, byte userId, int maxFrameSize, String database, byte[] key) throws IOException
    {
        if (maxFrameSize < _c.MIN_FRAME_SIZE)
            throw new ConnException( CmdResult.INVALID_ARGS, "The maximum communication frame size value is invalid.");

        mServerCookie = 0;
        mRndGenerator = new Random();
        mServer = s;
        mIStream = s.getInputStream();
        mOStream = s.getOutputStream();
        mRawFrame = ByteBuffer.allocate( _c.MIN_FRAME_SIZE).order( ByteOrder.LITTLE_ENDIAN);
        mExpectedFrameId = 0;
        mPendingCommand = _c.CMD_INVALID;
        mCipher = CipherFactory.plainCipher();

        readRawFrame();
        if (mRawFrame.get( _c.FRAME_TYPE_OFF) != _c.FRAME_TYPE_AUTH_CLNT)
            throw new ConnException( "Did not received the authentication frame.");

        final byte protocolVer = 1;
        if ((mRawFrame.getInt( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_VER_OFF) & protocolVer) == 0)

        {
            throw new ConnException( "The communication protocol version used "
                    + "by server is not supported by this client.");
        }

        int serverFrameSize = mRawFrame.getInt( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_SIZE_OFF);
        serverFrameSize &= 0x0000FFFF;

        if (maxFrameSize < serverFrameSize)
            serverFrameSize = maxFrameSize;

        switch (mRawFrame.get( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_ENC_OFF)) {
        case _c.FRAME_ENCTYPE_PLAIN:
            mCipher = CipherFactory.plainCipher();
            break;

        case _c.FRAME_ENCTYPE_3K:
            mCipher = CipherFactory.threeKingsCipher();
            serverFrameSize -= (serverFrameSize % 8);
            break;

        case _c.FRAME_ENCTYPE_DES:
            mCipher = CipherFactory.desCipher();
            serverFrameSize -= (serverFrameSize % 8);
            break;

        case _c.FRAME_ENCTYPE_3DES:
            mCipher = CipherFactory.desedeCipher();
            serverFrameSize -= (serverFrameSize % 8);
            break;

        default:
            throw new ConnException( "The communication cipher used by the "
                                         + "server is not supported by this "
                                         + "client.");
        }
        mKey = mCipher.prepareKey (key);

        final byte[] challengeRsp = CipherFactory.desCipher().encode (key,
                                                                      mRawFrame.array(),
                                                                      _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_CHALLENGE_OFF,
                                                                      8);
        assert challengeRsp.length == 8;

        if (serverFrameSize != mRawFrame.capacity())
            mRawFrame = ByteBuffer.allocate( serverFrameSize).order( ByteOrder.LITTLE_ENDIAN);

        // Prepare the answer the authentication frame response
        mRawFrame.put( _c.FRAME_ENCTYPE_OFF, _c.FRAME_ENCTYPE_PLAIN)
                 .put( _c.FRAME_TYPE_OFF, _c.FRAME_TYPE_AUTH_CLNT_RSP)
                 .putInt( _c.FRAME_ID_OFF, 0)
                 .putInt( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_VER_OFF, protocolVer)
                 .put( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_USR_OFF, userId)
                 .put( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_ENC_OFF, mCipher.type())
                 .putShort( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_SIZE_OFF, (short) serverFrameSize);

        mRawFrame.position(_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_CHALLENGE_OFF);
        for (int i = 0; i < challengeRsp.length; ++i)
            mRawFrame.put(challengeRsp[i]);

        mRawFrame.position (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_FIXED_SIZE);
        mRawFrame.put (database.getBytes (StandardCharsets.UTF_8))
                 .put ((byte) 0);

        mRawFrame.putShort( _c.FRAME_SIZE_OFF, (short) mRawFrameSize);

        mOStream.write( mRawFrame.array(), 0, mRawFrameSize);

        // Make sure we have a clean status
        discardCommandBuffer();
    }

    final void Close()
    {
        try {
            mServer.close();
        } catch (IOException e) {
            // Do noting here! Just ignore it.
        }
    }

    final int maxCmdSize()
    {
        return mRawFrame.capacity() - mCipher.metadataSize();
    }

    final int availableCmdSize()
    {
        assert mRawFrameSize >= mCipher.metadataSize();

        return maxCmdSize() - getLastPosition();
    }

    final ByteBuffer getCmdBuffer()
    {
        assert mRawFrameSize >= mCipher.metadataSize();

        mRawFrame.position( mCipher.metadataSize());

        return mRawFrame;
    }

    final int getLastPosition()
    {
        assert mRawFrameSize >= mCipher.metadataSize();

        return mRawFrameSize;
    }

    final void markBufferPositionValid()
    {
        mRawFrameSize = mRawFrame.position();

        assert mRawFrameSize <= mRawFrame.capacity();
        assert mRawFrameSize >= mCipher.metadataSize();
    }

    final void sendCommand( short cmd, boolean waitForAnswer) throws IOException
    {
        mLastReceivedRsp = _c.CMD_INVALID_RSP;

        final int headerOffset = mCipher.metadataSize() - _c.PLAIN_HDR_SIZE;

        mClientCookie = mRndGenerator.nextInt();

        mRawFrame.putInt( headerOffset + _c.PLAIN_CLNT_COOKIE_OFF, mClientCookie)
                 .putInt( headerOffset + _c.PLAIN_SERV_COOKIE_OFF, mServerCookie)
                 .putShort( headerOffset + _c.PLAIN_TYPE_OFF, cmd)
                 .putShort( headerOffset + _c.PLAIN_CRC_OFF, computeCheckSum());

        writeRawFrame( _c.FRAME_TYPE_NORMAL);

        if (waitForAnswer)
            waithForAnswer( (short) (cmd + 1));
    }

    final void sendCommand( short cmd) throws IOException
    {
        sendCommand( cmd, true);
    }

    final void flushPendingCommand() throws IOException
    {
        if (!hasPendingCommands())
            return;

        sendCommand( mPendingCommand);

        final ByteBuffer b = getCmdBuffer();
        final int cmdRsp = b.getInt();
        if (cmdRsp != CmdResult.OK)
            throw new ConnException( cmdRsp);

        mPendingCommand = _c.CMD_INVALID;
    }

    final void discardCommandBuffer()
    {
        mRawFrame.position( mCipher.metadataSize());

        mRawFrameSize = mRawFrame.position();
        mPendingCommand = _c.CMD_INVALID;
        mLastReceivedRsp = _c.CMD_INVALID_RSP;
    }

    final boolean hasPendingCommands()
    {
        assert (mPendingCommand & 1) == 0;
        assert (mLastReceivedRsp & 1) == 1;

        return mPendingCommand != _c.CMD_INVALID;
    }

    final int getPendingCommand()
    {
        assert (mPendingCommand & 1) == 0;
        assert (mLastReceivedRsp & 1) == 1;

        return mPendingCommand;
    }

    final void setPendingCommand( int command)
    {
        assert (mPendingCommand & 1) == 0;
        assert (mLastReceivedRsp & 1) == 1;

        assert command != _c.CMD_INVALID;
        assert command != _c.CMD_INVALID_RSP;

        mPendingCommand = (short) command;
    }

    final boolean hasCachedReponse()
    {
        assert (mPendingCommand & 1) == 0;
        assert (mLastReceivedRsp & 1) == 1;

        return mLastReceivedRsp != _c.CMD_INVALID_RSP;
    }

    final int getCachedResponse()
    {
        assert (mPendingCommand & 1) == 0;
        assert (mLastReceivedRsp & 1) == 1;

        return mLastReceivedRsp;
    }

    final private void waithForAnswer( short response) throws IOException
    {
        assert mLastReceivedRsp == _c.CMD_INVALID_RSP;

        final int headerOffset = mCipher.metadataSize() - _c.PLAIN_HDR_SIZE;
        readRawFrame();

        if (mRawFrame.getShort( headerOffset + _c.PLAIN_CRC_OFF) != computeCheckSum())
            throw new ConnException( "Received a frame with an invalid check sum.");
        else if (mRawFrame.getInt( headerOffset + _c.PLAIN_CLNT_COOKIE_OFF) != mClientCookie)
            throw new ConnException( "Received a frame with a different client cookie.");

        mServerCookie = mRawFrame.getInt( headerOffset + _c.PLAIN_SERV_COOKIE_OFF);
        if (response != mRawFrame.getShort( headerOffset + _c.PLAIN_TYPE_OFF))
            throw new ConnException( "Received an unexpected command response.");

        mLastReceivedRsp = response;
    }

    final private short computeCheckSum()
    {
        assert mRawFrameSize >= mCipher.metadataSize();

        int result = 0;

        for (int i = mCipher.metadataSize(); i < mRawFrameSize; ++i) {
            short b = mRawFrame.get( i);
            b &= 0x00FF; // Sign correction

            result += b;
            result &= 0x0000FFFFF;
        }
        return (short) result;
    }

    final private void readRawFrame() throws IOException
    {
        int bytesRead = 0;
        while (bytesRead < _c.FRAME_HDR_SIZE) {
            final int count = mIStream.read( mRawFrame.array(), bytesRead, _c.FRAME_HDR_SIZE - bytesRead);

            if (count < 0)
                throw new ConnException( CmdResult.DROPPED);

            bytesRead += count;
        }

        final int frameId = mRawFrame.getInt( _c.FRAME_ID_OFF);
        if (mExpectedFrameId != frameId)
            throw new ConnException( "Received a communication frame with an invalid frame.");

        switch (mRawFrame.get( _c.FRAME_TYPE_OFF)) {
        case _c.FRAME_TYPE_NORMAL:
        case _c.FRAME_TYPE_AUTH_CLNT: {
            int expected = mRawFrame.getInt( _c.FRAME_SIZE_OFF);
            expected &= 0x0000FFFF; // For sign correction.

            if (expected <= _c.FRAME_HDR_SIZE)
                throw new ConnException( "Received a frame with an invalid size.");

            while (bytesRead < expected) {
                final int count = mIStream.read( mRawFrame.array(), bytesRead, expected - bytesRead);

                if (count < 0)
                    throw new ConnException( CmdResult.DROPPED);

                bytesRead += count;
            }
            mRawFrameSize = expected;

            if (mRawFrame.get( _c.FRAME_TYPE_OFF) == _c.FRAME_TYPE_NORMAL)
                mCipher.decodeFrame( this, mKey);

            return;
        }

        case _c.FRAME_TYPE_SERV_BUSY:
            throw new ConnException( "Server says is too busy.");

        case _c.FRAME_TYPE_TIMEOUT:
            throw new ConnException( "Connection has timeout.");

        case _c.FRAME_TYPE_COMM_NOSYNC:
            throw new ConnException( "Communication with the server is out of synchronization.");

        default:
            throw new ConnException( "Received a frame with an unexpected type.");
        }

    }

    final private void writeRawFrame( byte frameType) throws IOException
    {
        assert mRawFrameSize >= mCipher.metadataSize();

        mRawFrame.put (_c.FRAME_TYPE_OFF, frameType);
        mRawFrame.putShort( _c.FRAME_SIZE_OFF, (short) mRawFrameSize);
        mRawFrame.put( _c.FRAME_ENCTYPE_OFF, mCipher.type());
        mRawFrame.putInt( _c.FRAME_ID_OFF, mExpectedFrameId++);

        mCipher.encodeFrame( this, mKey);

        mOStream.write( mRawFrame.array(), 0, mRawFrameSize);
        mOStream.flush();
    }

    private final Random mRndGenerator;
    private final InputStream mIStream;
    private final OutputStream mOStream;
    private final Socket mServer;
    private Cipher mCipher;
    private ByteBuffer mRawFrame;
    private int mRawFrameSize;
    private int mExpectedFrameId;
    private int mClientCookie;
    private int mServerCookie;
    private short mLastReceivedRsp;
    private short mPendingCommand;
    private Object mKey;
}
