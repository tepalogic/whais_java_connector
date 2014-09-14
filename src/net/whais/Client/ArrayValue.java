package net.whais.Client;

import java.util.Vector;

public class ArrayValue extends Value
{
    ArrayValue (ValueType type, Value[] values) throws ConnException
    {
        super (type);

        if (values == null)
        {
            this.values = null;
            return ;
        }

        this.values = new Vector<Value> ();
        for (int i = 0; i < values.length; ++i)
        {
            if ((values[i] == null) || values[i].isNull ())
            {
                throw new ConnException (
                                CmdResult.INVALID_ARGS,
                                "An array should may not hold a null values."
                                        );
            }
            else if (! type.equals(values[i].type ()))
            {
                throw new ConnException (
                        CmdResult.INVALID_ARGS,
                        "Cannot add a value of type "
                            + values[i].type ().toString ()
                            + " to an array of type " + type.toString () + '.'
                                         );
            }

            this.values.add (values[i]);
        }
    }

    ArrayValue (ValueType type) throws ConnException
    {
        this (type, null);
    }

    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof ArrayValue))
            return false;

        final ArrayValue o = (ArrayValue) p;

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

        else if (this.isNull())
            return true;

        else if (this.values.size () != o.values.size ())
            return false;

        for (int i = 0; i < this.values.size (); ++i)
        {
            if (! this.get(i).equals (o.get(i)))
                return false;
        }

        return true;
    }

    public void add (Value v) throws ConnException
    {
        if (v.isNull ())
        {
            throw new ConnException (CmdResult.INVALID_ARGS,
                                     "Cannot add a null value to an array.");
        }

        if (! v.type ().isBasic ()
            || v.type ().equals (ValueType.textType ()))
        {
            throw new ConnException (CmdResult.INVALID_ARGS,
                                     "An array may hold only basic values.");
        }

        if (this.values == null)
        {
            this.values = new Vector<Value> ();
            this.values.add (v);

            return ;
        }

        if ( ! ValueType.create (this.type ().getBaseType ()).equals(v.type ()))
        {
            throw new ConnException (CmdResult.INVALID_ARGS,
                                     "Cannot add a value of type "
                                         + v.type ().toString ()
                                         + " to an array of type "
                                         + this.type ().toString () + '.');
        }
        this.values.add (v);
    }

    public Value get (int i)
    {
        if ((this.values == null) || (this.values.size () <= i))
            throw new ArrayIndexOutOfBoundsException (i);

        return this.values.get (i);
    }

    public Value remove (int i)
    {
        if ((this.values == null) || (this.values.size () <= i))
            throw new ArrayIndexOutOfBoundsException (i);

        return this.values.remove (i);
    }

    public Value[] toArray ()
    {
        Value[] result = new Value[this.values.size ()];

        if (this.isNull ())
            return null;

        return this.values.toArray (result);
    }

    @Override
    public String toString ()
    {
        if (this.isNull ())
            return "";

        final int count = this.values.size ();
        assert (count > 0);

        final StringBuilder resultBuilder = new StringBuilder ().append('[');
        for (int r = 0; r < count; ++r)
        {
            resultBuilder.append (this.values.get (r).toString ());
            if (r < count - 1)
                resultBuilder.append(", ");
        }
        resultBuilder.append (']');

        return resultBuilder.toString ();
    }

    @Override
    public boolean isNull ()
    {
        return (this.values == null) || (this.values.size () == 0);
    }

    private Vector<Value> values;
}
