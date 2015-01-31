package net.whais.Client;


interface Cipher
{
    abstract byte type();

    abstract int metadataSize();

    abstract void encodeFrame( CommunicationFrame frame, Object key);

    abstract void decodeFrame( CommunicationFrame frame, Object key);

    abstract Object prepareKey (byte[] key);
}
