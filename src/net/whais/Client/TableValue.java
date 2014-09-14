package net.whais.Client;

import java.util.Arrays;
import java.util.Vector;

public class TableValue extends Value
{
    TableValue (TableFieldType[] fields) throws ConnException
    {
        super (ValueType.create (ValueType.TABLE_MASK));

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
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof TableValue))
            return false;

        final TableValue o = (TableValue) p;
        try
        {
            if ( ! this.type ().equals (o.type ()))
                return false;
        }
        catch (Throwable e)
        {
            return false;
        }

        if (this.isNull () != o.isNull())
            return false;

        else if (this.isNull ())
            return true;

        if (this.values.size () != o.values.size ())
            return false;

        for (int i = 0; i < this.values.size (); ++i)
        {
            final Vector<Value> thisRow = this.values.get (i);
            final Vector<Value> othRow = o.values.get (i);

            if (thisRow.size () != othRow.size ())
                return false;

            for (int j = 0; j < thisRow.size (); ++j)
            {
                final Value tf = thisRow.get (j);
                final Value of = othRow.get (j);

                if (tf == null)
                {
                    if (of == null)
                        continue;

                    else
                        return false;
                }

                if ( ! tf.equals (of))
                    return false;
            }
        }

        return true;
    }

    @Override
    public String toString ()
    {
        if (this.isNull ())
            return "";

        final int rowsCount = this.values.size ();
        assert (rowsCount > 0);

        final StringBuilder resultBuilder = new StringBuilder ();
        for (int r = 0; r < rowsCount; ++r)
        {
            int f = 0;
            resultBuilder.append ('[');
            for (TableFieldType field : this.fields)
            {
                resultBuilder.append (field.getName ());
                resultBuilder.append (':');
                if (this.values.get (r).get (f) != null)
                    resultBuilder.append (this.values.get (r)
                                                     .get (f).toString ());
                if (f < this.fields.length - 1)
                    resultBuilder.append(", ");
                else
                    resultBuilder.append (']');

               ++f;
            }

            if (r < rowsCount - 1)
                resultBuilder.append (", ");
        }

        return resultBuilder.toString ();
    }

    @Override
    public boolean isNull ()
    {
        return (this.fields == null)
                || (this.values == null)
                || (this.values.size () == 0);
    }

    public Value get (String fieldName, int row) throws ConnException
    {
        if (this.isNull ())
            throw new ConnException (CmdResult.INVALID_ARGS, "Cannot retrieve a value from a null table.");

        int field = 0;
        for (field = 0; field < this.fields.length; ++field)
        {
            if (this.fields[field].getName().equals (fieldName))
                break;
        }

        if (field >= this.fields.length)
            throw new ConnException(CmdResult.INVALID_FIELD);

        if (this.values.size () >= row)
            throw new ConnException (CmdResult.INVALID_ROW);

        assert field < this.values.get (row).size ();

        return this.values.get (row).get (field);
    }

    public void put (Value value, String fieldName, int row) throws ConnException
    {
        if (row > this.getRowsCount ())
            throw new ConnException(CmdResult.INVALID_ROW);
        else if (row == this.getRowsCount ())
            this.addRows (1);

        int field = 0;
        for (field = 0; field < this.fields.length; ++field)
        {
            if (this.fields[field].getName().equals (fieldName))
                break;
        }

        if (field >= this.fields.length)
            throw new ConnException(CmdResult.INVALID_FIELD);

        if ((value != null) && ! value.type ().equals (this.fields[field].getType ()))
            throw new ConnException (CmdResult.INVALID_ARGS, "The value type is different than of the field type");

        this.values.get (row).set (field, value);
    }

    public final void addRows (int count)
    {
        if (this.values == null)
            this.values = new Vector<Vector<Value>>();

        for (int i = 0; i < count; ++i)
            this.values.add (new Vector<Value>(this.fields.length));
    }

    public final int getRowsCount ()
    {
        if (this.isNull ())
            return 0;

        return this.values.size ();
    }

    public TableFieldType[] getFields ()
    {
        assert this.fields.length > 0;

        return this.fields;
    }

    private final TableFieldType[]     fields;
    private Vector<Vector<Value>>      values;
}
