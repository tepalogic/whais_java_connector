/**
 * Copyright 2016-2018 Iulian Popa (popaiulian@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package net.whais.Client;

import java.nio.charset.StandardCharsets;

/**
 * Describes a WHAIS value. An instance of this object will be used to set send
 * provide procedures parameters or to retrieve the returned result after
 * executing such a procedure.
 *
 * @version 1.0
 */
public abstract class Value
{
    Value(ValueType type)
    {
        assert type != null;

        this.mType = type;
    }

    /**
     * Retrieves the default string representation of a WHAIS value.
     */
    @Override
    public abstract String toString();

    /**
     * Check if is a null WHAIS value.
     *
     * @since 1.0
     */
    public abstract boolean isNull();

    /**
     * Check if two WHAIS value objects are equal.
     * <p>
     * Two WHAIS values are considered equal if they the same type of
     * WHAIS value and they have the same content.
     */
    @Override
    public abstract boolean equals( Object p);

    /**
     * Check if it the value holds an array.
     *
     * @return
     *            Returns {@code true} if the values holds an WHAIS array
     *            value. If this is the case then its safe to case the
     *            object to a {@link ArrayValue} for better manipulation.
     * @since 1.0
     */
    public final boolean isArray()
    {
        return this instanceof ArrayValue;
    }

    /**
     * Check if it the value holds a table.
     *
     * @return
     *            Returns {@code true} if the values holds an WHAIS table
     *            value. If this is the case then its safe to case the
     *            object to a {@link TableValue} for better manipulation.
     * @since 1.0
     */
    public final boolean isTable()
    {
        return this instanceof TableValue;
    }

    /**
     * Check if it the value holds a field.
     * <p>
     * There is no method to create a {@link FieldValue} directly by the user,
     * but they are created by the WHAIS client framework to handle WHAIS
     * procedures returned field results.</p>
     *
     * @return
     *            Returns {@code true} if the values holds an WHAIS field
     *            value. If this is the case then its safe to case the
     *            object to a {@link FieldValue} for better manipulation.
     * @since 1.0
     */
    public final boolean isField()
    {
        return this instanceof FieldValue;
    }

    /**
     * Retrieve the type of the WHAIS value.
     *
     * @return
     *            An object describing the value's type.
     *
     * @throws ConnException
     *
     * @since 1.0
     */
    public final ValueType type() throws ConnException
    {
        return this.mType;
    }

    /**
     *  Factory method to create a WHAIS basic value.
     * <p>
     * A WHAIS basic value is a value of a boolean type is a value that is not
     * an array, a field nor a table value.</p>
     * <p>
     * Based on the requested value type, the supplied string has to obey a
     * format in order to ensure the proper creation of the value as follows:
     * <dl>
     *  <dt>{@link ValueType#BOOL}</dt>
     *  <dd> Requires {@code "1"}, {@code "true"} or {@code "on"} to create
     *       a WHAIS boolean <em>true</em> value, and {@code "0"},
     *       {@code "false"} or {@code "off"} for a <em>false</em> one.</dd>
     * </dl>
     * <dt>{@link ValueType#CHAR}</dt>
     * <dd>The WHAIS character value will be provided from by the supplied
     *     string. The respective string should only have one Unicode code
     *     point.</dd>
     * <dt>{@link ValueType#DATE}</dt>
     * <dd>Requires a string formated like <em>"YYYY/MM/DD"</em> (e.g. {@code
     *     "2001/11/24"}, {@code "1/1/1"} or {@code "-3000/12/1"}).</dd>
     * <dt>{@link ValueType#DATETIME}</dt>
     * <dd>Requires a string formated like <em>"YYYY/MM/DD hh:mm:ss"</em>
     *     (e.g. {@code "1989/12/23 13:59:1"}, {@code "-19/1/1 0:0:1"}).</dd>
     *
     * <dt>{@link ValueType#HIRESTIME}</dt>
     * <dd>Requires a string formated like <em>"YYYY/MM/DD hh:mm:ss.uuuuuu"
     *     </em> (e.g. {@code "1/1/1 0:0:123456"}). Users should supply the
     *     microseconds part left padded with <em>0s</em>. Other wise in case
     *     there are fewer digits the value will be right padded by default
     *     with <em>0s</em>(e.g. {@code "1/1/1 0/0/0.123"} gets translated to
     *     {@code "1/1/1 0/0/0.123000"}).</dd>
     * <dt>{@link ValueType#INT8}, {@link ValueType#INT16},
     *     {@link ValueType#INT32}, {@link ValueType#INT64}</dt>
     * <dd>These string should represents an integer value.<dd>
     * <dt>{@link ValueType#UINT8}, {@link ValueType#UINT16},
     *     {@link ValueType#UINT32}, {@link ValueType#UINT64}</dt>
     * <dd>These string should represents an positive integer value.<dd>
     * <dt>{@link ValueType#REAL}, {@link ValueType#RICHREAL}</dt>
     * <dd>These string should represents a rational number value.</dd>
     * <dt>{@link ValueType#REAL}, {@link ValueType#RICHREAL}</dt>
     * <dd>The string will supply the WHAIS text value.</dd>
     *
     * </dl></p>
     * <p>
     * <em>Note:</em>If for some reason the type could not hold the specified
     * field value (e.g. by using {@code "1024"} for a
     * {@link ValueType#UINT8}, or a real number with more fractional digits
     * that the server supports for the respective type), this framework will
     * not signal the error, leaving the server to manage the situation
     * (by either truncating the value, or signaling the error).</p>
     *
     * @param type
     *            The type of the value to instantiate.
     * @param s
     *            The string representation of the value. To create a null
     *            value this parameter should be either {@code null} or an
     *            empty string.
     * @return
     *            Returns an object of the specified if after the supplied
     *            string parsing was successful.
     *
     * @throws ConnException
     *
     * @since  1.0
     */
    public static Value createBasic( ValueType type, String s) throws ConnException
    {
        if (!type.isBasic())
            throw new ConnException( CmdResult.INVALID_ARGS, "This function may create only basic type values.");

        switch (type.getBaseType()) {
        case ValueType.BOOL:
            return new BoolValue( s);

        case ValueType.CHAR:
            return new CharValue( s);

        case ValueType.DATE:
        case ValueType.DATETIME:
        case ValueType.HIRESTIME:
            return new TimeValue( type, s);

        case ValueType.INT8:
        case ValueType.INT16:
        case ValueType.INT32:
        case ValueType.INT64:
        case ValueType.UINT8:
        case ValueType.UINT16:
        case ValueType.UINT32:
        case ValueType.UINT64:
            return new IntegerValue( type, s);

        case ValueType.REAL:
        case ValueType.RICHREAL:
            return new RealValue( type, s);

        case ValueType.TEXT:
            return new TextValue( s);
        }

        throw new ConnException( CmdResult.INVALID_ARGS, "Unknown type to create a value!");
    }

    /**
     * Wrapper to create a null value of the supplied type.
     *
     * @see #createBasic(ValueType, String)
     * @since 1.0
     */
    public static Value createBasic( ValueType type) throws ConnException
    {
        return createBasic( type, null);
    }

    /**
     * Wrapper to create a Whais boolean value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createBool (String s) throws ConnException
    {
        return createBasic (ValueType.boolType (), s);
    }

    /**
     * Wrapper to create a Whais boolean value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createBool (boolean s) throws ConnException
    {
        return createBasic (ValueType.boolType (), s ? "1" : "0");
    }

    /**
     * Wrapper to create a Whais NULL boolean value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createBool () throws ConnException
    {
        return createBasic (ValueType.boolType (), null);
    }

    /**
     * Wrapper to create a Whais char value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createChar (String s) throws ConnException
    {
        return createBasic (ValueType.charType (), s);
    }

    /**
     * Wrapper to create a Whais char value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createChar (char c) throws ConnException
    {
        return createBasic (ValueType.charType (), "" + c);
    }

    /**
     * Wrapper to create a Whais NULL char value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createChar () throws ConnException
    {
        return createBasic (ValueType.charType (), null);
    }



    /**
     * Wrapper to create a Whais date value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createDate (String s) throws ConnException
    {
        return createBasic (ValueType.dateType (), s);
    }

    /**
     * Wrapper to create a Whais NULL date value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createDate () throws ConnException
    {
        return createBasic (ValueType.dateType (), null);
    }


    /**
     * Wrapper to create a Whais date and time value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createDateTime (String s) throws ConnException
    {
        return createBasic (ValueType.datetimeType (), s);
    }

    /**
     * Wrapper to create a Whais NULL date and time value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createDateTime () throws ConnException
    {
        return createBasic (ValueType.datetimeType (), null);
    }

    /**
     * Wrapper to create a Whais high resolution value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createHiresTime (String s) throws ConnException
    {
        return createBasic (ValueType.hirestimeType (), s);
    }

    /**
     * Wrapper to create a Whais NULL high resolution value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createHiresTime () throws ConnException
    {
        return createBasic (ValueType.hirestimeType (), null);
    }

    /**
     * Wrapper to create a Whais 8 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt8 (String s) throws ConnException
    {
        if ((s != null) && ! s.isEmpty())
        {
            final Long l = Long.parseLong( s);
            if ((l < -128) || (l > 127))
                throw new ConnException( CmdResult.VALUE_OUT_OF_RANGE, "Cannot created a INT8 with to hold " + l);
        }
        return createBasic (ValueType.int8Type (), s);
    }

    /**
     * Wrapper to create a Whais 8 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt8 (int i) throws ConnException
    {
        return createInt8((long)i);
    }

    /**
     * Wrapper to create a Whais 8 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt8 (long l) throws ConnException
    {
        return createBasic (ValueType.int8Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 8 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt8 () throws ConnException
    {
        return createBasic (ValueType.int8Type (), null);
    }


    /**
     * Wrapper to create a Whais 16 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt16 (String s) throws ConnException
    {
        if ((s != null) && ! s.isEmpty())
        {
            final Long l = Long.parseLong( s);
            if ((l < -32768) || (l > 32767))
                throw new ConnException( CmdResult.VALUE_OUT_OF_RANGE, "Cannot created a INT16 with to hold " + l);
        }
        return createBasic (ValueType.int16Type (), s);
    }

    /**
     * Wrapper to create a Whais 16 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt16 (int i) throws ConnException
    {
        return createInt16((long)i);
    }


    /**
     * Wrapper to create a Whais 16 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt16 (long l) throws ConnException
    {
        return createBasic (ValueType.int16Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 16 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt16 () throws ConnException
    {
        return createBasic (ValueType.int16Type (), null);
    }

    /**
     * Wrapper to create a Whais 32 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt32 (String s) throws ConnException
    {
        if ((s != null) && ! s.isEmpty())
        {
            final Long l = Long.parseLong( s);
            if ((l < Integer.MIN_VALUE) || (l > Integer.MAX_VALUE))
                throw new ConnException( CmdResult.VALUE_OUT_OF_RANGE, "Cannot created a INT32 with to hold " + l);
        }

        return createBasic (ValueType.int32Type (), s);
    }

    /**
     * Wrapper to create a Whais 32 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt32 (int i) throws ConnException
    {
        return createInt32((long)i);
    }

    /**
     * Wrapper to create a Whais 32 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt32 (long l) throws ConnException
    {
        return createBasic (ValueType.int32Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 32 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt32 () throws ConnException
    {
        return createBasic (ValueType.int32Type (), null);
    }

    /**
     * Wrapper to create a Whais 64 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt64 (String s) throws ConnException
    {
        return createBasic (ValueType.int64Type (), s);
    }

    /**
     * Wrapper to create a Whais 64 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt64 (int i) throws ConnException
    {
        return createInt64((long)i);
    }

    /**
     * Wrapper to create a Whais 64 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt64 (long l) throws ConnException
    {
        return createBasic (ValueType.int64Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 64 bit integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createInt64 () throws ConnException
    {
        return createBasic (ValueType.int64Type (), null);
    }


    /**
     * Wrapper to create a Whais 8 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt8 (String s) throws ConnException
    {
        if ((s != null) && ! s.isEmpty())
        {
            final Long l = Long.parseLong( s);
            if ((l < 0) || (l > 255))
                throw new ConnException( CmdResult.VALUE_OUT_OF_RANGE, "Cannot created a UINT8 with to hold " + l);
        }
        return createBasic (ValueType.uint8Type (), s);
    }

    /**
     * Wrapper to create a Whais 8 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt8 (int i) throws ConnException
    {
        return createUInt8((long)i);
    }

    /**
     * Wrapper to create a Whais 8 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt8 (long l) throws ConnException
    {
        return createBasic (ValueType.uint8Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 8 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt8 () throws ConnException
    {
        return createBasic (ValueType.uint8Type (), null);
    }

    /**
     * Wrapper to create a Whais 16 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt16 (String s) throws ConnException
    {
        if ((s != null) && ! s.isEmpty())
        {
            final Long l = Long.parseLong( s);
            if ((l < 0) || (l > 65535))
                throw new ConnException( CmdResult.VALUE_OUT_OF_RANGE, "Cannot created a UINT16 with to hold " + l);
        }
        return createBasic (ValueType.uint16Type (), s);
    }

    /**
     * Wrapper to create a Whais 16 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt16 (int i) throws ConnException
    {
        return createUInt16((long)i);
    }

    /**
     * Wrapper to create a Whais 16 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt16 (long l) throws ConnException
    {
        return createBasic (ValueType.uint16Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 16 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt16 () throws ConnException
    {
        return createBasic (ValueType.uint16Type (), null);
    }


    /**
     * Wrapper to create a Whais 32 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt32 (String s) throws ConnException
    {
        if ((s != null) && ! s.isEmpty())
        {
            final Long l = Long.parseLong( s);
            if ((l < 0) || (l > 4294967295l ))
                throw new ConnException( CmdResult.VALUE_OUT_OF_RANGE, "Cannot created a UINT32 with to hold " + l);
        }
        return createBasic (ValueType.uint32Type (), s);
    }

    /**
     * Wrapper to create a Whais 32 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt32 (int i) throws ConnException
    {
        return createUInt32((long)i);
    }

    /**
     * Wrapper to create a Whais 32 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt32 (long l) throws ConnException
    {
        return createBasic (ValueType.uint32Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 32 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt32 () throws ConnException
    {
        return createBasic (ValueType.uint32Type (), null);
    }

    /**
     * Wrapper to create a Whais 64 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt64 (String s) throws ConnException
    {
        return createBasic (ValueType.uint64Type (), s);
    }

    /**
     * Wrapper to create a Whais 64 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt64 (int i) throws ConnException
    {
        return createUInt64((long)i);
    }

    /**
     * Wrapper to create a Whais 64 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt64 (long l) throws ConnException
    {
        return createBasic (ValueType.uint64Type (), Long.toString(l));
    }

    /**
     * Wrapper to create a Whais NULL 64 bit unsigned integer value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createUInt64 () throws ConnException
    {
        return createBasic (ValueType.uint64Type (), null);
    }


    /**
     * Wrapper to create a Whais real value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createReal (String s) throws ConnException
    {
        return createBasic (ValueType.realType (), s);
    }

    /**
     * Wrapper to create a Whais real value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createReal (Double d) throws ConnException
    {
        return createBasic (ValueType.realType (), d == null ? null : d.toString());
    }

    /**
     * Wrapper to create a Whais NULL real value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createReal () throws ConnException
    {
        return createBasic (ValueType.realType (), null);
    }


    /**
     * Wrapper to create a Whais rich real value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createRichReal (String s) throws ConnException
    {
        return createBasic (ValueType.richrealType (), s);
    }

    /**
     * Wrapper to create a Whais rich real value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createRichReal (Double d) throws ConnException
    {
        return createBasic (ValueType.richrealType (), d == null ?  null : d.toString());
    }

    /**
     * Wrapper to create a Whais NULL rich real value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createRichReal () throws ConnException
    {
        return createBasic (ValueType.richrealType (), null);
    }


    /**
     * Wrapper to create a Whais text value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createText (String s) throws ConnException
    {
        return createBasic (ValueType.textType (), s);
    }

    /**
     * Wrapper to create a Whais NULL text value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     *
     * @since  1.0
     */
    public static Value createText () throws ConnException
    {
        return createBasic (ValueType.textType (), null);
    }




    /**
     * Create an array of WHAIS values.
     *
     * @param type
     *            The type of the array to create.
     * @param s
     *            An arrays of strings to supply the value. See
     *            {@link #createBasic(ValueType, String)} for a description
     *            of the required value formats. If this is not provided or
     *            has no elements then this will be a null WHAIS array value
     *            of the specified type.
     * @return
     *            A value holding the WHAIS array.
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     * @see #createTable(TableFieldType[])
     *
     * @since 1.0
     */
    public static ArrayValue createArray( ValueType type, String... s) throws ConnException
    {
        if (!(type.isArray() || type.isBasic())) {
            throw new ConnException( CmdResult.INVALID_ARGS, "Invalid type provided to create a type.");
        }

        ArrayValue result = new ArrayValue( ValueType.create( type.getBaseType() | ValueType.ARRAY_MASK));
        if ((s == null) || (s.length == 0) || ((s.length == 1) && (s[0].length() == 0))) {
            return result;
        }

        ValueType baseType = ValueType.create( type.getBaseType());
        for (String v : s)
            result.add( Value.createBasic( baseType, v));

        return result;
    }

    /**
     * Wrapper to create a null WHAIS array of the specified type.
     */
    public static ArrayValue createArray( ValueType type) throws ConnException
    {
        return createArray( type, "");
    }

    static FieldValue createField( ValueType type) throws ConnException
    {
        return new FieldValue( type);
    }

    /**
     * Create an empty table WHAIS value.
     *
     * @param fields
     *            An array holding the table's fields description. See
     *            {@link TableValue#TableValue(TableFieldType[])} for more
     *            information about the fields.
     * @return
     *            The WHAIS table value.
     *
     * @throws ConnException
     *
     * @see #createBasic(ValueType, String)
     * @see #createArray(ValueType, String...)
     * @since 1.0
     */
    public static TableValue createTable( TableFieldType[] fields) throws ConnException
    {
        return new TableValue( fields);
    }

    protected static Value createBasic( ValueType type, byte[] src, int srcOffset) throws ConnException
    {
        if (!type.isBasic()) {
            throw new ConnException( CmdResult.INVALID_ARGS, "This function may create only basic type values.");
        }

        if (src[srcOffset] == 0)
            return Value.createBasic( type);

        long temp;
        int year, month, day, hours, mins, secs, usecs;
        int intSize;
        switch (type.getBaseType()) {
        case ValueType.BOOL:
            return new BoolValue( src[srcOffset] != '0');

        case ValueType.CHAR:

            int startOffset = srcOffset;
            while (src[srcOffset] != 0)
                ++srcOffset;

            assert (src[srcOffset] == 0);

            return new CharValue( new String( src, startOffset, srcOffset - startOffset, StandardCharsets.UTF_8));
        case ValueType.DATE:
            year = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == 0;

            return new TimeValue( ValueType.dateType(), year, month, day, 0, 0, 0, 0);

        case ValueType.DATETIME:
            year = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == ' ';
            ++srcOffset;

            hours = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            mins = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            secs = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == 0;

            return new TimeValue( ValueType.datetimeType(), year, month, day, hours, mins, secs, 0);

        case ValueType.HIRESTIME:
            year = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            month = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '/';
            ++srcOffset;

            day = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == ' ';
            ++srcOffset;

            hours = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            mins = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == ':';
            ++srcOffset;

            secs = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == '.';
            ++srcOffset;

            usecs = (int) getIntegerJavaWeirdWay( src, srcOffset);
            srcOffset += getIntegerStringLength( src, srcOffset);

            assert src[srcOffset] == 0;

            return new TimeValue( ValueType.hirestimeType(), year, month, day, hours, mins, secs, usecs);

        case ValueType.INT8:
        case ValueType.INT16:
        case ValueType.INT32:
        case ValueType.INT64:
        case ValueType.UINT8:
        case ValueType.UINT16:
        case ValueType.UINT32:
        case ValueType.UINT64:
            intSize = getIntegerStringLength( src, srcOffset);

            assert src[srcOffset + intSize] == 0;

            return new IntegerValue( type, new String( src, srcOffset, intSize, StandardCharsets.UTF_8));

        case ValueType.REAL:
        case ValueType.RICHREAL:
            intSize = getRealStringLength( src, srcOffset);

            assert src[srcOffset + intSize] == 0;

            return new RealValue( type, new String( src, srcOffset, intSize, StandardCharsets.UTF_8));

        case ValueType.TEXT:
            temp = srcOffset;
            while (src[srcOffset] != 0)
                ++srcOffset;

            return new TextValue( new String( src, srcOffset, (int) (srcOffset - temp), StandardCharsets.UTF_8));
        }

        throw new ConnException( CmdResult.INVALID_ARGS, "Unknown type to create a value!");
    }

    private static long getIntegerJavaWeirdWay( byte[] src, int offset)
    {
        long result = 0;
        boolean isNegative = false;

        if (src[offset] == '-') {
            ++offset;
            isNegative = true;
        }

        while (('0' <= src[offset]) && (src[offset] <= '9')) {
            result *= 10;
            result += src[offset++] - '0';
        }

        if (isNegative)
            result *= -1;

        return result;
    }

    private static int getIntegerStringLength( byte[] src, int offset)
    {
        final int originalOffset = offset;

        if (src[offset] == '-')
            ++offset;

        while (('0' <= src[offset]) && (src[offset] <= '9'))
            ++offset;

        return offset - originalOffset;
    }

    private static int getRealStringLength( byte[] src, int offset)
    {
        final int originalOffset = offset;

        if (src[offset] == '-')
            ++offset;

        while (('0' <= src[offset]) && (src[offset] <= '9'))
            ++offset;

        if (src[offset] == '.') {
            ++offset;
            while (('0' <= src[offset]) && (src[offset] <= '9'))
                ++offset;
        }

        return offset - originalOffset;
    }

    private ValueType mType;
}
