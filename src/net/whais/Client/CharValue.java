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

class CharValue extends Value
{
    CharValue(String v) throws ConnException
    {
        super( ValueType.charType());

        if ((v == null) || (v.length() == 0)) {
            mValue = 0;
            return;
        }

        if (v.codePointCount( 0, v.length()) > 1) {
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "The string used to construct a character value contains "
                                     + "more than one code points");
        }

        mValue = v.codePointAt( 0);
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if (!(p instanceof CharValue))
            return false;

        return getCodePoint() == ((CharValue) p).getCodePoint();
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return new String( Character.toChars( mValue));
    }

    @Override
    public boolean isNull()
    {
        return mValue == 0;
    }

    public final int getCodePoint()
    {
        return mValue;
    }

    private final int mValue;
}
