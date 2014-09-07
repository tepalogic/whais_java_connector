package net.whais.Client;

import java.io.IOException;


class TimeValue extends Value
{
    TimeValue (ValueType type,
               int       year,
               int       month,
               int       day,
               int       hours,
               int       mins,
               int       secs,
               int       usecs ) throws ConnException
    {
        super (type);

        this.year       = year;
        this.month      = (byte) month;
        this.day        = (byte) day;
        this.hours      = (byte) hours;
        this.mins       = (byte) mins;
        this.secs       = (byte) secs;
        this.usecs      = usecs;

        this.nullValue = false;

        this.validateDate ();
    }

    TimeValue (ValueType type, String v) throws ConnException
    {
        super (type);

        if ( ! (type.equals (ValueType.dateType ())
                || (type.equals (ValueType.datetimeType()))
                || (type.equals (ValueType.hirestimeType()))))
        {
            throw new ConnException (CmdResult.INVALID_ARGS,
                                     "The constructor needs a time related type.");
        }

        if ((v == null) || (v.length () == 0))
        {
            this.year = 0;
            this.month = 0;
            this.day = 0;
            this.hours = 0;
            this.mins = 0;
            this.secs = 0;
            this.usecs = 0;
            this.nullValue = true;

            return ;
        }

        try
        {

            final int yearEndOffset  = v.indexOf ('/');
            final int monthEndOffset = v.indexOf ('/', yearEndOffset + 1);
            final int dayEndOffset   = type.equals (ValueType.dateType ()) ?
                                           v.length () :
                                           v.indexOf (' ');

            this.year  = Integer.parseInt (v.substring(0, yearEndOffset));
            this.month = (byte) Integer.parseInt (v.substring(yearEndOffset + 1, monthEndOffset));
            this.day   = (byte) Integer.parseInt (v.substring (monthEndOffset + 1, dayEndOffset));

            if (type.equals (ValueType.dateType ()))
            {
                this.hours = this.mins = this.secs = 0;
                this.usecs = 0;
                this.nullValue = false;

                this.validateDate ();

                return ;
            }

            v = v.substring (dayEndOffset + 1);
            final int hoursEndOffset = v.indexOf(':');
            final int minsEndOffset = v.indexOf(':', hoursEndOffset + 1);
            final int secsEndOffset = type.equals (ValueType.datetimeType ()) ?
                                           v.length ():
                                           v.indexOf('.');

            this.hours = (byte) Integer.parseInt (v.substring(0, hoursEndOffset));
            this.mins  = (byte) Integer.parseInt (v.substring(hoursEndOffset + 1, minsEndOffset));
            this.secs  = (byte) Integer.parseInt (v.substring (minsEndOffset + 1, secsEndOffset));


            if (type.equals(ValueType.datetimeType()))
            {
                this.usecs = 0;
                this.nullValue = false;

                this.validateDate ();

                return ;
            }

            v = v.substring (secsEndOffset + 1);
            int usecs = 0;
            for (int i = 0; i < 6; ++i)
            {
                usecs *= 10;
                if (i < v.length())
                    usecs += v.charAt(i) - '0';
            }

            this.usecs = usecs;
            this.nullValue = false;
        }
        catch (Exception e)
        {
            ConnException buildExcept = null;
            if (type.equals(ValueType.dateType()))
            {
                buildExcept = new ConnException(CmdResult.INVALID_ARGS,
                                                "Failed to build date value. A valid format is 'YYYY/MM/DD");
            }
            else if (type.equals (ValueType.datetimeType()))
            {
                buildExcept = new ConnException(CmdResult.INVALID_ARGS,
                                                "Failed to build datetime value. A valid format is 'YYYY/MM/DD hh:mm:ss");
            }
            else
            {
                assert type.equals(ValueType.hirestimeType());
                buildExcept = new ConnException(CmdResult.INVALID_ARGS,
                                                "Failed to build hiresolution time value. A valid format is 'YYYY/MM/DD hh:mm:ss.uuuuu");
            }
            buildExcept.initCause (e);
            throw buildExcept;
        }

        this.validateDate ();
    }


    @Override
    public boolean equals (Object p)
    {
        if (this == p)
            return true;

        else if ( ! (p instanceof TimeValue))
            return false;

        final TimeValue o = (TimeValue) p;
        try
        {
            if ( ! this.type ().equals (o.type ()))
                return false;
        }
        catch (Throwable e)
        {
            return false;
        }

        if (this.isNull () != o.isNull())
            return false;

        else if (this.isNull ())
            return true;

        try
        {
            switch (this.type ().getBaseType ())
            {
            case ValueType.HIRESTIME:
                if (this.usecs != o.usecs)
                    return false;
                /* Fall through ! */

            case ValueType.DATETIME:
                if ((this.secs != o.secs)
                    || (this.mins != o.mins)
                    || (this.hours != o.hours))
                {
                    return false;
                }

                /* Fall through ! */
            case ValueType.DATE:
                if ((this.day != o.day)
                    || (this.month != o.month)
                    || (this.year != o.year))
                {
                    return false;
                }
                break;

            default:
                assert false;
                return false;
            }
        }
        catch (Throwable e)
        {
            return false;
        }

        return true;
    }


    @Override
    public String toString ()
    {
        if (this.precomputeString != null)
            return this.precomputeString;

        String s = "";

        if ( ! this.isNull ())
        {
            try
            {
                if (this.type ().equals (ValueType.dateType ()))
                    s = "" + this.year + '/' + this.month + '/' + this.day;

                else if (this.type ().equals (ValueType.datetimeType ()))
                {
                    s = "" + this.year + '/' + this.month + '/' + this.day + ' ' +
                             this.hours + ':' + this.mins + ':' + this.secs;
                }

                else
                {
                    s = "" + this.year + '/' + this.month + '/' + this.day + ' ' +
                             this.hours + ':' + this.mins + ':' + this.secs + '.' +
                             String.format ("%06d", this.usecs);
                }
            }
            catch (IOException e)
            {
                s = "";
            }
        }

        this.precomputeString = s;

        return this.precomputeString;
    }

    @Override
    public boolean isNull ()
    {
        return this.nullValue;
    }

    private final void validateDate () throws ConnException
    {
        if (this.isNull ())
            return ;

        if ((this.month < 1) || (this.month > 12))
            throw new ConnException (CmdResult.INVALID_ARGS, "Date's month is not valid.");

        if (this.day < 1)
            throw new ConnException (CmdResult.INVALID_ARGS, "Date's day is not valid.");

        if (this.month == 2)
        {
            if (isLeapYear (this.year))
            {
                if (this.day > 29)
                    throw new ConnException (CmdResult.INVALID_ARGS, "The month of the specified date does not have so many days.");
            }
            else if (this.day > 28)
                throw new ConnException (CmdResult.INVALID_ARGS, "The month of the specified date does not have so many days.");
        }
        else
        {
            if (this.day > sMonths[this.month - 1])
                throw new ConnException (CmdResult.INVALID_ARGS, "The month of the specified date does not have so many days.");
        }

        if ((this.hours < 0) || (this.hours > 23))
            throw new ConnException (CmdResult.INVALID_ARGS, "The DateTime value's hour should be in he interval [0-23].");

        if ((this.mins < 0) || (this.mins > 59))
            throw new ConnException (CmdResult.INVALID_ARGS, "The DateTime value's minute should be in he interval [0-59].");

        if ((this.secs < 0) || (this.secs > 59))
            throw new ConnException (CmdResult.INVALID_ARGS, "The DateTime value's seconds should be in he interval [0-59].");
    }

    public final int getYear () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the year of a null date value.");

        return this.year;
    }

    public final int getMonth () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the month of a null date value.");

        return this.month;
    }

    public final int getDay () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the day of a null date value.");

        return this.day;
    }

    public final int getHours () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the hours of a null time value.");

        return this.hours;
    }

    public final int getMinutes () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the minutes of a null time value.");
        return this.mins;
    }

    public final int getSeconds () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the seconds of a null time value.");

        return this.secs;
    }

    public final int getMicroseconds () throws ConnException
    {
        if (this.isNull ())
            throw new ConnException(CmdResult.INVALID_ARGS, "Requested the microseconds of a null time value.");

        return this.usecs;
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
    private final byte    hours;
    private final byte    mins;
    private final byte    secs;
    private final int     usecs;
    private final boolean nullValue;
    private String        precomputeString;

    private static final byte[] sMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

}
