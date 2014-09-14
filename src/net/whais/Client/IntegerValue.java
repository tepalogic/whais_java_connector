package net.whais.Client;

import java.math.BigInteger;

class IntegerValue extends Value
{
    IntegerValue (ValueType type, long value)
    {
        super (type);
        this.value = new BigInteger ("" + value);
    }

    IntegerValue (ValueType type, String v)
    {
        super (type);

        if ((v != null) && (v.length () > 0))
            this.value = new BigInteger(v);

        else
            this.value = null;
    }

    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof IntegerValue))
            return false;

        final IntegerValue o = (IntegerValue) p;
        try
        {
            if ( ! this.type ().equals (o.type ()))
                return false;
        }
        catch (Throwable e)
        {
            System.err.println ("Unexpected error!");
            System.exit (-1);
        }

        if (this.isNull () != o.isNull())
            return false;

        else if (this.isNull())
            return true;

        return this.value.compareTo (o.value) == 0;
    }

    @Override
    public String toString ()
    {
        if (this.value != null)
            return this.value.toString ();

        return "";
    }

    @Override
    public boolean isNull ()
    {
        return this.value == null;
    }

    private final BigInteger value;
}
