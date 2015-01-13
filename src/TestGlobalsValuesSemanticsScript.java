import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.Connection;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;


public class TestGlobalsValuesSemanticsScript
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

        TestNullValuesScript test = new TestNullValuesScript ();

        boolean testResult = executeTest (c, 1);
        testResult &= executeTest (c, 2);

        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }


    static boolean executeTest (Connection c, int step) throws IOException {

        System.out.print ("Step " + step + ": Executing test for globals values semantics ... ");
        Value expectedResult = Value.createBasic (ValueType.boolType(), "1");

        c.executeProcedure ("test_global_value_semantic_init");
        Value result = c.retrieveStackTop ();
        if ( ! result.equals(expectedResult)) {
            System.out.println ("FAIL: Cound not initialize the test.");
            return false;
        }

        c.executeProcedure ("test_global_value_semantic");
        result = c.retrieveStackTop ();
        if ( ! expectedResult.equals(result)) {
            System.out.println ("FAIL: The globals values did not behave.");
            return false;
        }

        System.out.println (" OK");
        return true;
    }


}
