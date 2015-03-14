import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.Connection;
import net.whais.Client.Value;
import test.net.whais.Client.CommnandLine;


public class TestErrorConditions
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

        TestErrorConditions test = new TestErrorConditions ();

        boolean testResult = test.testMissingProcedureCall (c);
        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }

    boolean testMissingProcedureCall (Connection c) throws IOException
    {
        final String procName = "my_name_is_missing_prceodure1234";

        System.out.print ("Executing test procedure '" + procName + "' ... ");
        c.pushStackValue (Value.createDate ("1970/01/01"));

        try
        {
            c.executeProcedure (procName);
        }
        catch (IOException e)
        {
            System.out.println (" OK");
            return true;
        }

        System.out.println (" FAIL");
        return false;
    }
}
