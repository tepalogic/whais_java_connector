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

import java.io.IOException;

/**
 * Used to signal the error condition when dealing with a WHAIS server.
 *
 * @version 1.0
 */
public class ConnException extends IOException
{
    private static final long serialVersionUID = 1L;

    ConnException(String message)
    {
        super( message);
        mCode = CmdResult.GENERAL_ERR;
    }

    ConnException(Throwable cause)
    {
        super( "Unexpected state encountered during command processing.", cause);
        mCode = CmdResult.GENERAL_ERR;
    }

    ConnException(String message, Throwable cause)
    {
        super( message, cause);
        mCode = CmdResult.GENERAL_ERR;
    }

    ConnException(int exceptCode, String s)
    {
        super( s);
        mCode = exceptCode;

        assert s != null;
    }

    ConnException(int exceptCode)
    {
        this( exceptCode, CmdResult.translateResultCode( exceptCode));
    }

    /**
     * Get the associated code with the signaled error cause.
     * @return
     *          The numeric code corresponding to the exceptional situation.\
     * @see CmdResult
     */
    final public int getCode()
    {
        return mCode;
    }

    final private int mCode;
}
