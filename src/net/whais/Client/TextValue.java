package net.whais.Client;


class TextValue extends Value
{
    TextValue (String value)
    {
        super (ValueType.textType ());
        this.value = value;
    }

    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof TextValue))
            return false;

        return this.toString().equals (p.toString());
    }

    @Override
    public String toString ()
    {
        if (this.isNull ())
            return "";

        return this.value;
    }

    @Override
    public boolean isNull ()
    {
        return (this.value == null) || (this.value.length () == 0);
    }

    private String value;
}
