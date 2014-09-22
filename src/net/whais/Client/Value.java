package net.whais.Client;

import java.nio.charset.StandardCharsets;

public abstract class Value
{
    Value (ValueType type)
    {
        assert type != null;

        this.type = type;
    }

    @Override
    public abstract String toString ();

    public abstract boolean isNull ();

    @Override
    public abstract boolean equals (Object p);

    public final boolean isArray ()
    {
        return this instanceof ArrayValue;
    }

    public final boolean isTable ()
    {
        return this instanceof TableValue;
    }

    public final ValueType type () throws ConnException
    {
        return this.type;
    }

    public static Value createBasic (ValueType type, String s) throws ConnException
    {
        if (! type.isBasic ())
            throw new ConnException (CmdResult.INVALID_ARGS, "This function may create only basic type values.");

        switch (type.getBaseType ())
        {
        case ValueType.BOOL:
            return new BoolValue (s);

        case ValueType.CHAR:
            return new CharValue (s);

        case ValueType.DATE:
        case ValueType.DATETIME:
        case ValueType.HIRESTIME:
            return new TimeValue (type, s);

        case ValueType.INT8:
        case ValueType.INT16:
        case ValueType.INT32:
        case ValueType.INT64:
        case ValueType.UINT8:
        case ValueType.UINT16:
        case ValueType.UINT32:
        case ValueType.UINT64:
            return new IntegerValue (type, s);

        case ValueType.REAL:
        case ValueType.RICHREAL:
            return new RealValue (type, s);

        case ValueType.TEXT:
            return new TextValue (s);
        }

        throw new ConnException(CmdResult.INVALID_ARGS, "Unknown type to create a value!");
    }

    public static Value createBasic (ValueType type) throws ConnException
    {
        return createBasic (type, null);
    }

    public static ArrayValue createArray (ValueType type, String ... s) throws ConnException
    {
        if (! (type.isArray() || type.isBasic()))
        {
            throw new ConnException (CmdResult.INVALID_ARGS,
                                     "Invalid type provided to create a type.");
        }
        else if (type.getBaseType () == ValueType.TYPE_NOTSET)
        {
            throw new ConnException (CmdResult.INVALID_ARGS,
                                     "Cannot create an undefined array.");
        }

        ArrayValue result = new ArrayValue (ValueType.create (type.getBaseType() | ValueType.ARRAY_MASK));
        if ((s == null)
            || (s.length == 0)
            || ((s.length == 1) && (s[0].length() == 0)))
        {
            return result;
        }

        ValueType baseType = ValueType.create (type.getBaseType ());
        for (String v : s)
            result.add (Value.createBasic (baseType, v));

        return result;
    }

    public static ArrayValue createArray (ValueType type) throws ConnException
    {
        return createArray (type, "");
    }

    static FieldValue createField (ValueType type) throws ConnException
    {
        return new FieldValue (type);
    }

    public static TableValue createTable (TableFieldType[] fields) throws ConnException
    {
        if ((fields == null) || (fields.length == 0))
        {
            throw new ConnException(CmdResult.INVALID_ARGS,
                                    "Cannot create an undefined table.");
        }

        return new TableValue (fields);
    }

    static Value createBasic (ValueType type,
                              byte[]    src,
                              int       srcOffset) throws ConnException
    {
        if (! type.isBasic ())
        {
            throw new ConnException (
                            CmdResult.INVALID_ARGS,
                            "This function may create only basic type values."
                                    );
        }

        if (src[srcOffset] == 0)
            return Value.createBasic (type);

        long temp;
        int year, month, day, hours, mins, secs, usecs;
        int intSize;
        switch (type.getBaseType ())
        {
        case ValueType.BOOL:
            return new BoolValue (src[srcOffset] != '0');

        case ValueType.CHAR:

            int startOffset = srcOffset;
            while (src[srcOffset] != 0)
                ++srcOffset;

            assert (src[srcOffset] == 0);

            return new CharValue (new String (src,
                                              startOffset,
                                              srcOffset - startOffset,
                                              StandardCharsets.UTF_8));
        case ValueType.DATE:
            year = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == 0;

            return new TimeValue (ValueType.dateType(), year, month, day, 0, 0, 0, 0);

        case ValueType.DATETIME:
            year = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == ' ';
            ++srcOffset;

            hours = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            mins = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            secs = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == 0;

            return new TimeValue (ValueType.datetimeType(), year, month, day, hours, mins, secs, 0);

        case ValueType.HIRESTIME:
            year = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == ' ';
            ++srcOffset;

            hours = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            mins = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            secs = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == '.';
            ++srcOffset;

            usecs = (int) getIntegerJavaWeirdWay (src, srcOffset);
            srcOffset += getIntegerStringLength (src, srcOffset);

            assert src[srcOffset] == 0;

            return new TimeValue (ValueType.hirestimeType(), year, month, day, hours, mins, secs, usecs);

        case ValueType.INT8:
        case ValueType.INT16:
        case ValueType.INT32:
        case ValueType.INT64:
        case ValueType.UINT8:
        case ValueType.UINT16:
        case ValueType.UINT32:
        case ValueType.UINT64:
            intSize = getIntegerStringLength (src, srcOffset);

            assert src[srcOffset + intSize] == 0;

            return new IntegerValue (type, new String (src, srcOffset, intSize, StandardCharsets.UTF_8));

        case ValueType.REAL:
        case ValueType.RICHREAL:
            intSize = getRealStringLength (src, srcOffset);

            assert src[srcOffset + intSize] == 0;

            return new RealValue (type, new String (src, srcOffset, intSize, StandardCharsets.UTF_8));

        case ValueType.TEXT:
            temp = srcOffset;
            while (src[srcOffset] != 0)
                ++srcOffset;

            return new TextValue (new String (src, srcOffset, (int) (srcOffset - temp), StandardCharsets.UTF_8));
        }

        throw new ConnException(CmdResult.INVALID_ARGS,
                                "Unknown type to create a value!");
    }

    private static long getIntegerJavaWeirdWay (byte[] src, int offset)
    {
        long result = 0;
        boolean isNegative = false;

        if (src[offset] == '-')
        {
            ++offset;
            isNegative = true;
        }

        while (('0' <= src[offset]) && (src[offset] <= '9'))
        {
            result *= 10;
            result += src[offset++] - '0';
        }

        if (isNegative)
            result *= -1;

        return result;
    }

    private static int getIntegerStringLength (byte[] src, int offset)
    {
        final int originalOffset = offset;

        if (src[offset] == '-')
            ++offset;

        while (('0' <= src[offset]) && (src[offset] <= '9'))
            ++offset;

        return offset - originalOffset;
    }

    private static int getRealStringLength (byte[] src, int offset)
    {
        final int originalOffset = offset;

        if (src[offset] == '-')
            ++offset;

        while (('0' <= src[offset]) && (src[offset] <= '9'))
            ++offset;

        if (src[offset] == '.')
        {
            ++offset;
            while (('0' <= src[offset]) && (src[offset] <= '9'))
                ++offset;
        }

        return offset - originalOffset;
    }

    private ValueType type;
}
