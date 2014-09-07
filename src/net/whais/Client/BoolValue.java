package net.whais.Client;

class BoolValue extends Value
{
    BoolValue (boolean value)
    {
        super (ValueType.boolType());

        this.value      = value;
        this.nullValue  = false;
    }

    BoolValue (String s)
    {
        super (ValueType.boolType());

        if ((s == null) || (s.length () == 0))
        {
            this.value      = false;
            this.nullValue  = true;
        }
        else
        {
            this.nullValue = false;
            this.value = (s.equals ("1")
                          || s.toLowerCase().equals ("T")
                          || s.toLowerCase().equals ("true"));
        }
    }

    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof BoolValue))
            return false;

        final BoolValue o = (BoolValue) p;
        if (this.isNull () != o.isNull())
            return false;

        else if (this.isNull())
            return true;

        return this.value == o.value;
    }

    @Override
    public String toString ()
    {
        if (this.isNull ())
            return "";

        else
            return this.value ? "1" : "0";
    }

    @Override
    public boolean isNull ()
    {
        return this.nullValue;
    }

    private final boolean   value;
    private final boolean   nullValue;
}
