import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.whais.Client.Connection;
import test.net.whais.Client.CommnandLine;

public class TestWhaisField implements WhaisAPITester
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

        TestWhaisField t = new TestWhaisField();

        boolean testResult = true;
        for (WhaisApiTestCases testCase : t.GetTests())
            testResult &= testCase.DoTests(c);

        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }

    public TestWhaisField()
    {
        testCases.add(new WhaisApiTestCases("get_table",
                "Test with NULL field parameters",
                "Test with persistent and local tables fields (verify table storage 1)",
                "Test with persistent and local tables fields for fields updates (via returned table)",
                "Test with persistent and local tables fields (verify table storage 2)"));

        testCases.add(new WhaisApiTestCases("is_indexed",
                "Test with NULL field parameters",
                "Test with non-local table fields (for well defined tables)"));

        testCases.add(new WhaisApiTestCases("get_name",
                "Test with NULL field parameters",
                "Test simple field name verifications",
                "Test complex field name verifications"));

        testCases.add(new WhaisApiTestCases("get_smallest",
                "Test with NULL field parameters",
                "Test with default values."));

        testCases.add(new WhaisApiTestCases("get_biggest",
                "Test with NULL field parameters",
                "Test with default values."));

        testCases.add(new WhaisApiTestCases("match_rows",
                "Test with NULL field parameters",
                "Test with interval values (non indexed field)."));

        testCases.add(new WhaisApiTestCases("match_rows",
                "Test with NULL field parameters",
                "Test with interval values (non indexed field).",
                "Test with interval rows (non indexed field).",
                "Test with interval rows defaults (non indexed field).",
                "Test with interval rows defaults 2 (non indexed field).",
                "Test with interval values (non indexed field).",
                "Test with interval rows (non indexed field).",
                "Test with interval rows defaults (non indexed field).",
                "Test with interval rows defaults 2 (non indexed field)."));

        testCases.add(new WhaisApiTestCases("filter_rows",
                "Test with NULL field parameters",
                "Test with particular values",
                "Test with particular rows",
                "Test with to verify NULL row values",
                "Test tp verify the filter out verification"));

    }

    public List<WhaisApiTestCases> GetTests()
    {
        return testCases;
    }

    private List<WhaisApiTestCases> testCases = new ArrayList<>();

    @Override
    public boolean DoTest( Connection c) throws IOException
    {
        return false;
    }
}
