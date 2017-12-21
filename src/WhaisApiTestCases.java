import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.whais.Client.Connection;
import net.whais.Client.Value;

class WhaisApiTestCases
{

    public WhaisApiTestCases( String functionName, String... testDescriptions)
    {
        this.testFunctionName = functionName;
        testCasesDescriptions = new String[testDescriptions.length];

        for (int i = 0; i < testDescriptions.length; ++i)
            testCasesDescriptions[i] = testDescriptions[i];

    }

    public boolean DoTests( final Connection c)
    {
        boolean result = true;

        for (int i = 0; i < testCasesDescriptions.length; ++i) {
            try {
                System.out.print("Test " + testFunctionName + " [" + i + "]: " + testCasesDescriptions[i] + " ... ");
                final Value testResult = c.callProcedure( "test_whais_api_" + testFunctionName, Value.createUInt32( i));
                if (testResult.equals( Value.createBool())) {
                    result &= false;
                    System.out.println( "FAIL (got a bool NULL value)");
                } else if (testResult.equals( Value.createBool( false))) {
                    result &= false;
                    System.out.println( "FAIL");
                } else if (testResult.equals( Value.createBool( true))) {
                    result &= true;
                    System.out.println( "PASS");
                } else {
                    result &= false;
                    System.out.println( "FAIL (got a non-bool value)");
                }

            } catch (IOException e) {
                System.out.println(
                        "\nSerious sistem failure while executing test case " + i + " for " + testFunctionName);
                System.out.println( "Exception: " + e + ": " + e.getMessage());
                result &= false;
            }
        }

        try {
            for (WhaisAPITester tester : extraTests)
                result = tester.DoTest( c);
        } catch (IOException e) {
            System.out.println( "\nSerious sistem failure while executing custom test cases.");
            System.out.println( "Exception: " + e + ": " + e.getMessage());
            result &= false;
        }

    return result;
    }

    public void AddTester( final WhaisAPITester tester)
    {
        extraTests.add( tester);
    }

    private String testFunctionName = "";
    private String[] testCasesDescriptions = new String[0];
    private List<WhaisAPITester> extraTests = new ArrayList<>();
}