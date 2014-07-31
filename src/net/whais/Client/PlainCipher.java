package net.whais.Client;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import net.whais.Client._c;

final class PlainCipher implements Cipher
{
    @Override
    public byte type ()
    {
        return _c.FRAME_ENCTYPE_PLAIN;
    }

    @Override
    public int prepareAuthResponse (ByteBuffer frame, String database, byte[] key)
    {
        frame.position (_c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_FIXED_SIZE);
        frame.put (database.getBytes (StandardCharsets.UTF_8));
        frame.put ((byte) 0);
        
        frame.put(key);
        frame.put ((byte) 0);
        
        return frame.position ();
    }
    
    @Override
    public int metadataSize ()
    {
        return _c.FRAME_HDR_SIZE +_c.PLAIN_HDR_SIZE;
    }
}
