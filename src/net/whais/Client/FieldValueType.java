package net.whais.Client;

public class FieldValueType implements Comparable<FieldValueType>
{
    public FieldValueType (String name, ValueType type) throws ConnException
    {
        if ((name == null) || (name.length ()  == 0))
            throw new ConnException (CmdResult.INVALID_ARGS, "A field type needs to have a name.");

        else if (type == null)
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid field definition.");

        else if ((type.getBaseType () < ValueType.BOOL)
                 || (type.getBaseType () > ValueType.TYPE_NOTSET))
                
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
    
    public final String getName ()
    {
        return this.fieldName;
    }
    
    public final short getType ()
    {
        return this.fieldType.getType ();
    }
    
    public final ValueType type ()
    {
        assert this.fieldType != null;
        
        return fieldType;
    }
    
    @Override
    public int compareTo (FieldValueType o)
    {
        return fieldName.compareTo (o.fieldName);
    }
    
    private final String    fieldName;
    private final ValueType fieldType;

}
