import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.Connection;
import net.whais.Client.Value;
import test.net.whais.Client.CommnandLine;

public class TestApiMinMax
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

        TestApiMinMax test = new TestApiMinMax ();

        boolean testResult = true;

        testResult &= test.executeTest( c, "min_bool", Value.createBool("false"));
        testResult &= test.executeTest( c, "max_bool", Value.createBool("true"));

        testResult &= test.executeTest( c, "min_char", Value.createChar(new String (Character.toChars(1))));
        testResult &= test.executeTest( c, "max_char", Value.createChar(new String (Character.toChars(0x10FFFF))));

        testResult &= test.executeTest( c, "min_date", Value.createDate("-32768/1/1"));
        testResult &= test.executeTest( c, "max_date", Value.createDate("32767/12/31"));

        testResult &= test.executeTest( c, "min_dtime", Value.createDateTime( "-32768/1/1 0:0:0"));
        testResult &= test.executeTest( c, "max_dtime", Value.createDateTime( "32767/12/31 23:59:59"));

        testResult &= test.executeTest( c, "min_htime", Value.createHiresTime( "-32768/1/1 0:0:0.0"));
        testResult &= test.executeTest( c, "max_htime", Value.createHiresTime( "32767/12/31 23:59:59.999999"));

        testResult &= test.executeTest( c, "min_i8", Value.createInt8("-128"));
        testResult &= test.executeTest( c, "max_i8", Value.createInt8("127"));

        testResult &= test.executeTest( c, "min_i16", Value.createInt16("-32768"));
        testResult &= test.executeTest( c, "max_i16", Value.createInt16("32767"));

        testResult &= test.executeTest( c, "min_i32", Value.createInt32("-2147483648"));
        testResult &= test.executeTest( c, "max_i32", Value.createInt32("2147483647"));

        testResult &= test.executeTest( c, "min_i64", Value.createInt64("-9223372036854775808"));
        testResult &= test.executeTest( c, "max_i64", Value.createInt64("9223372036854775807"));


        testResult &= test.executeTest( c, "min_u8", Value.createUInt8("0"));
        testResult &= test.executeTest( c, "max_u8", Value.createUInt8("255"));

        testResult &= test.executeTest( c, "min_u16", Value.createUInt16("0"));
        testResult &= test.executeTest( c, "max_u16", Value.createUInt16("65535"));

        testResult &= test.executeTest( c, "min_u32", Value.createUInt32("0"));
        testResult &= test.executeTest( c, "max_u32", Value.createUInt32("4294967295"));

        testResult &= test.executeTest( c, "min_u64", Value.createUInt64("0"));
        testResult &= test.executeTest( c, "max_u64", Value.createUInt64("18446744073709551615"));

        testResult &= test.executeTest( c, "min_real", Value.createReal("-549755813888.999999"));
        testResult &= test.executeTest( c, "max_real", Value.createReal("549755813887.999999"));

        testResult &= test.executeTest( c, "min_rreal", Value.createRichReal("-9223372036854775808.99999999999999"));
        testResult &= test.executeTest( c, "max_rreal", Value.createRichReal("9223372036854775807.99999999999999"));

        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }
    
    
    public boolean executeTest(final Connection c, final String api, final Value v) {
        System.out.print ("Execute test " + api + ": ");
        
        Value result;

        try {
            c.popStackValues( Connection.ALL);
            result = c.callProcedure(api);
        } catch (IOException e) {
            System.out.println(" ... FAIL");
            e.printStackTrace();

            return false;
        }

        if ( ! result.equals( v)) {
            System.out.println(result.toString() + " ...  FAIL");

            return false;
        }
        
        System.out.println(result.toString() + " ...  OK");
        return true;
    }
}
