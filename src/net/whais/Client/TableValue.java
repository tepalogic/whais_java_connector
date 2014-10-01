package net.whais.Client;

import java.util.Vector;

/**
 * This specialized for of {@link Value} is intend to ease the manipulation of
 * WHAIS table values. It provides extra capabilities specific to table values
 * like retrieving the number of rows, retrieval or update a specific table
 * cell and so on.
 *
 * @version 1.0
 */
public class TableValue extends Value
{
    TableValue(TableFieldType[] fields) throws ConnException
    {
        super( ValueType.create( fields));

        mRows = null;
        if ((fields == null) || (fields.length == 0)) {
            throw new ConnException( CmdResult.INVALID_ARGS, "No fields descriptors supplied to create a table.");
        }
    }

    /**
     * Returns {@code true} if both class holds a WHAIS table and each having
     * the same fields and the same table cell values.
     *
     * @since 1.0
     */
    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;

        else if (!(p instanceof TableValue))
            return false;

        final TableValue o = (TableValue) p;
        try {
            if (!type().equals( o.type()))
                return false;
        } catch (Throwable e) {
            return false;
        }

        if (isNull() != o.isNull())
            return false;

        else if (isNull())
            return true;

        final int rowsCount = getRowsCount();
        if (rowsCount != o.getRowsCount())
            return false;

        for (int i = 0; i < rowsCount; ++i) {
            final Vector<Value> thisRow = mRows.get( i);
            final Vector<Value> othRow = o.mRows.get( i);

            if (thisRow == null) {
                if (othRow != null)
                    return false;
                continue;
            } else if (othRow == null)
                return false;

            if (thisRow.size() != othRow.size())
                return false;

            for (int j = 0; j < thisRow.size(); ++j) {
                final Value tf = thisRow.get( j);
                final Value of = othRow.get( j);

                if (tf == null) {
                    if (of == null)
                        continue;
                    else
                        return false;
                }

                if (!tf.equals( of))
                    return false;
            }
        }

        return true;
    }

    /**
     * Returns a string representation for a WHAIS table.
     */
    @Override
    public String toString()
    {
        if (isNull())
            return "";

        final int rowsCount = getRowsCount();
        assert (rowsCount > 0);

        final StringBuilder resultBuilder = new StringBuilder( '(');
        for (int r = 0; r < rowsCount; ++r) {
            int f = 0;
            resultBuilder.append( '[');

            final TableFieldType[] fields = getFields();
            for (TableFieldType field : fields) {
                resultBuilder.append( field.getName());
                Value cell = null;
                try {
                    cell = get( field.getName(), r);
                } catch (Throwable e) {
                    assert (false);
                    cell = null;
                }

                if (cell == null)
                    resultBuilder.append( "(internal error)");
                else {
                    if (cell.isArray())
                        resultBuilder.append( cell);
                    else {
                        resultBuilder.append( '\'');
                        resultBuilder.append( cell);
                        resultBuilder.append( '\'');
                    }
                }

                if (f < fields.length - 1)
                    resultBuilder.append( ' ');
                else
                    resultBuilder.append( ']');

                ++f;
            }

            if (r < rowsCount - 1)
                resultBuilder.append( ' ');
        }

        return resultBuilder.append( ')').toString();
    }

    /**
     * Returns {@code true} if this a null table value (e.g. a table with no
     * rows).
     */
    @Override
    public boolean isNull()
    {
        return getRowsCount() == 0;
    }

    /**
     * Get the value of a table's cell.
     *
     * @param fieldName
     *            Cell's corresponding field name.
     * @param row
     *            Cell's corresponding row number. The row number should be
     *            counted from {@code 0} (e.g. first row is {@code 0}, second
     *            row is {@code 1} and so one).
     * @return
     *            An object holding the cell's value.
     *
     * @throws ConnException
     *
     * @see #put(Value, String, int)
     * @see #getRowsCount()
     *
     * @since 1.0
     */
    public Value get( String fieldName, int row) throws ConnException
    {
        if (isNull()) {
            throw new ConnException( CmdResult.INVALID_ARGS, "Cannot retrieve a value from a null table.");
        }

        final TableFieldType[] fields = getFields();
        int field = 0;
        for (field = 0; field < fields.length; ++field) {
            if (fields[field].getName().equals( fieldName))
                break;
        }

        if (field >= fields.length)
            throw new ConnException( CmdResult.INVALID_FIELD);

        if (row >= getRowsCount())
            throw new ConnException( CmdResult.INVALID_ROW);

        assert (mRows.get( row) == null) || (field < mRows.get( row).size());

        Value result = null;

        if (mRows.get( row) != null) {
            assert mRows.get( row).size() == fields.length;

            result = mRows.get( row).get( field);
        }

        if (result == null) {
            final ValueType t = fields[field].getType();
            if (t.isArray())
                result = Value.createArray( t);
            else
                result = Value.createBasic( t);
        }

        return result;
    }

    /**
     * Update the content of a table cell.
     *
     * @param value
     *            The new cell value.
     * @param fieldName
     *            The corresponding cell's field name.
     * @param row
     *            Cell's corresponding row number. The row number should be
     *            counted from {@code 0} (e.g. first row is {@code 0}, second
     *            row is {@code 1} and so one).
     *
     * @throws ConnException
     *
     * @see #get(String, int)
     * @see #getRowsCount()
     * @see #addRows(int)
     *
     * @since 1.0
     */
    public void put( Value value, String fieldName, int row) throws ConnException
    {
        if (row > getRowsCount())
            throw new ConnException( CmdResult.INVALID_ROW);
        else if (row == getRowsCount())
            addRows( 1);

        final TableFieldType[] fields = getFields();
        int field = 0;
        for (field = 0; field < fields.length; ++field) {
            if (fields[field].getName().equals( fieldName))
                break;
        }

        if (field >= fields.length)
            throw new ConnException( CmdResult.INVALID_FIELD);

        if ((value != null) && !value.type().equals( fields[field].getType())) {
            throw new ConnException( CmdResult.INVALID_ARGS, "The value type is different from field type");
        }

        if ((value != null) && value.isNull()) {
            if (mRows.get( row) != null) {
                mRows.get( row).set( field, null);
                for (Value v : mRows.get( row)) {
                    if ((v != null) && !v.isNull())
                        return;
                }

                mRows.set( row, null);
            }
        } else {
            Vector<Value> rowValues = mRows.get( row);
            if (rowValues == null) {
                rowValues = new Vector<Value>();
                for (int f = 0; f < fields.length; ++f)
                    rowValues.add( null);

                mRows.set( row, rowValues);
            }

            assert rowValues.size() == fields.length;

            mRows.get( row).set( field, value);
        }
    }

    /**
     * Add rows to a table.
     * <p>
     * Used to increase the count of rows of a table. The new rows will be
     * at the end of a table and all cell values will be set null by
     * default.</p>
     *
     * @param count
     *            The number of rows to be added to a table.
     * @since 1.0
     */
    public final void addRows( int count)
    {
        if (count == 0)
            return;

        if (mRows == null)
            mRows = new Vector<Vector<Value>>();

        for (int i = 0; i < count; ++i)
            mRows.add( null);
    }

    /**
     * Get the table's rows count.
     */
    public final int getRowsCount()
    {
        if (mRows == null)
            return 0;

        return mRows.size();
    }

    /**
     * Describe the fields of a table.
     *
     * @return
     *            Returns an array holding the description of the table's fields.
     *            If the tables is not completely defined (e.g. it may happen
     *            during the description of a global value or of a procedure
     *            that uses undefined tables) than this will be an array with
     *            the size of {@code 0}.
     *
     * @since 1.0
     */
    public TableFieldType[] getFields()
    {
        try {
            return type().getFields();
        } catch (Throwable e) {
            assert false;
        }
        return null;
    }

    /**
     * Check if the row is used.
     *
     * @param row
     *            The row index.
     * @return
     *            Returns {@code true} if all table cells associated with this
     *            row are holding WHAIS null values
     */
    public boolean isEmptyRow( int row)
    {
        final Vector<Value> rowValues = mRows.get( row);

        if (rowValues == null)
            return true;

        assert rowValues.size() == getFields().length;

        for (Value f : rowValues) {
            if ((f != null) && !f.isNull())
                return false;
        }

        return true;
    }

    private Vector<Vector<Value>> mRows;
}
