import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.whais.Client.Connection;
import test.net.whais.Client.CommnandLine;

public class TestWhaisArray implements WhaisAPITester
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

        TestWhaisArray t = new TestWhaisArray();

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

    public TestWhaisArray()
    {
        testCases.add(new WhaisApiTestCases("count",
                      "Test with NULL arrays.",
                      "Test the result in various conditions",
                      "Test the result after arrays updates with NULL elements",
                      "Test the result after arrays updates with NULL elements(2)"));

        testCases.add(new WhaisApiTestCases("sort",
                      "Test with NULL arrays.",
                      "Test to verify the reverse parameter",
                      "Test to verify how the duplicate elements are removed"));

        testCases.add(new WhaisApiTestCases("get_min",
                      "Test with NULL arrays.",
                      "Test to verify the margin parameter",
                      "Test to verify the search start parameter"));

        testCases.add(new WhaisApiTestCases("get_max",
                      "Test with NULL arrays.",
                      "Test to verify the margin parameter",
                      "Test to verify the search start parameter"));

        testCases.add(new WhaisApiTestCases("truncate",
                      "Test with NULL arrays.",
                      "Test with the new count larger than current array's count",
                      "Test for various corner cases",
                      "Test with undefined array values"));

        testCases.add(new WhaisApiTestCases("hash_array",
                      "Test with NULL arrays.",
                      "Test for various corner cases"));
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
