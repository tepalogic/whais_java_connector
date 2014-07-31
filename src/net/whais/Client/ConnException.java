package net.whais.Client;

import java.io.IOException;



public class ConnException extends IOException
{
    private static final long serialVersionUID = 1L;

    ConnException (String message)
    {
        super(message);
        code = CmdResult.GENERAL_ERR;
    }

    ConnException (Throwable cause)
    {
        super("Unexpected state encountered during command processing.", cause);
        code = CmdResult.GENERAL_ERR;
    }

    ConnException (String message, Throwable cause)
    {
        super (message, cause);
        code = CmdResult.GENERAL_ERR;
    }
    
    ConnException (int exceptCode, String s)
    {
       super (s);
       code = exceptCode; 

       assert s != null;
    }
    
    ConnException (int exceptCode)
    {
        this (exceptCode, CmdResult.translateResultCode (exceptCode));
    }
    
    final public int getCode ()
    {
        return code;
    }
    
    final private int code;
}
