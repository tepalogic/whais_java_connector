package net.whais.Client;

public class CharValue extends Value implements Comparable<CharValue>
{
    public CharValue (int codePoint) throws ConnException
    {
        if (codePoint < 0)
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid code point value.");
        
        this.value = codePoint;
    }
    
    public CharValue (String v) throws ConnException
    {
        if ((v == null)
            || (v.length () == 0))
        {
            this.value = 0;
            return ;
        }

        if (v.codePointCount (0, v.length ()) > 1)
            throw new ConnException (CmdResult.INVALID_ARGS, "The string used to construct a character value contains more than one code points");
        
        this.value = v.codePointAt(0);
    }
    
    public CharValue ()
    {
        this.value = 0;
    }
    
    @Override
    public int compareTo (CharValue o)
    {
        if (this.isNull ())
        {
            if (o.isNull ())
                return 0;
            
            return -1;
        }
        
        if (o.isNull ())
            return 1;
        
        return this.value - o.value;
    }

    @Override
    public String toString ()
    {
        if (isNull ())
        return "";
        
        return new String (Character.toChars (this.value));
    }

    @Override
    public boolean isNull ()
    {
        return this.value == 0;
    }

    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.charType ();
    }
    
    private final int value;
}
