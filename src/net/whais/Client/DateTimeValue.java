package net.whais.Client;

public class DateTimeValue extends DateValue 
{
    public DateTimeValue (int year, int month, int day, int hours, int mins, int secs) throws ConnException
    {
        super (year, month, day);
        
        this.hours = (byte) hours;
        this.mins  = (byte) mins;
        this.secs  = (byte) secs;
        
        validateTime ();
    }
    
    public DateTimeValue (String value) throws ConnException
    {
        super (value.substring (0, value.indexOf (' ')));
        
        String timeValue = value.substring (value.indexOf(' ') + 1);
        
        int hoursEndOffset, minsEndOffset;

        if (((hoursEndOffset = timeValue.indexOf(':')) < 0)
            || ((minsEndOffset = timeValue.indexOf (':', hoursEndOffset)) < 0))
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid time format. A valid format is 'YYYY-MM-DD hh:mm:ss'.");
        }

        this.hours = (byte) Integer.parseInt (timeValue.substring(0, hoursEndOffset));
        this.mins  = (byte) Integer.parseInt (timeValue.substring(hoursEndOffset + 1, minsEndOffset));
        this.secs  = (byte) Integer.parseInt (timeValue.substring (minsEndOffset + 1));
        
        validateTime ();
    }
    
    public DateTimeValue ()
    {
        super ();
        hours = mins = secs = (byte) 0;
    }
    
    @Override
    public int compareTo (Object v)
    {
        final int sr = super.compareTo (v);
        if (sr != 0)
            return sr;
        
        DateTimeValue o = (DateTimeValue) v;
        
        if (this.hours != o.hours)
            return this.hours - o.hours;
        
        if (this.mins != o.mins)
            return this.mins -o.mins;
        
        return this.secs - o.secs;
    }
    
    @Override
    public String toString ()
    {
        if (isNull ())
            return "";
        
        return super.toString () + ' ' + this.hours + ':' + this.mins + ':' + this.secs; 
    }
    
    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.datetimeType ();
    }
    
    private final void validateTime () throws ConnException
    {
        if (isNull())
            return ;
        
        if ((this.hours < 0) || (this.hours > 23))
            throw new ConnException (CmdResult.INVALID_ARGS, "The DateTime value's hour should be in he interval [0-23].");
        
        if ((this.mins < 0) || (this.mins > 59))
            throw new ConnException (CmdResult.INVALID_ARGS, "The DateTime value's minute should be in he interval [0-59].");

        if ((this.secs < 0) || (this.secs > 59))
            throw new ConnException (CmdResult.INVALID_ARGS, "The DateTime value's seconds should be in he interval [0-59].");
    }

    public final int getHours () throws ConnException 
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the hours of a null time value.");
        return this.hours;
    }
    
    public final int getMins () throws ConnException 
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the minutes of a null time value.");
        return this.mins;
    }

    public final int getSecs () throws ConnException 
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the seconds of a null time value.");
        return this.secs;
    }
    
    private final byte hours;
    private final byte mins;
    private final byte secs;
}
