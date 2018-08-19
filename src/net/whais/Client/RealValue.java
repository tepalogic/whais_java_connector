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

import java.math.BigDecimal;

class RealValue extends Value
{
    RealValue(ValueType type, String value)
    {
        super( type);

        if ((value == null) || (value.length() == 0))
            mValue = null;

        else
            mValue = new BigDecimal( value);
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof RealValue))
            return false;

        final RealValue o = (RealValue) p;
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

        return mValue.compareTo( o.mValue) == 0;
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return mValue.toString();
    }

    @Override
    public boolean isNull()
    {
        return (mValue == null);
    }

    private final BigDecimal mValue;
}
