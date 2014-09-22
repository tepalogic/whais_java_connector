package net.whais.Client;

import java.util.Vector;

public class FieldValue extends Value
{
    FieldValue (ValueType type) throws ConnException
    {
        super (type);

        this.rows = null;
    }

    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof FieldValue))
            return false;

        final FieldValue o = (FieldValue) p;
        if (this.isNull () != o.isNull())
            return false;

        else if (this.isNull ())
            return true;

        else if (this.rows.size () != o.rows.size ())
            return false;

        final int rowsCount = this.rows.size ();
        for (int row = 0; row < rowsCount; ++row)
        {
            final Value tRow = this.rows.get (row);
            final Value oRow = this.rows.get (row);

            if (tRow == null)
            {
                if (oRow != null)
                    return false;
            }
            else if (oRow == null)
                return false;

            if (! tRow.equals (oRow))
                return false;
        }

        return true;
    }

    void add (Value v) throws ConnException
    {
        assert (v.type().isBasic() || v.type().isArray());
        assert (! v.type().isTable() || v.type().isField ());

        if ((v != null) && (v.isNull ()))
            this.rows.add (null);

        this.rows.add (v);
    }

    public Value get (int row) throws ConnException
    {
        if ((this.rows == null) || (this.rows.size () <= row))
            throw new ArrayIndexOutOfBoundsException (row);

        final Value result = this.rows.get (row);
        if (result == null)
        {
            int type = this.type ().getTypeId() & ~ValueType.FIELD_MASK;
            if (ValueType.isArray (type))
                return Value.createArray (ValueType.create(type));

            assert ValueType.isBasic (type);

            return Value.createBasic(ValueType.create(type));
        }

        assert ! result.isNull ();

        return result;
    }

    @Override
    public String toString ()
    {
        if (this.isNull ())
            return "[]";

        final int rowsCount = this.rows.size ();
        assert (rowsCount > 0);

        final StringBuilder resultBuilder = new StringBuilder ().append('[');
        for (int r = 0; r < rowsCount; ++r)
        {
            resultBuilder.append ('[');
            if (this.rows.get (r) != null)
                resultBuilder.append (this.rows.get (r).toString ());

            if (r < rowsCount - 1)
                resultBuilder.append("] ");

            else
                resultBuilder.append (']');
        }

        return resultBuilder.toString ();
    }

    @Override
    public boolean isNull ()
    {
        return (this.rows == null) || (this.rows.size () == 0);
    }

    private Vector<Value> rows;
}
