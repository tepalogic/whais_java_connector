package net.whais.Client;

import java.math.BigDecimal;

public class RealValue extends Value
{
    RealValue (ValueType type, String value)
    {
        super (type);

        if ((value == null) || (value.length () == 0))
            this.value = null;

        else
            this.value = new BigDecimal (value);
    }

    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof RealValue))
            return false;

        final RealValue o = (RealValue) p;
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

        return this.value.compareTo (o.value) == 0;
    }

    @Override
    public String toString ()
    {
        if (this.isNull ())
            return "";

        return this.value.toString ();
    }

    @Override
    public boolean isNull ()
    {
        return (this.value == null);
    }

    private final  BigDecimal    value;
}
