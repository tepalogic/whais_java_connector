package net.whais.Client;

import java.io.IOException;

/**
 * A specialized form of {@link Value} designed for better manipulation of
 * time related WHAIS value types (e.g. date, date&time or high resolution
 * time values).
 * <p>
 * Unlike other types, a user might need its own formats to display time
 * related information and object of this type will provides several accessors
 * to time related components.</p>
 *
 * @version 1.0
 */
public class TimeValue extends Value
{
    TimeValue( ValueType        type,
               int              year,
               int              month,
               int              day,
               int              hours,
               int              mins,
               int              secs,
               int              usecs) throws ConnException
    {
        super( type);

        mYear = year;
        mMonth = (byte) month;
        mDay = (byte) day;
        mHours = (byte) hours;
        mMins = (byte) mins;
        mSecs = (byte) secs;
        mMicrosecs = usecs;

        mIsNull = false;

        validateDate();
    }

    TimeValue(ValueType type, String v) throws ConnException
    {
        super( type);

        if ( ! (type.equals( ValueType.dateType())
                || (type.equals( ValueType.datetimeType()))
                || (type.equals( ValueType.hirestimeType())))) {
            throw new ConnException( CmdResult.INVALID_ARGS, "The constructor needs a time related type.");
        }

        if ((v == null) || (v.length() == 0)) {
            mMicrosecs = mYear = mMonth = mDay = mHours = mMins = mSecs = 0;
            mIsNull = true;
            return;
        }

        try {

            final int yearEndOffset = v.indexOf( '/');
            final int monthEndOffset = v.indexOf( '/', yearEndOffset + 1);
            final int dayEndOffset = type.equals( ValueType.dateType()) ? v.length() : v.indexOf( ' ');

            mYear = Integer.parseInt( v.substring( 0, yearEndOffset));
            mMonth = (byte) Integer.parseInt( v.substring( yearEndOffset + 1, monthEndOffset));
            mDay = (byte) Integer.parseInt( v.substring( monthEndOffset + 1, dayEndOffset));

            if (type.equals( ValueType.dateType())) {
                mMicrosecs = mHours = mMins = mSecs = 0;
                mIsNull = false;

                validateDate();

                return;
            }

            v = v.substring( dayEndOffset + 1);
            final int hoursEndOffset = v.indexOf( ':');
            final int minsEndOffset = v.indexOf( ':', hoursEndOffset + 1);
            final int secsEndOffset = type.equals( ValueType.datetimeType()) ? v.length() : v.indexOf( '.');

            mHours = (byte) Integer.parseInt( v.substring( 0, hoursEndOffset));
            mMins = (byte) Integer.parseInt( v.substring( hoursEndOffset + 1, minsEndOffset));
            mSecs = (byte) Integer.parseInt( v.substring( minsEndOffset + 1, secsEndOffset));

            if (type.equals( ValueType.datetimeType())) {
                mMicrosecs = 0;
                mIsNull = false;

                validateDate();

                return;
            }

            v = v.substring( secsEndOffset + 1);
            int usecs = 0;
            for (int i = 0; i < 6; ++i) {
                usecs *= 10;
                if (i < v.length())
                    usecs += v.charAt( i) - '0';
            }

            mMicrosecs = usecs;
            mIsNull = false;
            validateDate();
        } catch (Exception e) {
            ConnException buildExcept = null;
            if (type.equals( ValueType.dateType())) {
                buildExcept = new ConnException( CmdResult.INVALID_ARGS,
                        "Failed to build date value. A valid format is 'YYYY/MM/DD");
            } else if (type.equals( ValueType.datetimeType())) {
                buildExcept = new ConnException( CmdResult.INVALID_ARGS,
                        "Failed to build date&time value. A valid format is 'YYYY/MM/DD hh:mm:ss");
            } else {
                assert type.equals( ValueType.hirestimeType());
                buildExcept = new ConnException( CmdResult.INVALID_ARGS,
                        "Failed to build high resolution time value. A valid format is 'YYYY/MM/DD hh:mm:ss.uuuuuu");
            }
            buildExcept.initCause( e);
            throw buildExcept;
        }
    }

    /**
     * Returns {@code true} of both objects holds the same type of WHAIS time
     * related type (e.g both holds a date, or bold holds a date&time values)
     * and their fields are equal.
     */
    @Override
    public boolean equals( Object p)
    {
        if (this == p)
            return true;
        else if ( ! (p instanceof TimeValue))
            return false;

        final TimeValue o = (TimeValue) p;
        try {
            if ( ! type().equals( o.type()))
                return false;
        } catch (Throwable e) {
            return false;
        }

        if (isNull() != o.isNull())
            return false;
        else if (isNull())
            return true;

        //For date&time types that don't use all of these component the
        //constructor should have set them to 0 by default.
        return (mYear == o.mYear)
                && (mMonth == o.mMonth)
                && (mDay == o.mDay)
                && (mHours == o.mHours)
                && (mMins == o.mMins)
                && (mSecs == o.mSecs)
                && (mMicrosecs == o.mMicrosecs);
    }

    /**
     * Returns a default representation the WHAIS time value.
     */
    @Override
    public String toString()
    {
        if (mPrecomputeString != null)
            return mPrecomputeString;

        String s = "";

        if (!isNull()) {
            try {
                if (type().equals( ValueType.dateType()))
                    s = "" + mYear + '/' + mMonth + '/' + mDay;

                else if (type().equals( ValueType.datetimeType())) {
                    s = "" + mYear + '/' + mMonth + '/' + mDay + ' ' + mHours + ':' + mMins + ':' + mSecs;
                }

                else {
                    s = "" + mYear + '/' + mMonth + '/' + mDay + ' ' + mHours + ':' + mMins + ':' + mSecs + '.'
                           + String.format( "%06d", mMicrosecs);
                }
            } catch (IOException e) {
                s = "";
            }
        }

        mPrecomputeString = s;

        return mPrecomputeString;
    }

    /**
     * Returns {@code true} if the WHAIS time value is null.
     */
    @Override
    public boolean isNull()
    {
        return mIsNull;
    }

    private final void validateDate() throws ConnException
    {
        if (isNull())
            return;

        if ((mMonth < 1) || (mMonth > 12))
            throw new ConnException( CmdResult.INVALID_ARGS, "Date's month is not valid.");

        if (mDay < 1)
            throw new ConnException( CmdResult.INVALID_ARGS, "Date's day is not valid.");

        if (mMonth == 2) {
            if (isLeapYear( mYear)) {
                if (mDay > 29)
                    throw new ConnException( CmdResult.INVALID_ARGS,
                                             "The month of the specified date does not have so many days.");
            } else if (mDay > 28)
                throw new ConnException( CmdResult.INVALID_ARGS,
                                         "The month of the specified date does not have so many days.");
        } else if (mDay > sMonths[mMonth - 1]) {
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "The month of the specified date does not have so many days.");
        }

        if ((mHours < 0) || (mHours > 23))
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "The DateTime value's hour should be in he interval [0-23].");

        if ((mMins < 0) || (mMins > 59))
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "The DateTime value's minute should be in he interval [0-59].");

        if ((mSecs < 0) || (mSecs > 59))
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "The DateTime value's seconds should be in he interval [0-59].");
    }

    /**
     * Get the year part of WHAIS time value.
     *
     * @throws ConnException
     *            The caller should check first this is a non null value.
     */
    public final int getYear() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the year of a null date value.");

        return mYear;
    }

    /**
     * Get the month part of WHAIS time value.
     *
     * @return
     *            The month values are counted from {@code 1} for <em>January</em> to
     *            {@code 12} for <em>December</em>.
     * @throws ConnException
     *            The caller should check first this is a non null value.
     */
    public final int getMonth() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the month of a null date value.");

        return mMonth;
    }

    /**
     * Get the day part of WHAIS time value.
     *
     * @return
     *            The month's day count start with {@code 1}.
     * @throws ConnException
     *            The caller should check first this is a non null value.
     */
    public final int getDay() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the day of a null date value.");

        return mDay;
    }

    /**
     * Get the hours part of WHAIS time value.
     *
     * @return
     *            The hour part of the time value. If this does not actually
     *            hold a WHAIS date&time or high resolution value (e.g.
     *            it holds simply a WHAIS date value) than this will be
     *            {@code 0} by default.
     * @throws ConnException
     *            The caller should check first this is a non null value.
     * @since 1.0
     */
    public final int getHours() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the hours of a null time value.");

        return mHours;
    }

    /**
     * Get the minutes part of WHAIS time value.
     *
     * @return
     *            The minutes part of the time value. If this does not actually
     *            hold a WHAIS date&time or high resolution value (e.g.
     *            it holds simply a WHAIS date value) than this will be
     *            {@code 0} by default.
     * @throws ConnException
     *            The caller should check first this is a non null value.
     * @since 1.0
     */
    public final int getMinutes() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the minutes of a null time value.");
        return mMins;
    }

    /**
     * Get the seconds part of WHAIS time value.
     *
     * @return
     *            The seconds part of the time value. If this does not actually
     *            hold a WHAIS date&time or high resolution value (e.g.
     *            it holds simply a WHAIS date value) than this will be
     *            {@code 0} by default.
     * @throws ConnException
     *            The caller should check first this is a non null value.
     * @since 1.0
     */
    public final int getSeconds() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the seconds of a null time value.");

        return mSecs;
    }

    /**
     * Get the microseconds part of WHAIS time value.
     *
     * @return
     *            The microseconds part of the time value. If this does not
     *            actually hold a WHAIS high resolution value (e.g. it holds
     *            a WHAIS date or a date&time value) than this will be
     *            {@code 0} by default.
     * @throws ConnException
     *            The caller should check first this is a non null value.
     * @since 1.0
     */
    public final int getMicroseconds() throws ConnException
    {
        if (isNull())
            throw new ConnException( CmdResult.INVALID_ARGS, "Requested the microseconds of a null time value.");

        return mMicrosecs;
    }

    /**
     * Check for a leap year.
     * @param year
     *            Year to check for.
     * @return
     *            It will return {@code true} if the year leaps.
     */
    public static final boolean isLeapYear( final int year)
    {
        if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                if ((year % 400) == 0)
                    return true;
                else
                    return false;
            } else
                return true;
        }

        return false;
    }

    private final int mYear;
    private final byte mMonth;
    private final byte mDay;
    private final byte mHours;
    private final byte mMins;
    private final byte mSecs;
    private final int mMicrosecs;
    private final boolean mIsNull;
    private String mPrecomputeString;

    private static final byte[] sMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
}
