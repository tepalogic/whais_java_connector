package net.whais.Client;


public class TextValue extends Value implements Comparable<TextValue>
{
    public TextValue (String value)
    {
        this.value = value;
    }
    
    public TextValue ()
    {
        this (null);
    }
    
    @Override
    public String toString ()
    {
        if (isNull ())
            return "";
        
        return value;
    }

    @Override
    public boolean isNull ()
    {
        return (value == null) || (value.length () == 0);
    }

    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.textType ();
    }
    
    @Override
    public int compareTo (TextValue o)
    {
        if (this.isNull ())
        {
            if (o.isNull ())
                return 0;
            
            return -1;
        }
        
        if (o.isNull ())
            return 1;
        
        return this.value.compareTo (o.value);
    }

    private String value;
}
