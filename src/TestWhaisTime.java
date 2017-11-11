import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;

import net.whais.Client.Connection;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;

public class TestWhaisTime
{
    public static void main (String[] args) throws IOException
    {
        String[]  customArgs = {
                                "-d",
                                "test_exec_db",
                                "--fs",
                                "32768"
                               };

        final int customCount = customArgs.length;
        customArgs = Arrays.copyOf(customArgs, customCount + args.length);
        for (int i = 0; i < args.length; ++i)
            customArgs[customCount + i] = args[i];

        CommnandLine cmdLine = new CommnandLine (customArgs);
        Connection c = new Connection (cmdLine.getHostname (),
                                       cmdLine.getPort (),
                                       cmdLine.getDatabase (),
                                       cmdLine.getPassword (),
                                       (byte) cmdLine.getUserId (),
                                       cmdLine.getMaxFrameSize ());

        boolean testResult = true;
        
        TestWhaisTime t = new TestWhaisTime();
      
        testResult &= t.executeTestAddDays(c);
        testResult &= t.executeTestAddSeconds(c);
        testResult &= t.executeTestAddMicroSeconds(c);
        
        testResult &= t.executeTestDay(c);
        testResult &= t.executeTestDaysDiff(c);
        
        testResult &= t.executeTestHours(c);
        testResult &= t.executeTestMicroseconds(c);
        testResult &= t.executeTestMinutes(c);
        testResult &= t.executeTestSeconds(c);
        testResult &= t.executeTestMonth(c);  
        
        testResult &= t.executeTestNow(c);
        
        testResult &= t.executeTestSecondsDiff(c);
        testResult &= t.executeTestMicosecsDiff(c);
        
        testResult &= t.executeTestTicks(c);
        
        testResult &= t.executeTestWeek(c);
        testResult &= t.executeTestWeekday(c);
        testResult &= t.executeTestYear(c);
        testResult &= t.executeTestYearLeaps(c);
        testResult &= t.executeLastWeekDate(c);
        
        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }
    
    boolean executeTestYearLeaps(Connection c) throws IOException {
        System.out.print("Check year_leaps with NULL parameter ... ");
        Value result = c.callProcedure( "year_leaps", Value.createInt64());
        Value expected = Value.createBool();

        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
            System.out.println("OK");
        
        System.out.print("Check year_leaps with 2000 parameter ... ");
        result = c.callProcedure( "year_leaps", Value.createInt64(2000));
        expected = Value.createBool(true);
        
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
            System.out.println("OK");
            
        System.out.print( "Check year_leaps with 2017 parameter ... ");
        result = c.callProcedure( "year_leaps", Value.createInt64(2017));
        expected = Value.createBool(false);
        if (!result.equals( expected)) {
            System.out
                    .println( "FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println( "OK");
        
        System.out.print( "Check year_leaps with 1900 parameter ... ");
        result = c.callProcedure( "year_leaps", Value.createInt64(1900));
        expected = Value.createBool(false);
        if (!result.equals( expected)) {
            System.out
                    .println( "FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println( "OK");
        
        System.out.print( "Check year_leaps with 1800 parameter ... ");
        result = c.callProcedure( "year_leaps", Value.createInt64(1800));
        expected = Value.createBool(false);
        if (!result.equals( expected)) {
            System.out
                    .println( "FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println( "OK");
   
        return true;
    }
    
    boolean executeTestYear(Connection c) throws IOException {
        System.out.print("Check year with NULL parameter ... ");
        Value result = c.callProcedure( "year", Value.createDate());
        Value expected = Value.createInt32();

        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check year with '9081-09-25'  ... ");
        result = c.callProcedure( "year", Value.createDate("9081-09-25"));
        expected = Value.createInt32(9081);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check year with '-81-2-01'  ... ");
        result = c.callProcedure( "year", Value.createDate("-81-2-01"));
        expected = Value.createInt32(-81);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check year with '2015-5-28'  ... ");
        result = c.callProcedure( "year", Value.createDate("2015-5-28"));
        expected = Value.createInt32(2015);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
            
        return true;
    }
    
    boolean executeTestWeekday(Connection c) throws IOException {
        System.out.print("Check weekday with NULL parameter ... ");
        Value result = c.callProcedure( "weekday", Value.createDate());
        Value expected = Value.createUInt8();

        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check weekday with '2017-11-9'  ... ");
        result = c.callProcedure( "weekday", Value.createDate("2017-11-9"));
        expected = Value.createUInt8(4);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check weekday with '2017-11-16'  ... ");
        result = c.callProcedure( "weekday", Value.createDate("2017-11-16"));
        expected = Value.createUInt8(4);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check weekday with '2017-11-03'  ... ");
        result = c.callProcedure( "weekday", Value.createDate("2017-11-03"));
        expected = Value.createUInt8(5);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check weekday with '2017-02-28'  ... ");
        result = c.callProcedure( "weekday", Value.createDate("2017-02-28"));
        expected = Value.createUInt8(2);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check weekday with '2016-2-29'  ... ");
        result = c.callProcedure( "weekday", Value.createDate("2016-2-29"));
        expected = Value.createUInt8(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check weekday with '2016-03-6'  ... ");
        result = c.callProcedure( "weekday", Value.createDate("2016-03-6"));
        expected = Value.createUInt8(7);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");

        return true;
    }
    
    boolean executeTestWeek(Connection c) throws IOException {
        System.out.print        ("Check week with NULL parameter ... ");
        Value result = c.callProcedure( "week", Value.createDate());
        Value expected = Value.createUInt8();

        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2017-01-1'  ... ");
        result = c.callProcedure( "week", Value.createDate("2017-01-1"));
        expected = Value.createUInt8(52);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2017-1-2'  ... ");
        result = c.callProcedure( "week", Value.createDate("2017-1-2"));
        expected = Value.createUInt8(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2016-12-31'  ... ");
        result = c.callProcedure( "week", Value.createDate("2016-12-31"));
        expected = Value.createUInt8(52);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2015-01-01'  ... ");
        result = c.callProcedure( "week", Value.createDate("2015-01-1"));
        expected = Value.createUInt8(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2014-12-29'  ... ");
        result = c.callProcedure( "week", Value.createDate("2015-01-1"));
        expected = Value.createUInt8(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2016-1-31'  ... ");
        result = c.callProcedure( "week", Value.createDate("2016-1-31"));
        expected = Value.createUInt8(4);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2015-12-31'  ... ");
        result = c.callProcedure( "week", Value.createDate("2015-12-31"));
        expected = Value.createUInt8(53);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2016-02-15'  ... ");
        result = c.callProcedure( "week", Value.createDate("2016-02-15"));
        expected = Value.createUInt8(7);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check week with '2017-06-28'  ... ");
        result = c.callProcedure( "week", Value.createDate("2017-06-28"));
        expected = Value.createUInt8(26);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        

        return true;
    }
    
    boolean executeTestTicks(Connection c) throws IOException {
        System.out.print("Check ticks ... ");
        
        int count = 1;
        while (true)
        {
            Value result = c.callProcedure( "ticks");
    
            if (result.isNull() || ! result.type().equals(ValueType.create(ValueType.UINT64))) {
                System.out.println("FAIL (unexpected value or type from first tick call)");
                return false;
            }
            
            long t1 = Long.parseLong(result.toString());
    
            result = c.callProcedure( "ticks");
            if (result.isNull() || ! result.type().equals(ValueType.create(ValueType.UINT64))) {
                System.out.println("FAIL (unexpected value or type from second tick call)");
                return false;
            }
            
            long t2 = Long.parseLong(result.toString());
            long tdelta = t2-t1;
            
            if ((t1 < 0) || (t2 < 0)) {
                System.out.println("FAIL (negative tick value recived).");
                return false;
            } else if (t2 < t1) {
                System.out.println("FAIL (second tick value is less than the first one).");
                return false;
            } else if (t2 - t1 > 3) {
                if (count >= 5)
                {
                    System.out.println("FAIL (consecutive tick values are more than 3 milis apart).");
                    return false;
                }
            }
            else
                break;
        }
         
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestNow(Connection c) throws IOException {
        System.out.print("Check now ... ");
        Value result = c.callProcedure( "now");

        if (result.isNull() || ! result.type().equals(ValueType.create(ValueType.HIRESTIME))) {
            System.out.println("FAIL (unexpected value or type from first tick call)");
            return false;
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestSecondsDiff(Connection c) throws IOException {
        System.out.print("Check seconds diff with first parameter NULL ... ");
        Value expected = Value.createInt64();
        Value result = c.callProcedure( "seconds_diff", Value.createDateTime(), Value.createDateTime("1980-12-31 13:23:10"));

        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds diff with second parameter NULL ... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:10"), Value.createDateTime());
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds_diff \"1980-12-31 13:23:10\" and \"1980-12-31 13:23:11\" ... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:10"), Value.createDateTime("1980-12-31 13:23:11"));
        expected = Value.createInt64(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds_diff \"1980-12-31 13:24:09\" and \"1980-12-31 13:23:9\" ... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:24:09"), Value.createDateTime("1980-12-31 13:23:9"));
        expected = Value.createInt64(-60);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds_diff \"1980-12-31 14:23:11\" and \"1980-12-31 13:23:10\" ... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 14:23:11"), Value.createDateTime("1980-12-31 13:23:10"));
        expected = Value.createInt64(-3601);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds_diff \"1981-1-1 13:23:11\" and \"1980-12-31 13:23:10\" ... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1981-1-1 13:23:11"), Value.createDateTime("1980-12-31 13:23:10"));
        expected = Value.createInt64(-86401);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check seconds_diff \"1980-12-31 13:23:11\" and \"1980-12-31 13:23:10\"... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:11"), Value.createDateTime("1980-12-31 13:23:10"));
        expected = Value.createInt64(-1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");

        System.out.print("Check seconds_diff \"1980-12-31 13:23:9\" and \"1980-12-31 13:23:9\"... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:9"), Value.createDateTime("1980-12-31 13:24:9"));
        expected = Value.createInt64(60);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check seconds_diff \"1980-12-31 13:23:10\" and \"1980-12-31 14:23:11\"... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:10"), Value.createDateTime("1980-12-31 14:23:11"));
        expected = Value.createInt64(3601);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds_diff \"1980-12-31 13:23:10\" and \"1980-12-31 14:23:11\"... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:10"), Value.createDateTime("1981-1-1 13:23:11"));
        expected = Value.createInt64(86401);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check seconds_diff \"1980-12-31 13:23:10\" and \"1980-12-31 14:22:11\"... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:10"), Value.createDateTime("1980-12-31 14:22:11"));
        expected = Value.createInt64(3541);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds_diff \"1980-12-31 13:23:10\" and \"1980-12-30 14:22:11\"... ");
        result = c.callProcedure( "seconds_diff", Value.createDateTime("1980-12-31 13:23:10"), Value.createDateTime("1980-12-30 14:22:11"));
        expected = Value.createInt64(-82859);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    
    boolean executeTestMicroseconds(Connection c) throws IOException {
        System.out.print("Check useconds with NULL parameter... ");
        Value result = c.callProcedure("useconds" , Value.createHiresTime());
        Value expected = Value.createUInt32();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check useconds with NON null parameter... ");
        result = c.callProcedure("useconds" , Value.createHiresTime("2981-3-4 09:1:2.123"));
        expected = Value.createUInt32(123000);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check useconds with NON null parameter (2)... ");
        result = c.callProcedure("useconds" , Value.createHiresTime("2981-3-4 09:1:2.0123"));
        expected = Value.createUInt32(12300);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check useconds with NON null parameter (3)... ");
        result = c.callProcedure("useconds" , Value.createHiresTime("2981-3-4 09:1:2.01230"));
        expected = Value.createUInt32(12300);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        return true;
        
    }
    
    boolean executeTestSeconds(Connection c) throws IOException {
        System.out.print("Check seconds with NULL parameter... ");
        Value result = c.callProcedure("seconds" , Value.createDateTime());
        Value expected = Value.createUInt8();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check seconds with NON null parameter... ");
        result = c.callProcedure("seconds" , Value.createDateTime("2981-3-4 09:1:2"));
        expected = Value.createUInt8(2);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check seconds with NON null parameter 2 ... ");
        result = c.callProcedure("seconds" , Value.createDateTime("2981-02-01 08:02:03"));
        expected = Value.createUInt8(3);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }

    boolean executeTestMinutes(Connection c) throws IOException {
        System.out.print("Check minutes with NULL parameter... ");
        Value result = c.callProcedure("minutes" , Value.createDateTime());
        Value expected = Value.createUInt8();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check minutes with NON null parameter... ");
        result = c.callProcedure("minutes" , Value.createDateTime("2981-3-4 09:1:2"));
        expected = Value.createUInt8(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check minutes with NON null parameter 2 ... ");
        result = c.callProcedure("minutes" , Value.createDateTime("2981-02-01 08:02:03"));
        expected = Value.createUInt8(2);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestHours(Connection c) throws IOException {
        System.out.print("Check hours with NULL parameter... ");
        Value result = c.callProcedure("hours" , Value.createDateTime());
        Value expected = Value.createUInt8();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check hours with NON null parameter... ");
        result = c.callProcedure("hours" , Value.createDateTime("2981-3-4 9:1:2"));
        expected = Value.createUInt8(9);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check hours with NON null parameter 2 ... ");
        result = c.callProcedure("hours" , Value.createDateTime("2981-02-01 08:02:03"));
        expected = Value.createUInt8(8);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestDay(Connection c) throws IOException {
        System.out.print("Check day with NULL parameter... ");
        Value result = c.callProcedure("day" , Value.createDateTime());
        Value expected = Value.createUInt8();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check day with NON null parameter... ");
        result = c.callProcedure("day" , Value.createDate("2981-3-4"));
        expected = Value.createUInt8(4);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check day with NON null parameter 2 ... ");
        result = c.callProcedure("day" , Value.createDate("2981-02-01"));
        expected = Value.createUInt8(1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestMonth(Connection c) throws IOException {
        System.out.print("Check month with NULL parameter... ");
        Value result = c.callProcedure("month" , Value.createDate());
        Value expected = Value.createUInt8();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check month with NON null parameter... ");
        result = c.callProcedure("month" , Value.createDate("2981-3-4"));
        expected = Value.createUInt8(3);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check month with NON null parameter 2 ... ");
        result = c.callProcedure("month" , Value.createDate("2981-02-01"));
        expected = Value.createUInt8(2);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestAddDays(Connection c) throws IOException {
        System.out.print("Check add_days with first parameter NULL... ");
        Value result = c.callProcedure("add_days" , Value.createHiresTime(), Value.createInt64(1));
        Value expected = Value.createHiresTime();
        if (! result.equals(expected)) {
            System.out.println("FAIL (retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_days with second parameter NULL... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("1-1-1 1:1:1.1"), Value.createInt64());
        if (! result.equals(expected)) {
            System.out.println("FAIL (retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check add_days with \"1-1-1 1:1:1.1\" and 1 ... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("1-1-1 1:1:1.1"), Value.createInt64(1));
        expected = Value.createHiresTime("1-1-2 1:1:1.1");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_days with \"1-1-1 1:1:1.1\" and -1  ... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("1-1-1 1:1:1.1"), Value.createInt64(-1));
        expected = Value.createHiresTime("0-12-31 1:1:1.1");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_days with \"0-1-1 1:1:1.1\" and -1  ... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("0-1-1 1:1:1.1"), Value.createInt64(-1));
        expected = Value.createHiresTime("-1-12-31 1:1:1.1");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check add_days with \"2012-12-12 19:1:21.0\" and 0  ... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("2012-12-12 19:1:21.0"), Value.createInt64(0));
        expected = Value.createHiresTime("2012-12-12 19:1:21.0");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_days with \"2012-12-31 19:1:21.0\" and 3  ... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("2012-12-31 19:1:21.0"), Value.createInt64(3));
        expected = Value.createHiresTime("2013-1-3 19:1:21.0");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_days with \"2012-12-31 19:1:21.0\" and -3  ... ");
        result = c.callProcedure("add_days" , Value.createHiresTime("2012-12-31 19:1:21.0"), Value.createInt64(-3));
        expected = Value.createHiresTime("2012-12-28 19:1:21.0");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    
    boolean executeTestAddSeconds(Connection c) throws IOException {
        System.out.print("Check add_seconds with first parameter NULL... ");
        Value result = c.callProcedure("add_seconds" , Value.createHiresTime(), Value.createInt64(1));
        Value expected = Value.createHiresTime();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_seconds with second parameter NULL... ");
        result = c.callProcedure("add_seconds" , Value.createHiresTime("1-1-1 1:1:1.1"), Value.createInt64());
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_seconds with \"2012-12-31 19:1:21.02\" and -24  ... ");
        result = c.callProcedure("add_seconds" , Value.createHiresTime("2012-12-31 19:1:21.02"), Value.createInt64(-24));
        expected = Value.createHiresTime("2012-12-31 19:0:57.02");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check add_seconds with \"2012-12-31 19:1:21.02\" and 21911  ... ");
        result = c.callProcedure("add_seconds" , Value.createHiresTime("2012-12-31 19:1:21.02"), Value.createInt64(21911));
        expected = Value.createHiresTime("2013-1-1 1:6:32.02");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }

    boolean executeTestAddMicroSeconds(Connection c) throws IOException {
        System.out.print("Check add_useconds with first parameter NULL... ");
        Value result = c.callProcedure("add_useconds" , Value.createHiresTime(), Value.createInt64(1));
        Value expected = Value.createHiresTime();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_useconds with second parameter NULL... ");
        result = c.callProcedure("add_useconds" , Value.createHiresTime("1-1-1 1:1:1.1"), Value.createInt64());
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_useconds with \"2012-12-31 19:1:21.02\" and -24  ... ");
        result = c.callProcedure("add_useconds" , Value.createHiresTime("2012-12-31 19:1:21.02"), Value.createInt64(-24));
        expected = Value.createHiresTime("2012-12-31 19:1:21.019976");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check add_useconds with \"2012-12-31 19:1:21.02\" and 21911  ... ");
        result = c.callProcedure("add_useconds" , Value.createHiresTime("2012-12-31 19:1:21.02"), Value.createInt64(21911));
        expected = Value.createHiresTime("2012-12-31 19:1:21.041911");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_seconds with \"2012-12-31 19:1:21.02\" and 5021911  ... ");
        result = c.callProcedure("add_useconds" , Value.createHiresTime("2012-12-31 19:1:21.02"), Value.createInt64(5021911));
        expected = Value.createHiresTime("2012-12-31 19:1:26.041911");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check add_useconds with \"2012-12-31 19:1:21.02\" and -6000024  ... ");
        result = c.callProcedure("add_useconds" , Value.createHiresTime("2012-12-31 19:1:21.02"), Value.createInt64(-6000024));
        expected = Value.createHiresTime("2012-12-31 19:1:15.019976");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }    
    
    
    boolean executeTestDaysDiff(Connection c) throws IOException {
        System.out.print("Check days_diff with first parameter NULL... ");
        Value result = c.callProcedure("days_diff" , Value.createDate(), Value.createDate("1981-10-2"));
        Value expected = Value.createInt64();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check days_diff with second parameter NULL... ");
        result = c.callProcedure("days_diff" , Value.createDate("1981-10-2"), Value.createDate());
        expected = Value.createInt64();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check days_diff with '2010-12-11' and '2010-12-10' ... ");
        result = c.callProcedure("days_diff" , Value.createDate("2010-12-11"), Value.createDate("2010-12-10"));
        expected = Value.createInt64(-1);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check days_diff with '2010-12-11' and '2011-12-11' ... ");
        result = c.callProcedure("days_diff" , Value.createDate("2010-12-11"), Value.createDate("2011-12-11"));
        expected = Value.createInt64(365);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check days_diff with '2010-12-11' and '2011-11-8' ... ");
        result = c.callProcedure("days_diff" , Value.createDate("2010-12-11"), Value.createDate("2011-11-8"));
        expected = Value.createInt64(332);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check days_diff with '2010-12-11' and '2010-10-08' ... ");
        result = c.callProcedure("days_diff" , Value.createDate("2010-12-11"), Value.createDate("2010-10-08"));
        expected = Value.createInt64(-64);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check days_diff with '2010-12-11' and '2007-12-1' ... ");
        result = c.callProcedure("days_diff" , Value.createDate("2010-12-11"), Value.createDate("2007-12-1"));
        expected = Value.createInt64(-1106);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check days_diff with '2010-12-11' and '2007-12-14' ... ");
        result = c.callProcedure("days_diff" , Value.createDate("2010-12-11"), Value.createDate("2007-12-14"));
        expected = Value.createInt64(-1093);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestMicosecsDiff(Connection c) throws IOException {
        System.out.print("Check useconds_diff with first parameter NULL... ");
        Value result = c.callProcedure("useconds_diff" , Value.createHiresTime(), Value.createHiresTime("1981-10-2 0:0:1.1"));
        Value expected = Value.createInt64();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check useconds_diff with second parameter NULL... ");
        result = c.callProcedure("useconds_diff" , Value.createHiresTime("1981-10-2 0:0:1.1"), Value.createHiresTime());
        expected = Value.createInt64();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check useconds_diff with  1981-10-2 0:0:1.1 and 1981-10-2 0:0:1.022 ... ");
        result = c.callProcedure("useconds_diff" , Value.createHiresTime("1981-10-2 0:0:1.1"), Value.createHiresTime("1981-10-2 0:0:1.022"));
        expected = Value.createInt64(-78000);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check useconds_diff with  1981-10-2 0:0:1.193001 and 1981-10-2 1:1:5.720923 ... ");
        result = c.callProcedure("useconds_diff" , Value.createHiresTime("1981-10-2 0:0:1.193001"), Value.createHiresTime("1981-10-2 1:1:5.720923"));
        expected = Value.createInt64(3664527922l);
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        return true;
    }
    
    
    boolean executeLastWeekDate(Connection c) throws IOException {
        System.out.print("Check last_week_date with year parameter NULL... ");
        Value result = c.callProcedure("last_week_date" , Value.createInt64(), Value.createUInt8(1));
        Value expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check last_week_date with week parameter NULL... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(1), Value.createUInt8());
        expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with year parameter out of bounds ... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(10902910), Value.createUInt8());
        expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with week parameter out of bounds ... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2017), Value.createUInt8(53));
        expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check last_week_date with week parameter out of bounds (2)... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2015), Value.createUInt8(54));
        expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with week parameter out of bounds (3)... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2015), Value.createUInt8(54));
        expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with week parameter out of bounds (4)... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2015), Value.createUInt8(0));
        expected = Value.createDate();
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with week 2015 and 23 ... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2015), Value.createUInt8(41));
        expected = Value.createDate("2015-10-11");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        System.out.print("Check last_week_date with week 2016 and 23 ... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2016), Value.createUInt8(23));
        expected = Value.createDate("2016-6-12");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with week 2016 and 1 ... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2016), Value.createUInt8(1));
        expected = Value.createDate("2016-1-10");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        System.out.print("Check last_week_date with week 2017 and 3 ... ");
        result = c.callProcedure("last_week_date" , Value.createInt64(2017), Value.createUInt8(3));
        expected = Value.createDate("2017-1-22");
        if (! result.equals(expected)) {
            System.out.println("FAIL (the type retrieved is '" + result + "' rather than expected '" + expected + "')");
            return false;
        }
        System.out.println("OK");
        
        
        
        return true;
    }
}
