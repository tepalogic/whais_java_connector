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
     */
    final public int getCode()
    {
        return mCode;
    }

    final private int mCode;
}
