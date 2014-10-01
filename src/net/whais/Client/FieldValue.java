package net.whais.Client;

import java.util.Vector;

public class FieldValue extends Value
{
    FieldValue(ValueType type) throws ConnException
    {
        super( type);

        mRows = null;
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if (!(p instanceof FieldValue))
            return false;

        final FieldValue o = (FieldValue) p;
        if (isNull() != o.isNull())
            return false;
        else if (isNull())
            return true;
        else if (mRows.size() != o.mRows.size())
            return false;

        final int rowsCount = mRows.size();
        for (int row = 0; row < rowsCount; ++row) {
            final Value tRow = mRows.get( row);
            final Value oRow = mRows.get( row);

            if (tRow == null) {
                if (oRow != null)
                    return false;
            } else if (oRow == null)
                return false;

            if (!tRow.equals( oRow))
                return false;
        }

        return true;
    }

    void add( Value v) throws ConnException
    {
        assert (v.type().isBasic() || v.type().isArray());
        assert (!v.type().isTable() || v.type().isField());

        if (mRows == null)
            mRows = new Vector<Value>();

        if ((v != null) && (v.isNull()))
            mRows.add( null);

        mRows.add( v);
    }

    public Value get( int row) throws ConnException
    {
        if (getRowsCount() <= row)
            throw new ArrayIndexOutOfBoundsException( row);

        final Value result = mRows.get( row);
        if (result == null) {
            int type = type().getTypeId() & ~ValueType.FIELD_MASK;
            if (ValueType.isArray( type))
                return Value.createArray( ValueType.create( type));

            assert ValueType.isBasic( type);

            return Value.createBasic( ValueType.create( type));
        }

        return result;
    }

    public int getRowsCount()
    {
        return (mRows == null) ? 0 : mRows.size();
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "[]";

        final int rowsCount = mRows.size();
        assert (rowsCount > 0);

        final StringBuilder resultBuilder = new StringBuilder().append( '[');
        for (int r = 0; r < rowsCount; ++r) {
            resultBuilder.append( '[');
            if (mRows.get( r) != null)
                resultBuilder.append( mRows.get( r).toString());

            if (r < rowsCount - 1)
                resultBuilder.append( "] ");

            else
                resultBuilder.append( ']');
        }

        return resultBuilder.toString();
    }

    @Override
    public boolean isNull()
    {
        return (getRowsCount() == 0);
    }

    private Vector<Value> mRows;
}
