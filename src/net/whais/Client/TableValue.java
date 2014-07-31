package net.whais.Client;

import java.util.Arrays;
import java.util.Vector;

public class TableValue extends Value
{
    TableValue (FieldValueType[] fields) throws ConnException
    {
        if ((fields == null) || (fields.length == 0))
            throw new ConnException (CmdResult.INVALID_ARGS, "No fields descriptors supplied to create a table.");

        this.fields = fields;
        Arrays.sort (this.fields);
        
        for (int i = 0; i < this.fields.length; ++i)
        {
            if ((i > 0) && this.fields[i].getName().equals (this.fields[i-1].getName ()))
                throw new ConnException (CmdResult.INVALID_ARGS, "A table cannot fields with the same name.");
        }
    }

    
    @Override
    public String toString ()
    {
        if (isNull ())
            return "";

        return values.toString ();
    }


    @Override
    public boolean isNull ()
    {
        return (fields == null) || (values == null) || (values.size () == 0);
    }


    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.create ((short) ValueType.TABLE_MASK);
    }
    
    public Value get (String fieldName, int row) throws ConnException
    {
        if (isNull ())
            throw new ConnException (CmdResult.INVALID_ARGS, "Cannot retrieve a value from a null table.");
        
        int field = 0;
        for (field = 0; field < fields.length; ++field)
        {
            if (fields[field].getName().equals (fieldName))
                break;
        }
        
        if (field >= fields.length)
            throw new ConnException(CmdResult.INVALID_FIELD);
        
        if (values.size () >= row)
            throw new ConnException (CmdResult.INVALID_ROW);
        
        assert field < values.get (row).size ();
        
        return values.get (row).get (field);
    }
    
    public void put (Value value, String fieldName, int row) throws ConnException
    {
        if (row > this.getRowsCount ())
            throw new ConnException(CmdResult.INVALID_ROW);
        else if (row == this.getRowsCount ())
            this.addRows (1);
        
        int field = 0;
        for (field = 0; field < fields.length; ++field)
        {
            if (fields[field].getName().equals (fieldName))
                break;
        }
        
        if (field >= fields.length)
            throw new ConnException(CmdResult.INVALID_FIELD);
        
        if ((value != null) && ! value.type ().equals (fields[field].type ()))
            throw new ConnException (CmdResult.INVALID_ARGS, "The value type is different than of the field type");
        
        values.get (row).set (field, value);
    }
    
    public final void addRows (int count)
    {
        if (values == null)
            values = new Vector<Vector<Value>>();

        for (int i = 0; i < count; ++i)
            values.add (new Vector<Value>(fields.length));
    }
    
    public final int getRowsCount ()
    {
        if (isNull ())
            return 0;
        
        return values.size ();
    }
    
    public FieldValueType[] getFields ()
    {
        assert fields.length > 0;
        
        return fields;
    }
    
    private final FieldValueType[]     fields;
    private Vector<Vector<Value>>      values;      
}
