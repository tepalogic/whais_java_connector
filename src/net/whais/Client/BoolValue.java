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

class BoolValue extends Value
{
    BoolValue(boolean value)
    {
        super( ValueType.boolType());

        mValue = value;
        mIsNull = false;
    }

    BoolValue(String s)
    {
        super( ValueType.boolType());

        if ((s == null) || (s.length() == 0)) {
            mValue = false;
            mIsNull = true;
        } else {
            mIsNull = false;
            mValue = (s.equals( "1") || s.toLowerCase().equals( "T") || s.toLowerCase().equals( "true"));
        }
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if ( ! (p instanceof BoolValue))
            return false;

        final BoolValue o = (BoolValue) p;
        if (isNull() != o.isNull())
            return false;
        else if (isNull())
            return true;

        return mValue == o.mValue;
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return mValue ? "1" : "0";
    }

    @Override
    public boolean isNull()
    {
        return mIsNull;
    }

    private final boolean mValue;
    private final boolean mIsNull;
}
