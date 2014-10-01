package net.whais.Client;

class BoolValue extends Value
{
    BoolValue(boolean value)
    {
        super( ValueType.boolType());

        mValue = value;
        mIsNull = false;
    }

    BoolValue(String s)
    {
        super( ValueType.boolType());

        if ((s == null) || (s.length() == 0)) {
            mValue = false;
            mIsNull = true;
        } else {
            mIsNull = false;
            mValue = (s.equals( "1") || s.toLowerCase().equals( "T") || s.toLowerCase().equals( "true"));
        }
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if ( ! (p instanceof BoolValue))
            return false;

        final BoolValue o = (BoolValue) p;
        if (isNull() != o.isNull())
            return false;
        else if (isNull())
            return true;

        return mValue == o.mValue;
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return mValue ? "1" : "0";
    }

    @Override
    public boolean isNull()
    {
        return mIsNull;
    }

    private final boolean mValue;
    private final boolean mIsNull;
}
