package net.whais.Client;

class TextValue extends Value
{
    TextValue(String value)
    {
        super( ValueType.textType());
        mValue = value;
    }

    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if ( ! (p instanceof TextValue))
            return false;

        return toString().equals( p.toString());
    }

    @Override
    public String toString()
    {
        if (isNull())
            return "";

        return mValue;
    }

    @Override
    public boolean isNull()
    {
        return (mValue == null) || (mValue.length() == 0);
    }

    private String mValue;
}
