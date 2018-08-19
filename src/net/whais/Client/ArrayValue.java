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

import java.util.Vector;

/**
 * Represents a specialized form of a {@link Value} to ease the manipulation of
 * WHAIS array values.
 *
 * @version 1.0
 */
public class ArrayValue extends Value
{
    ArrayValue(ValueType type, Value[] values) throws ConnException
    {
        super( type);

        if (values == null) {
            mValues = null;
            return;
        }

        mValues = new Vector<>();
        for (int i = 0; i < values.length; ++i) {
            if ((values[i] == null) || values[i].isNull())
                throw new ConnException( CmdResult.INVALID_ARGS, "An array should may not hold a null values.");
            else if ( ! type.equals( values[i].type())) {
                throw new ConnException( CmdResult.INVALID_ARGS,
                                         "Cannot add a value of type " + values[i].type().toString()
                                          + " to an array of type " + type.toString() + '.');
            }

            mValues.add( values[i]);
        }
    }

    ArrayValue(ValueType type) throws ConnException
    {
        this( type, null);
    }

    /**
     * Returns {@code true} if both object holds an array of WHAIS objects
     * each with equals elements and in the same order.
     */
    @Override
    public boolean equals( Object p) {
        if (this == p)
            return true;
        else if ( ! (p instanceof ArrayValue))
            return false;

        final ArrayValue o = (ArrayValue) p;

        try {
            if ( ! type().equals( o.type()))
                return false;
        } catch (Throwable e) {
            return false;
        }

        if (isNull() != o.isNull())
            return false;
        else if (isNull())
            return true;
        else if (mValues.size() != o.mValues.size())
            return false;

        for (int i = 0; i < mValues.size(); ++i) {
            if ( ! get( i).equals( o.get( i)))
                return false;
        }

        return true;
    }

    /**
     * Add a value to the array.
     * <p>
     * Increase the array with a new value. The value will be inserted into
     * the array at its current size (e.g. value returned by {@link #size()})
     * and increase the array size afterward if the addition was
     * successful.</p>
     *
     * @param v
     *            The value to add into the array. It should not be a null value
     *            as WHAIS arrays do not hold null values.
     *
     * @throws ConnException
     */
    public void add( Value v) throws ConnException
    {
        if (v.isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Cannot add a null value to an array.");

        if ( ! v.type().isBasic() || v.type().equals( ValueType.textType()))
            throw new ConnException( CmdResult.INVALID_ARGS, "An array may hold only basic values.");

        if (mValues == null) {

            mValues = new Vector<>();
            mValues.add( v);

            return;
        }

        if ( ! ValueType.create( type().getBaseType()).equals( v.type())) {
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "Cannot add a value of type " + v.type().toString()
                                     + " to an array of type " + this.type().toString() + '.');
        }

        this.mValues.add( v);
    }

    /**
     * Retrieve an array element.
     *
     * @param i
     *            The index of the element in the array.
     * @return
     *            Return a value of the {@code i}th element from the array.
     */
    public Value get( int i)
    {
        if ((this.mValues == null) || (this.mValues.size() <= i))
            throw new ArrayIndexOutOfBoundsException( i);

        return this.mValues.get( i);
    }

    /**
     * Remove an array element.
     *
     * @param i
     *            The index of the element in the array.
     * @return
     *            Return a value of the {@code i}th element removed from
     *            the array.
     */
    public Value remove( int i)
    {
        if ((this.mValues == null) || (this.mValues.size() <= i))
            throw new ArrayIndexOutOfBoundsException( i);

        return this.mValues.remove( i);
    }

    /**
     * Get a Java like array of values.
     *
     * @return
     *            Return a standard Java array with the values hold by the
     *            WHAIS array.
     */
    public Value[] toArray()
    {
        Value[] result = new Value[this.mValues.size()];

        if (this.isNull())
            return null;

        return this.mValues.toArray( result);
    }

    /**
     * Get the array's elements count.
     */
    public int size()
    {
        return (this.mValues == null) ? 0 : this.mValues.size();
    }

    @Override
    public String toString()
    {
        if (this.isNull())
            return "";

        final int count = this.mValues.size();
        assert (count > 0);

        final StringBuilder resultBuilder = new StringBuilder().append( '{');
        for (int r = 0; r < count; ++r) {
            resultBuilder.append( '\'');
            resultBuilder.append( this.mValues.get( r).toString());
            resultBuilder.append( '\'');
            if (r < count - 1)
                resultBuilder.append( ' ');
        }
        resultBuilder.append( '}');

        return resultBuilder.toString();
    }

    /**
     * Check if this is a null WHAIS array value.
     */
    @Override
    public boolean isNull()
    {
        return (this.mValues == null) || (this.mValues.size() == 0);
    }

    private Vector<Value> mValues;
}
