package net.whais.Client;

import java.util.Vector;

public class TableValue extends Value
{
    TableValue (TableFieldType[] fields) throws ConnException
    {
        super (ValueType.create (fields));

        this.values = null;
        if ((fields == null) || (fields.length == 0))
        {
            throw new ConnException (
                            CmdResult.INVALID_ARGS,
                            "No fields descriptors supplied to create a table."
                                    );
        }
    }

    @Override
    public boolean
    equals (Object p)
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

        final int rowsCount = this.getRowsCount ();
        if (rowsCount != o.getRowsCount ())
            return false;

        for (int i = 0; i < rowsCount; ++i)
        {
            final Vector<Value> thisRow = this.values.get (i);
            final Vector<Value> othRow = o.values.get (i);

            if (thisRow == null)
            {
                if (othRow != null)
                    return false;

                continue;
            }
            else if (othRow == null)
                return false;

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
    public String
    toString ()
    {
        if (this.isNull ())
            return "";

        final int rowsCount = this.getRowsCount ();
        assert (rowsCount > 0);

        final StringBuilder resultBuilder = new StringBuilder ('(');
        for (int r = 0; r < rowsCount; ++r)
        {
            int f = 0;
            resultBuilder.append ('[');

            final TableFieldType[] fields = this.getFields ();
            for (TableFieldType field : fields)
            {
                resultBuilder.append (field.getName ());
                Value cell = null;
                try
                {
                    cell = this.get (field.getName (), r);
                }
                catch (Throwable e)
                {
                    assert (false);
                    cell = null;
                }

                if (cell == null)
                    resultBuilder.append ("(internal error)");
                else
                {
                    if (cell.isArray ())
                        resultBuilder.append (cell);
                    else
                    {
                        resultBuilder.append ('\'');
                        resultBuilder.append (cell);
                        resultBuilder.append ('\'');
                    }
                }

                if (f < fields.length - 1)
                    resultBuilder.append(' ');
                else
                    resultBuilder.append (']');

               ++f;
            }

            if (r < rowsCount - 1)
                resultBuilder.append (' ');
        }

        return resultBuilder.append (')').toString ();
    }

    @Override
    public boolean
    isNull ()
    {
        return this.getRowsCount () == 0;
    }

    public Value
    get (String fieldName, int row) throws ConnException
    {
        if (this.isNull ())
        {
            throw new ConnException (
                            CmdResult.INVALID_ARGS,
                            "Cannot retrieve a value from a null table."
                                    );
        }

        final TableFieldType[] fields = this.getFields ();
        int field = 0;
        for (field = 0; field < fields.length; ++field)
        {
            if (fields[field].getName().equals (fieldName))
                break;
        }

        if (field >= fields.length)
            throw new ConnException(CmdResult.INVALID_FIELD);

        if (row >= this.getRowsCount ())
            throw new ConnException (CmdResult.INVALID_ROW);

        assert (this.values.get (row) == null)
               || (field < this.values.get (row).size ());

        Value result = null;

        if (this.values.get (row) != null)
        {
            assert this.values.get (row).size () == fields.length ;

            result = this.values.get (row).get (field);
        }

        if (result == null)
        {
            final ValueType t = fields[field].getType ();
            if (t.isArray ())
                result = Value.createArray (t);

            else
                result = Value.createBasic (t);
        }

        return result;
    }

    public void
    put (Value value, String fieldName, int row) throws ConnException
    {
        if (row > this.getRowsCount ())
            throw new ConnException(CmdResult.INVALID_ROW);

        else if (row == this.getRowsCount ())
            this.addRows (1);

        final TableFieldType[] fields = this.getFields ();
        int field = 0;
        for (field = 0; field < fields.length; ++field)
        {
            if (fields[field].getName().equals (fieldName))
                break;
        }

        if (field >= fields.length)
            throw new ConnException(CmdResult.INVALID_FIELD);

        if ((value != null)
            && ! value.type ().equals (fields[field].getType ()))
        {
            throw new ConnException (
                    CmdResult.INVALID_ARGS,
                    "The value type is different from field type"
                                    );
        }

        if ((value != null) && value.isNull ())
        {
            if (this.values.get (row) != null)
            {
                this.values.get (row).set (field, null);
                for (Value v : this.values.get (row))
                {
                    if ((v != null) && ! v.isNull ())
                        return ;
                }

                this.values.set (row, null);
            }
        }
        else
        {
            Vector<Value> rowValues = this.values.get (row);
            if (rowValues == null)
            {
                rowValues = new Vector<Value> ();
                for (int f = 0; f < fields.length; ++f)
                    rowValues.add (null);

                this.values.set (row, rowValues);
            }

            assert rowValues.size () == fields.length;

            this.values.get (row).set (field, value);
        }
    }

    public final void
    addRows (int count)
    {
        if (count == 0)
            return ;

        if (this.values == null)
            this.values = new Vector<Vector<Value>>();

        for (int i = 0; i < count; ++i)
            this.values.add (null);
    }


    public final int
    getRowsCount ()
    {
        if (this.values == null)
            return 0;

        return this.values.size ();
    }

    public TableFieldType[]
    getFields ()
    {
        try
        {
            assert this.type ().getFields ().length > 0;

            return this.type ().getFields ();
        }
        catch (Throwable e)
        {
            assert false;
        }
       return null;
    }

    public boolean
    isEmptyRow (int row)
    {
        final Vector<Value> rowValues = this.values.get (row);

        if (rowValues == null)
            return true;

        assert rowValues.size () == this.getFields ().length;

        for (Value f : rowValues)
        {
            if ((f != null) && ! f.isNull ())
                return false;
        }

        return true;
    }

    private Vector<Vector<Value>>      values;
}
