package net.whais.Client;

public class ProcedureParameters
{
    protected ProcedureParameters (ValueType[] parameters) throws ConnException
    {
        if ((parameters == null) || (parameters.length == 0))
            throw new ConnException(CmdResult.INVALID_ARGS, "There should be at least one procedure parameter (the return value).");
        
        this.parameters = parameters;
    }
    
    public int count ()
    {
        return parameters.length - 1;
    }

    public final ValueType describeReturnValue ()
    {
        return parameters[0];
    }
    
    public final ValueType describeParameter (int i) throws ConnException
    {
        if ((i < 0) || (i >= parameters.length))
            throw new ConnException(CmdResult.INVALID_ARGS, "No such parameter.");
        
        return parameters[i];
    }
    
    private final ValueType[] parameters;

}
