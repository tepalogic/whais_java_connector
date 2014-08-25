package net.whais.Client;

/**
 * Describes the return value and the parameters of a Whais procedure.
 *
 * @version 1.0
 *
 * @see Connection#describeProcedure(String)
 */

public class ProcedureDescription
{
    protected ProcedureDescription (String name, ValueType[] parameters) throws ConnException
    {
        assert parameters.length > 0;
        assert name != null;

        this.name       = name;
        this.parameters = parameters;
    }

    /**
     * Get the name of the described Whais procedure.
     *
     * @return The procedure name.
     *
     * @since 1.0
     */
    public String getName ()
    {
        return this.name;
    }

    /**
     * Describe the return type of a Whais procedure.
     *
     * @return A description of the procedure return type.
     *
     * @since 1.0
     * @see ValueType
     */
    public final ValueType describeReturnValue ()
    {
        return this.parameters[0];
    }

    /**
     * Get the parameters count of the described Whais procedure.
     *
     * @return The number of procedure parameters.
     *
     * @since 1.0
     * @see #describeParameter(int)
     */
    public int getParametersCount ()
    {
        return this.parameters.length - 1;
    }

    /**
     * Describe the paramter's type of a Whais procedure.
     *
     * @param i The index of the procedure (counting from 0).
     * @return  The description of the parameter type.
     *
     * @throws ConnException
     *
     * @since 1.0
     *
     * @see #getParametersCount()
     * @see ValueType
     */
    public final ValueType describeParameter (int i) throws ConnException
    {
        if ((i < 0) || (i >= this.parameters.length - 1))
            throw new ConnException(CmdResult.INVALID_ARGS, "No such parameter.");

        return this.parameters[i + 1];
    }

    private final String      name;
    private final ValueType[] parameters;
}
