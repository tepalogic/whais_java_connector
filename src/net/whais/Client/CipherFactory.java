package net.whais.Client;

import java.nio.ByteBuffer;

interface Cipher
{
    abstract byte type ();
    abstract int prepareAuthResponse (ByteBuffer frame, String database, byte[] key);
    abstract int metadataSize ();
}


final class CipherFactory
{
    static PlainCipher plainCipher ()
    {
        
        if (cipher == null) {
            cipher = new PlainCipher ();
        }
        
        return cipher;
    }
    
    static PlainCipher cipher;
}
