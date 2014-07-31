package net.whais.Client;

import java.nio.charset.StandardCharsets;

public abstract class Value 
{
    @Override
    public abstract String toString ();
    
    public abstract boolean isNull ();
    
    public abstract ValueType type () throws ConnException;
    
    static Value create (byte[] src, int srcOffset, ValueType type) throws ConnException
    {

        if (type.isTable())
            return new TableValue (type.getFields ());
        
        else if (type.isArray ())
            return new ArrayValue ();
        
        else if (type.isField ())
            throw new ConnException (CmdResult.INVALID_ARGS, "Cannot create a field value.");
        
        long temp;
        int codePoint, year, month, day, hours, mins, secs, usecs;
        int intSize;
        switch (type.getBaseType ())
        {
        case ValueType.BOOL:
            return new BoolValue (src[0] != '0');
            
        case ValueType.CHAR:
            codePoint = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += codePoint % 100;
            codePoint /= 100;

            assert src[srcOffset] == 0;
            
            return new CharValue ((int) codePoint);
            
        case ValueType.DATE:
            year = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += year % 100;
            year /= 100;
            
            assert src[srcOffset] == '-';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += month % 100;
            month /= 100;

            assert src[srcOffset] == '-';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += day % 100;
            day /= 100;

            assert src[srcOffset] == 0;
            
            return new DateValue (year, month, day);
            
        case ValueType.DATETIME:
            year = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += year % 100;
            year /= 100;
            
            assert src[srcOffset] == '-';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += month % 100;
            month /= 100;

            assert src[srcOffset] == '-';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += day % 100;
            day /= 100;

            assert src[srcOffset] == ' ';
            ++srcOffset;
            
            hours = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += hours % 100;
            hours /= 100;
            
            assert src[srcOffset] == ':';
            ++srcOffset;

            mins = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += mins % 100;
            mins /= 100;

            assert src[srcOffset] == ':';
            ++srcOffset;

            secs = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += secs % 100;
            secs /= 100;

            assert src[srcOffset] == 0;
            
            return new DateTimeValue (year, month, day, hours, mins, secs);
            
        case ValueType.HIRESTIME:
            year = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += year % 100;
            year /= 100;
            
            assert src[srcOffset] == '-';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += month % 100;
            month /= 100;

            assert src[srcOffset] == '-';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += day % 100;
            day /= 100;

            assert src[srcOffset] == ' ';
            ++srcOffset;
            
            hours = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += hours % 100;
            hours /= 100;
            
            assert src[srcOffset] == ':';
            ++srcOffset;

            mins = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += mins % 100;
            mins /= 100;

            assert src[srcOffset] == ':';
            ++srcOffset;

            secs = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += secs % 100;
            secs /= 100;

            assert src[srcOffset] == '.';
            ++srcOffset;

            usecs = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += usecs % 100;
            usecs /= 100;

            assert src[srcOffset] == 0;
            
            return new HiresTimeValue (year, month, day, hours, mins, secs, usecs);
            
        case ValueType.INT8:
        case ValueType.INT16:
        case ValueType.INT32:
        case ValueType.INT64:
        case ValueType.UINT8:
        case ValueType.UINT16:
        case ValueType.UINT32:
        case ValueType.UINT64:
            temp = getIntegerJavaWeirdWay (src, srcOffset);
            intSize = (int) (temp % 100);
            
            assert src[srcOffset + intSize] == 0;

            return new IntegerValue (new String (src, srcOffset, intSize, StandardCharsets.UTF_8));
            
        case ValueType.REAL:
        case ValueType.RICHREAL:
            temp = getIntegerJavaWeirdWay (src, srcOffset);
            intSize = (int) (temp % 100);
            
            assert src[srcOffset + intSize] == '.';
            
            temp = getIntegerJavaWeirdWay (src, srcOffset + intSize + 1);
            intSize += (int) (temp % 100);

            assert src[srcOffset + intSize] == 0;
            
            return new RealValue (new String (src, srcOffset, intSize, StandardCharsets.UTF_8));
            
        case ValueType.TEXT:
            temp = srcOffset;
            while (src[srcOffset] != 0)
                ++srcOffset;

            return new TextValue (new String (src, srcOffset, (int) (srcOffset - temp), StandardCharsets.UTF_8));
        }
        
        throw new ConnException(CmdResult.INVALID_ARGS, "Unknown type to create a value!");
    }
    
    static Value create (String s, ValueType type) throws ConnException
    {
        if (type.isTable())
            return new TableValue (type.getFields ());
        
        else if (type.isArray ())
        {
            assert (type.getBaseType () == ValueType.TYPE_NOTSET);
            return new ArrayValue ();
        }
        
        else if (type.isField ())
            throw new ConnException (CmdResult.INVALID_ARGS, "Cannot create a field value.");
        
        switch (type.getBaseType ())
        {
        case ValueType.BOOL:
            return new BoolValue (s);
            
        case ValueType.CHAR:
            return new CharValue (s);
            
        case ValueType.DATE:
            return new DateValue (s);
            
        case ValueType.DATETIME:
            return new DateTimeValue (s);
            
        case ValueType.HIRESTIME:
            return new HiresTimeValue (s);
            
        case ValueType.INT8:
        case ValueType.INT16:
        case ValueType.INT32:
        case ValueType.INT64:
        case ValueType.UINT8:
        case ValueType.UINT16:
        case ValueType.UINT32:
        case ValueType.UINT64:
            return new IntegerValue (s);
            
        case ValueType.REAL:
        case ValueType.RICHREAL:
            return new RealValue (s);
            
        case ValueType.TEXT:
            return new TextValue (s);
        }
        
        throw new ConnException(CmdResult.INVALID_ARGS, "Unknown type to create a value!");
    }
    
    static Value create (ValueType type) throws ConnException
    {
        return create (null, type);
    }
    
    private static long getIntegerJavaWeirdWay (byte[] src, int offset)
    {
        final int originalOffset = offset;

        int result = 0;
        boolean negative = false;
        
        if (src[offset] == '-')
        {
            ++offset;
            negative = true;
        }
        
        while (('0' <= src[offset]) && (src[offset] <= '9'))
        {
            result *= 10;
            result += src[offset] - '0';
        }
        
        if (negative)
            result *= -1;
        
        return result * 100 + (offset - originalOffset);
    }
}
