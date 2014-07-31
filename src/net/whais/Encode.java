package net.whais;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;


final public class Encode
{
    static public short loadBeShort (byte[] buffer, int offset) 
    {
        return ByteBuffer.wrap (buffer, offset, 2)
                .order (ByteOrder.BIG_ENDIAN)
                .getShort ();
    }

    static public void storeBeShort (short value, byte[] buffer, int offset) 
    {
        ByteBuffer.wrap (buffer, offset, 2)
            .order (ByteOrder.BIG_ENDIAN)
            .putShort (value);
    }

    static public int loadBeInt (byte[] buffer, int offset) 
    {
        return ByteBuffer.wrap (buffer, offset, 4)
                .order (ByteOrder.BIG_ENDIAN)
                .getInt ();
    }

    static public void storeBeInt (int value, byte[] buffer, int offset) 
    {
        ByteBuffer.wrap (buffer, offset, 4)
            .order (ByteOrder.BIG_ENDIAN)
            .putInt (value);
    }

    static public long loadBeLong (byte[] buffer, int offset) 
    {
        return ByteBuffer.wrap (buffer, offset, 8)
                .order (ByteOrder.BIG_ENDIAN)
                .getLong ();
    }

    static public void storeBeLong (long value, byte[] buffer, int offset) 
    {
        ByteBuffer.wrap (buffer, offset, 4)
            .order (ByteOrder.BIG_ENDIAN)
            .putLong (value);
    }
    
    static public short loadLeShort (byte[] buffer, int offset) 
    {
        return ByteBuffer.wrap (buffer, offset, 2)
                .order (ByteOrder.LITTLE_ENDIAN)
                .getShort ();
    }

    static public void storeLeShort (short value, byte[] buffer, int offset) 
    {
        ByteBuffer.wrap (buffer, offset, 2)
            .order (ByteOrder.LITTLE_ENDIAN)
            .putShort (value);
    }

    static public int loadLeInt (byte[] buffer, int offset) 
    {
        return ByteBuffer.wrap (buffer, offset, 4)
                .order (ByteOrder.LITTLE_ENDIAN)
                .getInt ();
    }

    static public void storeLeInt (int value, byte[] buffer, int offset) 
    {
        ByteBuffer.wrap (buffer, offset, 4)
            .order (ByteOrder.LITTLE_ENDIAN)
            .putInt (value);
    }

    static public long loadLeLong (byte[] buffer, int offset) 
    {
        return ByteBuffer.wrap (buffer, offset, 8)
                .order (ByteOrder.LITTLE_ENDIAN)
                .getLong ();
    }

    static public void storeLeLong (long value, byte[] buffer, int offset) 
    {
        ByteBuffer.wrap (buffer, offset, 4)
            .order (ByteOrder.LITTLE_ENDIAN)
            .putLong (value);
    }
    
    static public int storeUtf8 (String s, byte[] buffer, int offset)
    {
        final byte[] utf8 = s.getBytes (StandardCharsets.UTF_8); 

        ByteBuffer.wrap (buffer, offset, buffer.length - offset).put (utf8);
        
        buffer[offset + utf8.length] = 0;
        return utf8.length + 1;
    }
    
    static public String loadUtf8 (byte[] buffer, int offset)
    {
        int count = 0;
        for (; count + offset < buffer.length; ++count)
        {
            if (buffer[offset + count] == 0)
                break;
        }
        
        return new String (buffer, offset, count, StandardCharsets.UTF_8);
    }
}
