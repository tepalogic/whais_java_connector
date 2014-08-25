package net.whais.Client;

/**
 * Used to represent a description of Whais table's field.
 *
 * @version 1.0
 *
 * @see Connection
 * @see ValueType
 */
public class TableFieldType implements Comparable<TableFieldType>
{
    /**
     * Instantiate a field description.
     *
     * @param name  Whais table's field name.
     * @param type  Whais table's field type.
     *
     * @throws ConnException
     *
     * @since 1.0
     */
    public TableFieldType (String name, ValueType type) throws ConnException
    {
        if ((name == null) || (name.length ()  == 0))
            throw new ConnException (CmdResult.INVALID_ARGS, "A field type needs to have a name.");

        else if (type == null)
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid field definition.");

        else if ((type.getBaseType () < ValueType.BOOL)
                 || (type.getBaseType () >= ValueType.TYPE_NOTSET))

        {
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid type for a field descriptor.");
        }

        this.fieldName = name;
        this.fieldType = type;

        if (this.fieldType.isField()
            || this.fieldType.isTable ())
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid type used for field definition");
        }
    }

    /**
     * Get a string representation of a Whais table field.
     */
    @Override
    public final String toString ()
    {
        return this.getName () + " AS " + this.fieldType;
    }

    /**
     * Get the name of a Whais table field.
     *
     * @return Field's name. This should have a size bigger than 0.
     *
     * @since 1.0
     */
    public final String getName ()
    {
        return this.fieldName;
    }

    /**
     * Get the type of a Whais table field.
     *
     * @return Field's type.
     *
     * @since 1.0
     * @see ValueType
     */
    public final ValueType getType ()
    {
        assert this.fieldType != null;

        return this.fieldType;
    }

    /*
     * Used internally to normalize the fields of a table.
     *
     * @deprecated Should not be used.
     * @since 1.0
     */
    @Override
    public int compareTo (TableFieldType o)
    {
        return this.fieldName.compareTo (o.fieldName);
    }

    private final String    fieldName;
    private final ValueType fieldType;

}
