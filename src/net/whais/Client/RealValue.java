package net.whais.Client;

import java.math.BigDecimal;

public class RealValue extends Value implements Comparable<RealValue>
{
    public RealValue (double value)
    {
        this.value = new BigDecimal (value);
    }
    
    public RealValue (String value)
    {
        if ((value == null) || (value.length () == 0))
            this.value = null;
        
        else
            this.value = new BigDecimal (value);
    }
    
    public RealValue (BigDecimal value)
    {
        this.value = value;
    }
    
    public RealValue ()
    {
        this.value = null;
    }

    @Override
    public String toString ()
    {
        if (isNull ())
            return "";
                    
        return value.toString ();
    }

    @Override
    public boolean isNull ()
    {
        return (this.value == null);
    }

    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.realType ();
    }
    
    @Override
    public int compareTo (RealValue o)
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

    private final  BigDecimal    value;
}
