package net.whais.Client;

@SuppressWarnings("rawtypes")
public class DateValue extends Value implements Comparable
{
    public DateValue (int year, int month, int day) throws ConnException
    {
        this.year = year;
        this.month = (byte) month;
        this.day = (byte) day;
        this.nullValue = false;

        validateDate ();
    }
    
    public DateValue (String v) throws ConnException
    {
        if ((v == null) || (v.length () == 0))
        {
            this.year = 0;
            this.month = 0;
            this.day = 0;
            this.nullValue = true;
            
            return ;
        }
        
        int yearEndOffset, monthEndOffset;

        if (((yearEndOffset = v.indexOf('-', 1)) < 0)
            || ((monthEndOffset = v.indexOf ('-', yearEndOffset)) < 0))
        {
            throw new ConnException (CmdResult.INVALID_ARGS, "Invalid date format. A valid format is YYYY-MM-DD.");
        }

        this.year = Integer.parseInt (v.substring(0, yearEndOffset));
        this.month = (byte) Integer.parseInt (v.substring(yearEndOffset + 1, monthEndOffset));
        this.day = (byte) Integer.parseInt (v.substring (monthEndOffset + 1));
        
        this.nullValue = false;
        
        validateDate ();
    }
    
    public DateValue ()
    {
        this.year  = 0;
        this.month = 0;
        this.day   = 0;
        
        this.nullValue = true;
    }
    
    @Override
    public int compareTo (Object v)
    {
        DateValue o = (DateValue) v;

        if (this.isNull ())
        {
            if (o.isNull ())
                return 0;
            
            return -1;
        }
        
        if (o.isNull ())
            return 1;
        
        if (this.year != o.year)
            return this.year - o.year;
        
        if (this.month != o.month)
            return this.month - o.month;
        
        return this.day - o.day;
    }

    @Override
    public String toString ()
    {
        return "" + year + '-' + month + '-' + day; 
    }

    @Override
    public boolean isNull ()
    {
        return nullValue;
    }

    @Override
    public ValueType type () throws ConnException
    {
        return ValueType.dateType ();
    }
    
    private final void validateDate () throws ConnException 
    {
        if (isNull ())
            return ;
        
        if ((month < 1) || (month > 12))
            throw new ConnException (CmdResult.INVALID_ARGS, "Date's month is not valid.");
        
        if (day < 1)
            throw new ConnException (CmdResult.INVALID_ARGS, "Date's day is not valid.");
            
        if (month == 2)
        {
            if (isLeapYear (this.year))
            {
                if (day > 29)
                    throw new ConnException (CmdResult.INVALID_ARGS, "The month of the specified date does not have so many days.");
            }
            else if (day > 28)
                throw new ConnException (CmdResult.INVALID_ARGS, "The month of the specified date does not have so many days.");
        }
        else
        {
            if (day > sMonths[month - 1])
                throw new ConnException (CmdResult.INVALID_ARGS, "The month of the specified date does not have so many days.");
        }
    }

    public final int getYear () throws ConnException
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the year of a null date value.");
        return this.year;
    }
    
    public final int getMonth () throws ConnException
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the month of a null date value.");

        return this.month;
    }
    
    public final int getDay () throws ConnException
    {
        if (isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the day of a null date value.");

        return this.day;
    }
    
    public static final boolean isLeapYear (final int year)
    {
      if ((year % 4) == 0)
        {
          if ((year % 100) == 0)
            {
              if ((year % 400) == 0)
                return true;

              else
                return false;
            }
          else
            return true;
        }
      
      return false;
    }

    
    private final int     year;
    private final byte    month;
    private final byte    day;
    private final boolean nullValue;
    
    private static final byte[] sMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

}
