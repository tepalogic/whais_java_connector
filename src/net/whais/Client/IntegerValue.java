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

import java.math.BigInteger;

class IntegerValue extends Value
{
    IntegerValue(ValueType type, long value)
    {
        super( type);
        mValue = new BigInteger( "" + value);
    }

    IntegerValue(ValueType type, String v)
    {
        super( type);

        if ((v != null) && (v.length() > 0))
            mValue = new BigInteger( v);
        else
            mValue = null;
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;

        else if (!(p instanceof IntegerValue))
            return false;

        final IntegerValue o = (IntegerValue) p;
        try {
            if (!type().equals( o.type()))
                return false;
        } catch (Throwable e) {
            System.err.println( "Unexpected error!");
            System.exit( -1);
        }

        if (isNull() != o.isNull())
            return false;
        else if (isNull())
            return true;

        return mValue.compareTo( o.mValue) == 0;
    }

    @Override
    public String toString()
    {
        if (mValue != null)
            return mValue.toString();

        return "";
    }

    @Override
    public boolean isNull()
    {
        return mValue == null;
    }

    private final BigInteger mValue;
}
