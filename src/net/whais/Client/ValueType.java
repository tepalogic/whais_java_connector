package net.whais.Client;

import java.util.Arrays;

/**
 * Used to represent the type a Whais data value.
 *
 * This class will be used as the return value whenever a user needs to know
 * the type of a defined global variables, procedure parameters, the result of
 * a user request, etc.
 *
 * @version 1.0
 *
 * @see Connection
 * @see TableFieldType
 */
public class ValueType
{
    private ValueType (int type, TableFieldType[] fields) throws ConnException
    {
        if ((isArray (type) || (! isTable (type) && ! isField (type)))
            && (fields != null))
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "An array or a basic type does not need fields descriptors");
        }

        this.type   = (short)type;
        this.fields = ((fields == null) || (fields.length == 0)) ? null : Arrays.copyOf (fields, fields.length);

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

    /**
     * Returns a string description of the ValueType instance.
     */
    @Override
    public final String toString ()
    {
       try {
           return this.typeAsString ();
       } catch (ConnException e) {
           e.printStackTrace ();
       }

       return null;
    }

    /**
     * Factory method used to instantiate a Whais table value.
     * A completely defined tables is a table that it has at least one field defined.
     *
     * @param fields The table fields.
     *
     * @return  A ValueType class instantiate tailored to describe the table.
     *
     * @throws ConnException
     *
     * @since  1.0
     * @see    ValueType#create(int)
     * @see    TableFieldType
     */
    public static final ValueType create (TableFieldType[] fields) throws ConnException
    {
        if ((fields == null) || (fields.length == 0))
            return create (TABLE_MASK);

        return new ValueType (TABLE_MASK, fields);
    }

    /**
     * Factory method to instantiate a value type.
     *
     * @param type The raw type of the value.
     * @return A ValueType class instance based on the specified types.
     *
     * @since 1.0
     * @see #BOOL
     * @see #CHAR
     * @see #DATE
     * @see #DATETIME
     * @see #HIRESTIME
     * @see #INT8
     * @see #INT16
     * @see #INT32
     * @see #INT64
     * @see #UINT8
     * @see #UINT16
     * @see #UINT32
     * @see #UINT64
     * @see #REAL
     * @see #RICHREAL
     * @see #TEXT
     * @see #ARRAY_MASK
     * @see #FIELD_MASK
     * @see #TABLE_MASK
     */
    public static final ValueType create (int type) throws ConnException
    {
        if (isField(type))
        {
            if (isArray (type))
            {
                switch (getBaseType (type))
                {
                case BOOL:
                    return fieldArrayBoolType();

                case CHAR:
                    return fieldArrayCharType ();

                case DATE:
                    return fieldArrayDateType ();

                case DATETIME:
                    return fieldArrayDatetimeType ();

                case HIRESTIME:
                    return fieldArrayHirestimeType ();

                case INT8:
                    return fieldArrayInt8Type ();

                case INT16:
                    return fieldArrayInt16Type ();

                case INT32:
                    return fieldArrayInt32Type ();

                case INT64:
                    return fieldArrayInt64Type ();

                case UINT8:
                    return fieldArrayUInt8Type ();

                case UINT16:
                    return fieldArrayUInt16Type ();

                case UINT32:
                    return fieldArrayUInt32Type ();

                case UINT64:
                    return fieldArrayUInt64Type ();

                case REAL:
                    return fieldArrayRealType ();

                case RICHREAL:
                    return fieldArrayRichrealType ();

                default:
                    if (sFieldArrayType == null)
                        sFieldArrayType = new ValueType (TYPE_NOTSET | ARRAY_MASK | FIELD_MASK);

                    return sFieldArrayType;
                }
            }
            else if (isBasic(type))
            {
                switch (type)
                {
                case BOOL:
                    return fieldBoolType ();

                case CHAR:
                    return fieldCharType ();

                case DATE:
                    return fieldDateType ();

                case DATETIME:
                    return fieldDatetimeType ();

                case HIRESTIME:
                    return fieldHirestimeType ();

                case INT8:
                    return fieldInt8Type ();

                case INT16:
                    return fieldInt16Type ();

                case INT32:
                    return fieldInt32Type ();

                case INT64:
                    return fieldInt64Type ();

                case UINT8:
                    return fieldUInt8Type ();

                case UINT16:
                    return fieldUInt16Type ();

                case UINT32:
                    return fieldUInt32Type ();

                case UINT64:
                    return fieldUInt64Type ();

                case REAL:
                    return fieldRealType ();

                case RICHREAL:
                    return fieldRichrealType ();

                case TEXT:
                    return fieldTextType ();

                default:
                    if (sFieldType == null)
                        sFieldType = new ValueType (TYPE_NOTSET | FIELD_MASK);

                    return sFieldType;
                }
            }
        }
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
                    sArrayType = new ValueType (TYPE_NOTSET | ARRAY_MASK);

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
        else if (isTable (type))
        {
            if (sTableType == null)
                sTableType = new ValueType (TABLE_MASK);

            return sTableType;
        }

        throw new ConnException(CmdResult.INVALID_ARGS, "The supplied type does not indetify a valid parameter type.");
    }

    /**
     * Get the numeric value associated with the type described by the class instance.
     *
     * @return The type id.
     *
     * @since 1.0
     * @see #create(int)
     * @see #BOOL
     * @see #CHAR
     * @see #DATE
     * @see #DATETIME
     * @see #HIRESTIME
     * @see #INT8
     * @see #INT16
     * @see #INT32
     * @see #INT64
     * @see #UINT8
     * @see #UINT16
     * @see #UINT32
     * @see #UINT64
     * @see #REAL
     * @see #RICHREAL
     * @see #TEXT
     * @see #ARRAY_MASK
     * @see #FIELD_MASK
     * @see #TABLE_MASK
     */
    public final short getTypeId ()
    {
        return this.type;
    }

    /**
     * Get the fields of a Whais table.
     * @return The fields list. If the ValueType instance does not describe a
     *         table value or properly defined table than returned array will
     *         have a 0 length.
     *
     * @see #create(TableFieldType[])
     * @see TableFieldType
     */
    public final TableFieldType[] getFields ()
    {
        return this.fields;
    }

    public final boolean isBasic ()
    {
        return isBasic (this.type);
    }

    /**
     * Checks if this is not a composite type.
     * A type is considered to be a composite type if is capable to hold multiple values
     * (like arrays, fields, tables but excepting text values).
     *
     * @since 1.0
     * @see ValueType#BOOL
     * @see #CHAR
     * @see #DATE
     * @see #DATETIME
     * @see #HIRESTIME
     * @see #INT8
     * @see #INT16
     * @see #INT32
     * @see #INT64
     * @see #UINT8
     * @see #UINT16
     * @see #UINT32
     * @see #UINT64
     * @see #REAL
     * @see #RICHREAL
     * @see #TEXT
     */
    public static boolean isBasic (final int type)
    {
        return ! (isArray(type) || isTable(type) || isField (type));
    }

    /**
     * Checks if this class instance describes a Whais array value.
     *
     * @since 1.0
     * @see #isArray(int)
     */
    public final boolean isArray ()
    {
        return isArray (this.type);
    }

    /**
     * Verify if the provided field describes it's a Whais array value.
     *
     * @param type The type identifier to check against.
     *
     * @return Returns true is the type describes indeed a Whais array.
     *
     * @since 1.0
     * @see #ARRAY_MASK
     */
    public static boolean isArray (final int type)
    {
        return (type & ARRAY_MASK) != 0;
    }

    /**
     * Checks if this class instance describes a Whais field value.
     *
     * @since 1.0
     * @see #isField(int)
     */
    public final boolean isField ()
    {
        return isField (this.type);
    }

    /**
     * Verify if the provided field describes it's a Whais field value.
     *
     * @param type The type identifier to check against.
     *
     * @return Returns true is the type describes indeed a Whais field.
     *
     * @since 1.0
     *
     * @see #FIELD_MASK
     */
    public static boolean isField (final int type)
    {
        return (type & FIELD_MASK) != 0;
    }

    /**
     * Checks if this class instance describes a Whais table value.
     *
     * @since 1.0
     * @see #isTable(int)
     */
    public final boolean isTable ()
    {
        return isTable (this.type);
    }

    /**
     * Verify if the provided field describes it's a Whais table value.
     *
     * @param type The type identifier to check against.
     *
     * @return Returns true is the type describes indeed a Whais table.
     *
     * @since 1.0
     * @see #TABLE_MASK
     */
    public static boolean isTable (final int type)
    {
        return (type & TABLE_MASK) != 0;
    }

    private String typeAsString () throws ConnException
    {
        if (isField (this.type))
            return fieldTypeAsString (this.type);

        else if (this.isArray ())
            return arrayTypeAsString (this.type);

        else if (this.isBasic())
            return basicTypeAsString (this.type);

        assert this.isTable ();

        if ((this.fields == null) || (this.fields.length == 0))
            return "TABLE";

        String result = "TABLE OF (";
        boolean firstField = true;
        for (TableFieldType f : this.fields)
        {
            if (! firstField)
                result += ", ";

            firstField = false;
            result += f;
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
            return "UNSIGNED INT8";

        case UINT16:
            return "UNSIGNED INT16";

        case UINT32:
            return "UNSIGNED INT32";

        case UINT64:
            return "UNSIGNED INT64";

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

    /**
     * Like its class method equivalent it returns the basic type associated with a composite type.
     * @return The associated base type.
     * @throws ConnException
     *
     * @since 1.0
     * @see #getBaseType(int)
     */
    public int getBaseType () throws ConnException
    {
        return getBaseType (this.type);
    }

    /**
     * Used to retrieve the basic type associated with composites types (array or fields types).
     *
     * @param type  The composite type.
     * @return      The base type associated with the composite type.
     *              For instance it will return {@link #DATE} for an 'ARRAY OF DATE',
     *              or {@link #TYPE_NOTSET} for a 'FIELD' type.
     *
     * @throws ConnException
     *
     * @since 1.0
     */
    public static int getBaseType (int type) throws ConnException
    {
        if (isTable (type) && ! isField (type))
            throw new ConnException (CmdResult.INVALID_ARGS, "The specified type must not be a table.");

        return type & 0xFF;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
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

   /**
    *  Helper method to create a specific ValueType.
    *
    *  @return An instance to an uniquely defined object of this specific type.
    *
    *  @since 1.0
    */
    public static ValueType fieldBoolType ()
    {
        try {
            if (sFieldBoolType == null)
                sFieldBoolType = new ValueType (BOOL | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldBoolType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldCharType ()
    {
        try {
            if (sFieldCharType == null)
                sFieldCharType = new ValueType (CHAR | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldCharType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldDateType ()
    {
        try {
            if (sFieldDateType == null)
                sFieldDateType = new ValueType (DATE | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldDateType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldDatetimeType ()
    {
        try {
            if (sFieldDateTimeType == null)
                sFieldDateTimeType = new ValueType (DATETIME | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldDateTimeType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldHirestimeType ()
    {
        try {
            if (sFieldHiresTimeType == null)
                sFieldHiresTimeType = new ValueType (HIRESTIME | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldHiresTimeType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldInt8Type ()
    {
        try {
            if (sFieldInt8Type == null)
                sFieldInt8Type = new ValueType (INT8 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldInt8Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldInt16Type ()
    {
        try {
            if (sFieldInt16Type == null)
                sFieldInt16Type = new ValueType (INT16 | FIELD_MASK );
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldInt16Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldInt32Type ()
    {
        try {
            if (sFieldInt32Type == null)
                sFieldInt32Type = new ValueType (INT32 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldInt32Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldInt64Type ()
    {
        try {
            if (sFieldInt64Type == null)
                sFieldInt64Type = new ValueType (INT64 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldInt64Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldUInt8Type ()
    {
        try {
            if (sFieldUInt8Type == null)
                sFieldUInt8Type = new ValueType (UINT8 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldUInt8Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldUInt16Type ()
    {
        try {
            if (sFieldUInt16Type == null)
                sFieldUInt16Type = new ValueType (UINT16 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldUInt16Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldUInt32Type ()
    {
        try {
            if (sFieldUInt32Type == null)
                sFieldUInt32Type = new ValueType (UINT32 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldUInt32Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldUInt64Type ()
    {
        try {
            if (sFieldUInt64Type == null)
                sFieldUInt64Type = new ValueType (UINT64 | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldUInt64Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldRealType ()
    {
        try {
            if (sFieldRealType == null)
                sFieldRealType = new ValueType (REAL | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldRealType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldRichrealType ()
    {
        try {
            if (sFieldRichRealType == null)
                sFieldRichRealType = new ValueType (RICHREAL | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldRichRealType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldTextType ()
    {
        try {
            if (sFieldTextType == null)
                sFieldTextType = new ValueType (TEXT | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldTextType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayBoolType ()
    {
        try {
            if (sFieldArrayBoolType == null)
                sFieldArrayBoolType = new ValueType (BOOL | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayBoolType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayCharType ()
    {
        try {
            if (sFieldArrayCharType == null)
                sFieldArrayCharType = new ValueType (CHAR | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayCharType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayDateType ()
    {
        try {
            if (sFieldArrayDateType == null)
                sFieldArrayDateType = new ValueType (DATE | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayDateType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayDatetimeType ()
    {
        try {
            if (sFieldArrayDateTimeType == null)
                sFieldArrayDateTimeType = new ValueType (DATETIME | ARRAY_MASK | FIELD_MASK);

        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayDateTimeType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayHirestimeType ()
    {
        try {
            if (sFieldArrayHiresTimeType == null)
                sFieldArrayHiresTimeType = new ValueType (HIRESTIME | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayHiresTimeType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayInt8Type ()
    {
        try {
            if (sFieldArrayInt8Type == null)
                sFieldArrayInt8Type = new ValueType (INT8 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayInt8Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayInt16Type ()
    {
        try {
            if (sFieldArrayInt16Type == null)
                sFieldArrayInt16Type = new ValueType (INT16 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayInt16Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayInt32Type ()
    {
        try {
            if (sFieldArrayInt32Type == null)
                sFieldArrayInt32Type = new ValueType (INT32 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayInt32Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayInt64Type ()
    {
        try {
            if (sFieldArrayInt64Type == null)
                sFieldArrayInt64Type = new ValueType (INT64 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayInt64Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayUInt8Type ()
    {
        try {
            if (sFieldArrayUInt8Type == null)
                sFieldArrayUInt8Type = new ValueType (UINT8 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayUInt8Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayUInt16Type ()
    {
        try {
            if (sFieldArrayUInt16Type == null)
                sFieldArrayUInt16Type = new ValueType (UINT16 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayUInt16Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayUInt32Type ()
    {
        try {
            if (sFieldArrayUInt32Type == null)
                sFieldArrayUInt32Type = new ValueType (UINT32 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayUInt32Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayUInt64Type ()
    {
        try {
            if (sFieldArrayUInt64Type == null)
                sFieldArrayUInt64Type = new ValueType (UINT64 | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayUInt64Type;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayRealType ()
    {
        try {
            if (sFieldArrayRealType == null)
                sFieldArrayRealType = new ValueType (REAL | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayRealType;
    }

    /**
     *  Helper method to create a specific ValueType.
     *
     *  @return An instance to an uniquely defined object of this specific type.
     *
     *  @since 1.0
     */
    public static ValueType fieldArrayRichrealType ()
    {
        try {
            if (sFieldArrayRichRealType == null)
                sFieldArrayRichRealType = new ValueType (RICHREAL | ARRAY_MASK | FIELD_MASK);
        } catch (ConnException e) {
            // This should not happen
            e.printStackTrace();
        }
        return sFieldArrayRichRealType;
    }


    /**
     * Used to verify that two object fields points to the same field type.
     *
     * @param p     This parameter should point to another ValueType.
     *
     * @return      It will return true only if the other object is a ValueType
     *              and the both types are identical. Two tables are considered
     *              identical types if they have the same number of fields and
     *              the fields' names and types are identical.
     */
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

        if (! this.isTable ())
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

    /** Constant to identify a Whais boolean type value. */
    public static final int BOOL            = 0x0001;

    /** Constant to identify a Whais character type value. */
    public static final int CHAR            = 0x0002;

    /** Constant to identify a Whais date type value. */
    public static final int DATE            = 0x0003;

    /** Constant to identify a Whais date and time type value. */
    public static final int DATETIME        = 0x0004;

    /** Constant to identify a Whais high resolution time type value. */
    public static final int HIRESTIME       = 0x0005;

    /** Constant to identify a Whais 8 bit wide integer value. */
    public static final int INT8            = 0x0006;

    /** Constant to identify a Whais 16 bit wide integer value. */
    public static final int INT16           = 0x0007;

    /** Constant to identify a Whais 32 bit wide integer value. */
    public static final int INT32           = 0x0008;

    /** Constant to identify a Whais 64 bit integer. */
    public static final int INT64           = 0x0009;

    /** Constant to identify a Whais 8 bit wide unsigned integer value. */
    public static final int UINT8           = 0x000A;

    /** Constant to identify a Whais 16 bit wide unsigned integer value. */
    public static final int UINT16          = 0x000B;

    /** Constant to identify a Whais 32 bit wide unsigned integer value. */
    public static final int UINT32          = 0x000C;

    /** Constant to identify a Whais 64 bit wide unsigned integer value. */
    public static final int UINT64          = 0x000D;

    /** Constant to identify a Whais real number value. */
    public static final int REAL            = 0x000E;

    /** Constant to identify a Whais more precise real number value. */
    public static final int RICHREAL        = 0x000F;

    /** Constant to identify a Whais text value. */
    public static final int TEXT            = 0x0010;

    /** Constant to identify a Whais undefined value. */
    public static final int TYPE_NOTSET     = 0x0011;

    /** Mask used to describe Whisper array value. */
    public static final int ARRAY_MASK             = 0x0100;

    /** Mask used to describe Whisper field value. */
    public static final int FIELD_MASK             = 0x0200;

    /** Mask used to describe Whisper table value. */
    public static final int TABLE_MASK             = 0x0400;

    private final short              type;
    private final TableFieldType[]   fields;

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

    private static ValueType          sFieldBoolType;
    private static ValueType          sFieldCharType;
    private static ValueType          sFieldDateType;
    private static ValueType          sFieldDateTimeType;
    private static ValueType          sFieldHiresTimeType;
    private static ValueType          sFieldInt8Type;
    private static ValueType          sFieldInt16Type;
    private static ValueType          sFieldInt32Type;
    private static ValueType          sFieldInt64Type;
    private static ValueType          sFieldUInt8Type;
    private static ValueType          sFieldUInt16Type;
    private static ValueType          sFieldUInt32Type;
    private static ValueType          sFieldUInt64Type;
    private static ValueType          sFieldRealType;
    private static ValueType          sFieldRichRealType;
    private static ValueType          sFieldTextType;

    private static ValueType          sFieldArrayBoolType;
    private static ValueType          sFieldArrayCharType;
    private static ValueType          sFieldArrayDateType;
    private static ValueType          sFieldArrayDateTimeType;
    private static ValueType          sFieldArrayHiresTimeType;
    private static ValueType          sFieldArrayInt8Type;
    private static ValueType          sFieldArrayInt16Type;
    private static ValueType          sFieldArrayInt32Type;
    private static ValueType          sFieldArrayInt64Type;
    private static ValueType          sFieldArrayUInt8Type;
    private static ValueType          sFieldArrayUInt16Type;
    private static ValueType          sFieldArrayUInt32Type;
    private static ValueType          sFieldArrayUInt64Type;
    private static ValueType          sFieldArrayRealType;
    private static ValueType          sFieldArrayRichRealType;
    private static ValueType          sFieldArrayType;

    private static ValueType          sArrayType;
    private static ValueType          sFieldType;
    private static ValueType          sTableType;

}
