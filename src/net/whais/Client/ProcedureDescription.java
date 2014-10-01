package net.whais.Client;

/**
 * Represents the description of a WHAIS procedure.
 * <p>
 * Object of this type are returned in order to describe the procedures
 * defined in the context of a WHAIS database.</p>
 *
 * @version 1.0
 *
 * @see Connection
 * @see Connection#describeProcedure(String)
 */

public class ProcedureDescription
{
    ProcedureDescription(String name, ValueType[] parameters) throws ConnException
    {
        assert parameters.length > 0;
        assert name != null;

        mName = name;
        mParameters = parameters;
    }

    /**
     * Get the name of the procedure.
     *
     * @since 1.0
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Describe the procedure return type.
     *
     * @return
     *            An object holding the procedure's return value description.
     *
     * @see ValueType
     *
     * @since 1.0
     */
    public final ValueType describeReturnValue()
    {
        return mParameters[0];
    }

    /**
     * Get the count of the procedures parameters.
     * <p>
     * This method its used prior
     * {@link ProcedureDescription#describeParameter(int)} in order to
     * retrieve the number of arguments of a WHAIS procedure.</p>
     *
     * @return
     *            The count of procedure's parameters (the return type is not
     *            included on this count).
     *
     * @since 1.0
     */
    public int getParametersCount()
    {
        return mParameters.length - 1;
    }

    /**
     * Describe a procedure parameter.
     *
     * @param i
     *            The index of the procedure parameter. The index count starts
     *            from {@code 0} (e.g. use {@code 0} for the first, {@code 1}
     *            for the second on and so on).
     * @return
     *            An object holding the paramater's description.
     *
     * @throws ConnException
     *
     * @see #getParametersCount()
     *
     * @since 1.0
     */
    public final ValueType describeParameter( int i) throws ConnException
    {
        if ((i < 0) || (i >= mParameters.length - 1))
            throw new ConnException( CmdResult.INVALID_ARGS, "No such parameter.");

        return mParameters[i + 1];
    }

    private final String mName;
    private final ValueType[] mParameters;
}
