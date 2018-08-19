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

class TextValue extends Value
{
    TextValue(String value)
    {
        super( ValueType.textType());
        mValue = value;
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if ( ! (p instanceof TextValue))
            return false;

        return toString().equals( p.toString());
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return mValue;
    }

    @Override
    public boolean isNull()
    {
        return (mValue == null) || (mValue.length() == 0);
    }

    private String mValue;
}
