package net.whais.Client;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

final class PlainCipher implements Cipher
{
    @Override
    public byte
    type ()
    {
        return _c.FRAME_ENCTYPE_PLAIN;
    }

    @Override
    public int
    prepareAuthResponse (ByteBuffer frame, String database, byte[] key)
    {
        frame.position (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_FIXED_SIZE);
        frame.put (database.getBytes (StandardCharsets.UTF_8))
             .put ((byte) 0)
             .put(key)
             .put ((byte) 0);

        return frame.position ();
    }

    @Override
    public int
    metadataSize ()
    {
        return _c.FRAME_HDR_SIZE +_c.PLAIN_HDR_SIZE;
    }

    @Override
    public void
    encodeFrame (CommunicationFrame frame, byte[] key)
    {
        return ; //No need for us to do anything here.
    }

    @Override
    public void
    decodeFrame (CommunicationFrame frame, byte[] key)
    {
        return ; //No need for us to do anything here.
    }

}
