package net.whais.Client;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

final class ThreeKingsCipher implements Cipher
{

    @Override
    public byte type()
    {
        return _c.FRAME_ENCTYPE_3K;
    }

    @Override
    public int prepareAuthResponse( ByteBuffer frame, String database, byte[] key)
    {
        frame.position( _c.FRAME_HDR_SIZE + _c.FRAME_AUTH_RSP_FIXED_SIZE);
        frame.put( database.getBytes( StandardCharsets.UTF_8))
             .put( (byte) 0);

        return frame.position();
    }

    @Override
    public int metadataSize()
    {
        return _c.FRAME_HDR_SIZE + _c.ENC_3K_HDR_SIZE + _c.PLAIN_HDR_SIZE;
    }

    @Override
    public void encodeFrame( CommunicationFrame frame, byte[] key)
    {
        final ByteBuffer buffer = frame.getCmdBuffer();

        int bufferSize = frame.getLastPosition();
        int plainSize = bufferSize;

        buffer.position( bufferSize);
        while (bufferSize % 4 != 0) {
            buffer.put( (byte) (Math.random() * 1024));
            ++bufferSize;
            frame.markBufferPositionValid();
        }

        final int firstKing = ((byte) (Math.random() * 1024) << 24)
                              + ((byte) (Math.random() * 1024) << 16)
                              + ((byte) (Math.random() * 1024) << 8)
                              + ((byte) (Math.random() * 1024));
        buffer.putInt( _c.FRAME_HDR_SIZE + _c.ENC_3K_FIRST_KING_OFF, firstKing);

        final int secondKing = ((byte) (Math.random() * 1024) << 24)
                               + ((byte) (Math.random() * 1024) << 16)
                               + ((byte) (Math.random() * 1024) << 8)
                               + ((byte) (Math.random() * 1024));

        buffer.putInt( _c.FRAME_HDR_SIZE + _c.ENC_3K_SECOND_KING_OFF, secondKing);
        for (int i = 0, prev = 0; i < _c.ENC_3K_PLAIN_SIZE_OFF; ++i) {
            final byte b = buffer.get( _c.FRAME_HDR_SIZE + i);
            buffer.put( _c.FRAME_HDR_SIZE + i, (byte) (b ^ key[prev % key.length]));
            prev = b & 0xFF;
        }

        buffer.putShort( _c.FRAME_HDR_SIZE + _c.ENC_3K_PLAIN_SIZE_OFF, (short) plainSize)
              .putShort( _c.FRAME_HDR_SIZE + _c.ENC_3K_SPARE_OFF, (short) 0xFFFF);

        encodeBuffer( buffer, _c.FRAME_HDR_SIZE + _c.ENC_3K_PLAIN_SIZE_OFF, bufferSize, key, firstKing, secondKing);
        buffer.putShort( _c.FRAME_SIZE_OFF, (short) bufferSize);
    }

    @Override
    public void decodeFrame( CommunicationFrame frame, byte[] key)
    {
        ByteBuffer buffer = frame.getCmdBuffer();

        int bufferSize = frame.getLastPosition();

        for (int i = 0, prev = 0; i < _c.ENC_3K_PLAIN_SIZE_OFF; ++i) {
            byte b = buffer.get( _c.FRAME_HDR_SIZE + i);
            b ^= key[prev % key.length];
            buffer.put( _c.FRAME_HDR_SIZE + i, b);
            prev = b & 0xFF;
        }

        final int firstKing = buffer.getInt( _c.FRAME_HDR_SIZE + _c.ENC_3K_FIRST_KING_OFF);
        final int secondKing = buffer.getInt( _c.FRAME_HDR_SIZE + _c.ENC_3K_SECOND_KING_OFF);
        decodeBuffer( buffer, _c.FRAME_HDR_SIZE + _c.ENC_3K_PLAIN_SIZE_OFF, bufferSize, key, firstKing, secondKing);
        int plainSize = buffer.getShort( _c.FRAME_HDR_SIZE + _c.ENC_3K_PLAIN_SIZE_OFF);
        plainSize &= 0x0000FFFF;

        buffer.position( plainSize);
        frame.markBufferPositionValid();
    }

    static void encodeBuffer( ByteBuffer buffer, int from, int to, byte[] key, int firstKing, int secondKing)
    {
        assert to % 4 == 0;
        int keyIndex = (int) ((firstKing & 0xFFFFFFFFL) % key.length);

        for (int pos = from; pos < to; pos += 4) {
            int message = buffer.getInt( pos);
            message -= firstKing;
            message ^= secondKing;

            int thirdKing = key[keyIndex++] & 0xFF;
            thirdKing <<= 8;
            keyIndex %= key.length;

            thirdKing |= key[keyIndex++] & 0xFF;
            thirdKing <<= 8;
            keyIndex %= key.length;

            thirdKing |= key[keyIndex++] & 0xFF;
            thirdKing <<= 8;
            keyIndex %= key.length;

            thirdKing |= key[keyIndex++] & 0xFF;
            keyIndex %= key.length;

            for (int b = 0; b < 16; ++b) {
                if ((thirdKing & (1 << b)) != 0)
                    message = exchange_1bit_pair( message, 2 * b, 2 * b + 1);
            }

            for (int b = 0; b < 8; ++b) {
                if ((thirdKing & (1 << (16 + b))) != 0)
                    message = exchange_2bit_pair( message, 4 * b, 4 * b + 2);
            }

            for (int b = 0; b < 4; ++b) {
                if ((thirdKing & (1 << (24 + b))) != 0)
                    message = exchange_4bit_pair( message, 8 * b, 8 * b + 4);
            }

            for (int b = 0; b < 2; ++b) {
                if ((thirdKing & (1 << (28 + b))) != 0)
                    message = exchange_8bit_pair( message, 16 * b, 16 * b + 8);
            }

            if ((thirdKing & (1 << 30)) != 0)
                message = exchange_16bit_pair( message, 0, 16);

            if ((thirdKing & (1 << 31)) != 0)
                message = exchange_8bit_pair( message, 8, 16);

            buffer.putInt( pos, message);
        }
    }

    static void decodeBuffer( ByteBuffer buffer, int from, int to, byte[] key, int firstKing, int secondKing)
    {
        assert to % 4 == 0;

        int keyIndex = (int) ((firstKing & 0xFFFFFFFFL) % key.length);

        for (int pos = from; pos < to; pos += 4) {
            int message = buffer.getInt( pos);

            int thirdKing = key[keyIndex++] & 0xFF;
            thirdKing <<= 8;
            keyIndex %= key.length;

            thirdKing |= key[keyIndex++] & 0xFF;
            thirdKing <<= 8;
            keyIndex %= key.length;

            thirdKing |= key[keyIndex++] & 0xFF;
            thirdKing <<= 8;
            keyIndex %= key.length;

            thirdKing |= key[keyIndex++] & 0xFF;
            keyIndex %= key.length;

            if ((thirdKing & (1 << 31)) != 0)
                message = exchange_8bit_pair( message, 8, 16);

            if ((thirdKing & (1 << 30)) != 0)
                message = exchange_16bit_pair( message, 0, 16);

            for (int b = 1; b >= 0; --b) {
                if ((thirdKing & (1 << (28 + b))) != 0)
                    message = exchange_8bit_pair( message, 16 * b, 16 * b + 8);
            }

            for (int b = 3; b >= 0; --b) {
                if ((thirdKing & (1 << (24 + b))) != 0)
                    message = exchange_4bit_pair( message, 8 * b, 8 * b + 4);
            }

            for (int b = 7; b >= 0; --b) {
                if ((thirdKing & (1 << (16 + b))) != 0)
                    message = exchange_2bit_pair( message, 4 * b, 4 * b + 2);
            }

            for (int b = 15; b >= 0; --b) {
                if ((thirdKing & (1 << b)) != 0)
                    message = exchange_1bit_pair( message, 2 * b, 2 * b + 1);
            }

            message ^= secondKing;
            message += firstKing;

            buffer.putInt( pos, message);
        }
    }

    static int exchange_1bit_pair( int value, int p1, int p2)
    {
        int val1 = (value >> p1) & 1;
        int val2 = (value >> p2) & 1;

        assert (p1 < 32);
        assert (p2 < 32);

        value &= ~((1 << p1) | (1 << p2));
        value |= (val1 << p2) | (val2 << p1);

        return value;
    }

    static int exchange_2bit_pair( int value, int p1, int p2)
    {
        int val1 = (value >> p1) & 0x03;
        int val2 = (value >> p2) & 0x03;

        assert ((p1 < 32) && ((p1 % 2) == 0));
        assert ((p2 < 32) && ((p2 % 2) == 0));

        value &= ~((0x03 << p1) | (0x03 << p2));
        value |= (val1 << p2) | (val2 << p1);

        return value;
    }

    static int exchange_4bit_pair( int value, int p1, int p2)
    {
        int val1 = (value >> p1) & 0x0F;
        int val2 = (value >> p2) & 0x0F;

        assert ((p1 < 32) && ((p1 % 4) == 0));
        assert ((p2 < 32) && ((p2 % 4) == 0));

        value &= ~((0x0F << p1) | (0x0F << p2));
        value |= (val1 << p2) | (val2 << p1);

        return value;
    }

    static int exchange_8bit_pair( int value, int p1, int p2)
    {
        int val1 = (value >> p1) & 0xFF;
        int val2 = (value >> p2) & 0xFF;

        assert ((p1 < 32) && ((p1 % 8) == 0));
        assert ((p2 < 32) && ((p2 % 8) == 0));

        value &= ~((0xFF << p1) | (0xFF << p2));
        value |= (val1 << p2) | (val2 << p1);

        return value;
    }

    static int exchange_16bit_pair( int value, int p1, int p2)
    {
        int val1 = (value >> p1) & 0xFFFF;
        int val2 = (value >> p2) & 0xFFFF;

        assert ((p1 < 32) && ((p1 % 16) == 0));
        assert ((p2 < 32) && ((p2 % 16) == 0));

        value &= ~((0xFFFF << p1) | (0xFFFF << p2));
        value |= (val1 << p2) | (val2 << p1);

        return value;
    }
}
