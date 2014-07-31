package net.whais.Client;

import java.util.Arrays;

public class ValueType
{
    private ValueType (int type, FieldValueType[] fields) throws ConnException
    {
        if ((isArray (type) || (! isTable (type) && ! isField (type)))
            && (fields != null))
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "An array or a basic type does not need fields descriptors");
        }
        
        this.type   = (short)type;
        this.fields = (fields == null) ? null : Arrays.copyOf (fields, fields.length);
        
        if (this.fields == null)
            return ;
        
        Arrays.sort (this.fields); //Normalize the sort.
        
        for (int i = 1; i < this.fields.length; ++i)
        {
            if (this.fields[i].compareTo(this.fields[i - 1]) == 0)
                throw new ConnException (CmdResult.INVALID_ARGS, "All fields must have unique names.");
        }
    }
    
    private ValueType (int type) throws ConnException
    {
        this (type, null);
    }
    
    public static final ValueType create (int type, FieldValueType[] fields) throws ConnException
    {
        if (isArray (type))
        {
            switch (getBaseType (type))
            {
            case BOOL:
                return arrayBoolType();
                
            case CHAR:
                return arrayCharType ();
                
            case DATE:
                return arrayDateType ();
                
            case DATETIME:
                return arrayDatetimeType ();
                
            case HIRESTIME:
                return arrayHirestimeType ();
                
            case INT8:
                return arrayInt8Type ();
                
            case INT16:
                return arrayInt16Type ();
                
            case INT32:
                return arrayInt32Type ();
                
            case INT64:
                return arrayInt64Type ();
                
            case UINT8:
                return arrayUInt8Type ();
                
            case UINT16:
                return arrayUInt16Type ();
                
            case UINT32:
                return arrayUInt32Type ();
                
            case UINT64:
                return arrayUInt64Type ();

            case REAL:
                return arrayRealType ();
                
            case RICHREAL:
                return arrayRichrealType ();
                
            default:
                if (sArrayType == null)
                    sArrayType = new ValueType(ARRAY_MASK | TYPE_NOTSET);
                
                return sArrayType;
            }
        }
        else if (isBasic(type))
        {
            switch (type)
            {
            case BOOL:
                return boolType();
                
            case CHAR:
                return charType ();
                
            case DATE:
                return dateType ();
                
            case DATETIME:
                return datetimeType ();
                
            case HIRESTIME:
                return hirestimeType ();
                
            case INT8:
                return int8Type ();
                
            case INT16:
                return int16Type ();
                
            case INT32:
                return int32Type ();
                
            case INT64:
                return int64Type ();
                
            case UINT8:
                return uint8Type ();
                
            case UINT16:
                return uint16Type ();
                
            case UINT32:
                return uint32Type ();
                
            case UINT64:
                return uint64Type ();

            case REAL:
                return realType ();
                
            case RICHREAL:
                return richrealType ();
                
            case TEXT:
                return textType ();
                
            default:
                assert false;
            }
        }
        else if (isTable(type) && ((fields == null) || (fields.length == 0)))
        {
            if (sTableType == null)
                sTableType = new ValueType (TABLE_MASK);
            
            return sTableType;
        }
        
        return new ValueType (type, fields);
    }

    public static final ValueType create (int type) throws ConnException
    {
        return create (type, null);
    }
    
    public final short getType ()
    {
        return (short) this.type;
    }
    
    public final FieldValueType[] getFields ()
    {
        return this.fields;
    }
    
    public final boolean isBasic ()
    {
        return isBasic (this.type);
    }
    
    public static boolean isBasic (final int type)
    {
        return ! (isArray(type) || isTable(type) || isField (type));
    }
    
    public final boolean isArray ()
    {
        return isArray (this.type);
    }
    
    public static boolean isArray (final int type)
    {
        return (type & ARRAY_MASK) != 0;
    }
    
    public final boolean isField ()
    {
        return isField (this.type);
    }
    
    public static boolean isField (final int type)
    {
        return (type & FIELD_MASK) != 0;
    }

    public final boolean isTable ()
    {
        return isTable (this.type);
    }
    
    public static boolean isTable (final int type)
    {
        return (type & TABLE_MASK) != 0;
    }
    
    public String typeAsString () throws ConnException
    {
        if (isBasic())
            return basicTypeAsString (this.type);
        
        else if (isArray ())
            return arrayTypeAsString (this.type);
        
        else if (isField (this.type))
            return fieldTypeAsString (this.type);
        
        assert isTable ();
        
        if ((this.fields == null) || (this.fields.length == 0))
            return "TABLE";
            
        String result = "TABLE OF (";
        boolean firstField = true;
        for (FieldValueType f : this.fields)
        {
            if (! firstField)
                result += ", ";
            
            firstField = false;
            result += f.getName () + ' ' + (isArray (f.getType ()) ? 
                                                arrayTypeAsString (f.getType ()) :
                                                basicTypeAsString (f.getType ()));    
        }
        result += ')';
        
        return result;
    }
    
    private static String basicTypeAsString (final int type) throws ConnException
    {
        switch (type)
        {
        case BOOL:
            return "BOOL";
            
        case CHAR:
            return "CHARACTER";
            
        case DATE:
            return "DATE";
                    
        case DATETIME:
            return "DATETIME";
            
        case HIRESTIME:
            return "HIRESTIME";
            
        case INT8:
            return "INT8";

        case INT16:
            return "INT16";

        case INT32:
            return "INT32";

        case INT64:
            return "INT64";
            
        case REAL:
            return "REAL";

        case RICHREAL:
            return "RICHREAL";

        case UINT8:
            return "UINT8";

        case UINT16:
            return "UINT16";

        case UINT32:
            return "UINT32";

        case UINT64:
            return "UINT64";
            
        case TEXT:
            return "TEXT";
        
        case TYPE_NOTSET:
            return "UNDEFINED";
            
        default:
            throw new ConnException (CmdResult.GENERAL_ERR, "Received an unexpected type value for string conversion");
        }
    }
    
    private static String arrayTypeAsString (int type) throws ConnException
    {
        assert isArray (type);

        type = getBaseType (type);
        
        if (type == TYPE_NOTSET)
            return "ARRAY";
        
        return "ARRAY OF " + basicTypeAsString (type);
    }
    
    private static String fieldTypeAsString (int type) throws ConnException
    {
        assert isField (type);

        if (isArray (type))
        {
            assert (getBaseType (type) != 0);
            
            return "FIELD OF " + arrayTypeAsString (type);
        }

        type = getBaseType (type);
        
        if (type == TYPE_NOTSET)
            return "FIELD";
        
        return "FIELD OF " + basicTypeAsString (type);
    }
    
    public int getBaseType () throws ConnException
    {
        return getBaseType (this.type);
    }
    
    public static int getBaseType (int type) throws ConnException
    {
        if (isTable (type) && ! isField (type))
            throw new ConnException(CmdResult.INVALID_ARGS, "The specified type must not be a table.");
        
        return type & 0xFF;
    }
    
    public static ValueType boolType ()
    {
        try {
            if (sBoolType == null)
                sBoolType = new ValueType (BOOL);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sBoolType;
    }
    
    public static ValueType charType ()
    {
        try {
            if (sCharType == null)
                sCharType = new ValueType (CHAR);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sCharType;
    }
    
    public static ValueType dateType ()
    {
        try {
            if (sDateType == null)
                sDateType = new ValueType (DATE);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sDateType;
    }
    
    public static ValueType datetimeType ()
    {
        try {
            if (sDateTimeType == null)
                sDateTimeType = new ValueType (DATETIME);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sDateTimeType;
    }
    
    public static ValueType hirestimeType ()
    {
        try {
            if (sHiresTimeType == null)
                sHiresTimeType = new ValueType (HIRESTIME);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sHiresTimeType;
    }
    
    public static ValueType int8Type ()
    {
        try {
            if (sInt8Type == null)
                sInt8Type = new ValueType (INT8);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sInt8Type;
    }
    
    public static ValueType int16Type ()
    {
        try {
            if (sInt16Type == null)
                sInt16Type = new ValueType (INT16);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sInt16Type;
    }
    
    public static ValueType int32Type ()
    {
        try {
            if (sInt32Type == null)
                sInt32Type = new ValueType (INT32);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sInt32Type;
    }
    
    public static ValueType int64Type ()
    {
        try {
            if (sInt64Type == null)
                sInt64Type = new ValueType (INT64);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sInt64Type;
    }
    
    public static ValueType uint8Type ()
    {
        try {
            if (sUInt8Type == null)
                sUInt8Type = new ValueType (UINT8);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sUInt8Type;
    }
    
    public static ValueType uint16Type ()
    {
        try {
            if (sUInt16Type == null)
                sUInt16Type = new ValueType (UINT16);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sUInt16Type;
    }
    
    public static ValueType uint32Type ()
    {
        try {
            if (sUInt32Type == null)
                sUInt32Type = new ValueType (UINT32);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sUInt32Type;
    }
    
    public static ValueType uint64Type ()
    {
        try {
            if (sUInt64Type == null)
                sUInt64Type = new ValueType (UINT64);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sUInt64Type;
    }
    
    public static ValueType realType ()
    {
        try {
            if (sRealType == null)
                sRealType = new ValueType (REAL);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sRealType;
    }
    
    public static ValueType richrealType ()
    {
        try {
            if (sRichRealType == null)
                sRichRealType = new ValueType (RICHREAL);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sRichRealType;
    }
    
    public static ValueType textType ()
    {
        try {
            if (sTextType == null)
                sTextType = new ValueType (TEXT);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sTextType;
    }
    
    public static ValueType arrayBoolType ()
    {
        try {
            if (sArrayBoolType == null)
                sArrayBoolType = new ValueType (BOOL | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayBoolType;
    }
    
    public static ValueType arrayCharType ()
    {
        try {
            if (sArrayCharType == null)
                sArrayCharType = new ValueType (CHAR | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayCharType;
    }
    
    public static ValueType arrayDateType ()
    {
        try {
            if (sArrayDateType == null)
                sArrayDateType = new ValueType (DATE | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayDateType;
    }
    
    public static ValueType arrayDatetimeType ()
    {
        try {
            if (sArrayDateTimeType == null)
                sArrayDateTimeType = new ValueType (DATETIME | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayDateTimeType;
    }
    
    
    public static ValueType arrayHirestimeType ()
    {
        try {
            if (sArrayHiresTimeType == null)
                sArrayHiresTimeType = new ValueType (HIRESTIME | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayHiresTimeType;
    }
    
    public static ValueType arrayInt8Type ()
    {
        try {
            if (sArrayInt8Type == null)
                sArrayInt8Type = new ValueType (INT8 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayInt8Type;
    }
    
    public static ValueType arrayInt16Type ()
    {
        try {
            if (sArrayInt16Type == null)
                sArrayInt16Type = new ValueType (INT16 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayInt16Type;
    }
    
    public static ValueType arrayInt32Type ()
    {
        try {
            if (sArrayInt32Type == null)
                sArrayInt32Type = new ValueType (INT32 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayInt32Type;
    }
    
    public static ValueType arrayInt64Type ()
    {
        try {
            if (sArrayInt64Type == null)
                sArrayInt64Type = new ValueType (INT64 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayInt64Type;
    }
    
    public static ValueType arrayUInt8Type ()
    {
        try {
            if (sArrayUInt8Type == null)
                sArrayUInt8Type = new ValueType (UINT8 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayUInt8Type;
    }
    
    public static ValueType arrayUInt16Type ()
    {
        try {
            if (sArrayUInt16Type == null)
                sArrayUInt16Type = new ValueType (UINT16 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayUInt16Type;
    }
    
    public static ValueType arrayUInt32Type ()
    {
        try {
            if (sArrayUInt32Type == null)
                sArrayUInt32Type = new ValueType (UINT32 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayUInt32Type;
    }
    
    public static ValueType arrayUInt64Type ()
    {
        try {
            if (sArrayUInt64Type == null)
                sArrayUInt64Type = new ValueType (UINT64 | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayUInt64Type;
    }
    
    public static ValueType arrayRealType ()
    {
        try {
            if (sArrayRealType == null)
                sArrayRealType = new ValueType (REAL | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayRealType;
    }
    
    public static ValueType arrayRichrealType ()
    {
        try {
            if (sArrayRichRealType == null)
                sArrayRichRealType = new ValueType (RICHREAL | ARRAY_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sArrayRichRealType;
    }
    
    @Override
    public final boolean equals (Object p) 
    {
        if (this == p)
            return true;

        if ((p == null)
            || ! (p instanceof ValueType))
        {
            return false;
        }
        
        ValueType o = (ValueType) p;

        if (this.type != o.type)
            return false;

        if (! isTable ())
            return true;
        
        if (this.fields == null)
        {
            if (o.fields == null)
                return true;
            
            return false;
        }
        
        if (o.fields == null)
            return false;
        
        if (o.fields.length != this.fields.length)
            return false;
        
        for (int i = 0; i < 0; ++i)
        {
            if ( ! this.fields[i].getName ().equals(o.fields[i].getName ()))
                return false;
        }
        
        return true;
    }
    
    /* Constants describing the type of values or table fields. */
    public static final int BOOL            = 0x0001;
    public static final int CHAR            = 0x0002;
    public static final int DATE            = 0x0003;
    public static final int DATETIME        = 0x0004;
    public static final int HIRESTIME       = 0x0005;
    public static final int INT8            = 0x0006;
    public static final int INT16           = 0x0007;
    public static final int INT32           = 0x0008;
    public static final int INT64           = 0x0009;
    public static final int UINT8           = 0x000A;
    public static final int UINT16          = 0x000B;
    public static final int UINT32          = 0x000C;
    public static final int UINT64          = 0x000D;
    public static final int REAL            = 0x000E;
    public static final int RICHREAL        = 0x000F;
    public static final int TEXT            = 0x0010;
    public static final int TYPE_NOTSET     = 0x0011;

    /* Type modifiers to identify Whais' composite types. */
    public static final int ARRAY_MASK      = 0x0100;
    public static final int FIELD_MASK      = 0x0200;
    public static final int TABLE_MASK      = 0x0400;
    
    private final short              type;
    private final FieldValueType[]   fields;
    
    private static ValueType          sBoolType;
    private static ValueType          sCharType;
    private static ValueType          sDateType;
    private static ValueType          sDateTimeType;
    private static ValueType          sHiresTimeType;
    private static ValueType          sInt8Type;
    private static ValueType          sInt16Type;
    private static ValueType          sInt32Type;
    private static ValueType          sInt64Type;
    private static ValueType          sUInt8Type;
    private static ValueType          sUInt16Type;
    private static ValueType          sUInt32Type;
    private static ValueType          sUInt64Type;
    private static ValueType          sRealType;
    private static ValueType          sRichRealType;
    private static ValueType          sTextType;
    
    private static ValueType          sArrayBoolType;
    private static ValueType          sArrayCharType;
    private static ValueType          sArrayDateType;
    private static ValueType          sArrayDateTimeType;
    private static ValueType          sArrayHiresTimeType;
    private static ValueType          sArrayInt8Type;
    private static ValueType          sArrayInt16Type;
    private static ValueType          sArrayInt32Type;
    private static ValueType          sArrayInt64Type;
    private static ValueType          sArrayUInt8Type;
    private static ValueType          sArrayUInt16Type;
    private static ValueType          sArrayUInt32Type;
    private static ValueType          sArrayUInt64Type;
    private static ValueType          sArrayRealType;
    private static ValueType          sArrayRichRealType;
    
    private static ValueType          sArrayType;
    private static ValueType          sTableType;
    
}
