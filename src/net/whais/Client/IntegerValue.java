package net.whais.Client;

import java.math.BigInteger;

public class IntegerValue extends Value implements Comparable<IntegerValue>
{
    public IntegerValue (long value)
    {
        this.value = new BigInteger ("" + value);
    }
    
    public IntegerValue (String v)
    {
        if ((v != null) && (v.length () > 0)) 
            this.value = new BigInteger(v);
        
        else
            this.value = null;
    }
    
    public IntegerValue (BigInteger v)
    {
        this.value = v;
    }
    
    public IntegerValue ()
    {
        this.value = null;
    }

    @Override
    public boolean isNull ()
    {
        return value == null;
    }

    @Override
    public String toString ()
    {
        if (this.value != null)
            return this.value.toString ();
        
        return "";
    }

    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.int64Type ();
    }

    @Override
    public int compareTo (IntegerValue o)
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

    private final BigInteger value;
}
