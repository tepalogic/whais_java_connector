package net.whais.Client;

public class BoolValue extends Value implements Comparable<BoolValue>
{
    public BoolValue (boolean value)
    {
        this.value      = value;
        this.nullValue  = false;
    }
    
    public BoolValue (String s)
    {
        if ((s == null) || (s.length () == 0))
        {
            this.value      = false;
            this.nullValue  = true;
        }
        else
        {
            this.nullValue = false;
            this.value = (s.equals ("1") 
                          || s.toLowerCase().equals ("true"));
        }
    }
    
    public BoolValue ()
    {
        this.value      = false;
        this.nullValue  = true;
    }
    
    @Override
    public int compareTo (BoolValue o)
    {
        if (this.isNull ())
        {
            if (o.isNull ())
                return 0;
            
            return -1;
        }
        
        if (o.isNull ())
            return 1;
        
        if (this.value)
            return o.value ? 0 : 1;
        
        return o.value ? -1 : 0;
    }

    @Override
    public String toString ()
    {
        if (isNull ())
            return "";
        
        else
            return this.value ? "1" : "0";
    }

    @Override
    public boolean isNull ()
    {
        return this.nullValue;
    }

    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.boolType ();
    }
    
    private final boolean   value;
    private final boolean   nullValue;
}
