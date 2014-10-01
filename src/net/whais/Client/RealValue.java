package net.whais.Client;

import java.math.BigDecimal;

class RealValue extends Value
{
    RealValue(ValueType type, String value)
    {
        super( type);

        if ((value == null) || (value.length() == 0))
            mValue = null;

        else
            mValue = new BigDecimal( value);
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof RealValue))
            return false;

        final RealValue o = (RealValue) p;
        try {
            if ( ! type().equals( o.type()))
                return false;
        } catch (Throwable e) {
            return false;
        }

        if (isNull() != o.isNull())
            return false;

        else if (isNull())
            return true;

        return mValue.compareTo( o.mValue) == 0;
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return mValue.toString();
    }

    @Override
    public boolean isNull()
    {
        return (mValue == null);
    }

    private final BigDecimal mValue;
}
