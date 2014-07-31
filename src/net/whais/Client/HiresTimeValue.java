package net.whais.Client;

public class HiresTimeValue extends DateTimeValue
{
    public HiresTimeValue (int year, int month, int day, int hours, int mins, int secs, int usecs) throws ConnException
    {
        super (year, month, day, hours, mins, secs);
        
        if ((usecs < 0) || (usecs > 99999))
            throw new ConnException (CmdResult.INVALID_ARGS, "The HiresTime value's microseconds should be in he interval [0-99999].");
        
        this.usecs = usecs;
    }
    
    public HiresTimeValue (String value) throws ConnException
    {
        super (value.substring (0, value.indexOf ('.')));
        
        final int usecStartOff = value.indexOf ('.') + 1;
        if ((usecStartOff <= 0)
            || ((value.length () - usecStartOff + 1) < 5))
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid high resolution time format. A valid format is 'YYYY-MM-DD hh:mm:ss.uuuuu'");
        }
        
        int usec = 0;
        for (int i = usecStartOff; i < value.length (); ++i)
        {
            final char c = value.charAt (i);
            if ((c < '0') || ('9' < c))
                throw new NumberFormatException ();
            
            usec *= 10;
            usec += c - '0';
        }
        this.usecs = usec;
    }
    
    public HiresTimeValue ()
    {
        super ();
        usecs = 0;
    }
    
    @Override
    public String toString ()
    {
        if (isNull ())
            return "";
        
        return super.toString () + String.format ("%05d", this.usecs);
    }
    
    @Override
    public int compareTo (Object v)
    {
        final int sr = super.compareTo (v);
        if (sr != 0)
            return sr;
        
        return this.usecs - ((HiresTimeValue) v).usecs;
    }
    
    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.hirestimeType ();
    }
    
    public final int getMicrosecs () throws ConnException
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the microseconds of a null time value.");
        return this.usecs;
    }
    
    private final int usecs;
}
