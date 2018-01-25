import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.whais.Client.Connection;
import net.whais.Client.Value;
import test.net.whais.Client.CommnandLine;

public class TestWhaisTable implements WhaisAPITester
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

        TestWhaisTable t = new TestWhaisTable();

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

    public TestWhaisTable()
    {
        testCases.add(new WhaisApiTestCases("add_row",
                "Test with a NULL expresion for table argument.",
                "Test with a global non-persisten table (verify result).",
                "Test with a global non-persisten table (verify result 2).",
                "Test with a local table (verify result).",
                "Test with a local table (verify result 2).",
                "Test with a global undefined table.",
                "Test with a local undefined table."));


        testCases.add(new WhaisApiTestCases("is_persistent",
                "Test with a NULL expresion for table argument.",
                "Test with a global persistent table (no rows allocated).",
                "Test with a global persistent table (with allocated rows).",
                "Test with a global non-persistent table (no rows allocated).",
                "Test with a global non-persistent table (with allocated rows).",
                "Test with a local table (no rows allocated).",
                "Test with a local table (with allocated rows).",
                "Test with a global undefined table.",
                "Test with a local undefined table."));


        testCases.add(new WhaisApiTestCases("empty_row",
                      "Test with a NULL expresion for table argument.",
                      "Test with an undefined table.",
                      "Test with a missing row (table with no allocated rows)",
                      "Test with a missing row (on copied table refference)",
                      "Test on a raw that was allocated (only one row allocated).",
                      "Test on a row that was allocated (only one row allocated and via refference).",
                      "Test on empty global table (defined).",
                      "Test on a global table and a valid row.",
                      "Test on a global table a NULL row value.",
                      "Test on a global table a huge row value."));

        testCases.add(new WhaisApiTestCases("get_empty",
                      "Test a NULL expression table.",
                      "Test with a undefined table.",
                      "Test with a undefined global table",
                      "Test with a local table (through reference)",
                      "Test with a local table for an expected value (1)",
                      "Test with a non persistent global value for an expcted value (< 1 and 0)"));


        testCases.add(new WhaisApiTestCases("count_fields",
                "Test with a NULL expression table.",
                "Test with an undefined local table,",
                "Test with an undefined global table.",
                "Test with a local table",
                "Test with a global non persistent table",
                "Test with a global persisten table"));

        testCases.add(new WhaisApiTestCases("get_fieldth",
                "Test with a NULL expression table.",
                "Test with an undefined local table.",
                "Test with an undefined global table.",
                "Test with a local table and a valid index.",
                "Test with a local table but invalid index.",
                "Test with a global table and valid index.",
                "Test with a global table and NULL index."));

        testCases.add(new WhaisApiTestCases("get_field",
                "Test with a NULL expression table.",
                "Test with a undefined local table.",
                "Test with a local table and NULL index value.",
                "Test with a global non-persistent table and a invalid non-null field."));

        testCases.add(new WhaisApiTestCases("count_rows",
                "Test with a NULL expression table.",
                "Test with a undefined local table.",
                "Test with a undefined global table.",
                "Test with a local table and default value for empties (expected value).",
                "Test with a global non-persistent table and all rows selected.",
                "Test local tablen and both empties values (expected values for both)"));

        testCases.add(new WhaisApiTestCases("exchg_rows",
                "Test with a NULL expression table.",
                "Test with a undefined global table.",
                "Test with a local table, valid first row, invalid second row)",
                "Test with a local table, negative first row, valid second row)",
                "Test with a local table (by reference) and valid rows",
                "Test with a local table, valid fist row, NULL second row",
                "Test wirh a local table, NULL fist row,  valid second row"));

        testCases.add(new WhaisApiTestCases("sort_table",
                "Test with a NULL expression table.",
                "Test with a undefined global table.",
                "Test with local table and a invalid column element.",
                "Test with local table and more directions than columns.",
                "Test with local table and negative start row",
                "Test with local table and huge value for last row",
                "Test with local table and valid args and rows (smaller interval)",
                "Test with local table and valid args and rows (other small interval, and other cols)"));
    }


    boolean executeTestIsPersisten(Connection c) throws IOException {
        System.out.print("Check test is_persistent with NULL expression parameter ... ");
        Value result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(0));
        Value expected = Value.createBool();
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        System.out.print("Check test is_persistent with NULL global table parameter (persistent 1)... ");
        result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(1));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the value retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        System.out.print("Check test is_persistent with non-NULL global table parameter (persistent 2) ... ");
        result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(2));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the value retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");


        System.out.print("Check test is_persistent with NULL global table parameter (non - persistent 1)... ");
        result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(3));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the value retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        System.out.print("Check test is_persistent with non-NULL global table parameter (persistent 2) ... ");
        result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(4));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the value retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");


        System.out.print("Check test is_persistent with NULL local table parameter ... ");
        result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(3));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the value retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        System.out.print("Check test is_persistent with non-NULL local table parameter ... ");
        result = c.callProcedure("test_whais_api_is_persistent", Value.createUInt8(4));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        return true;
    }


    boolean executeTestAddRow(Connection c) throws IOException {
        System.out.print("Check add_row with NULL expression parameter ... ");
        Value result = c.callProcedure("test_whais_api_add_row", Value.createUInt8(0));
        Value expected = Value.createBool();
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");


        System.out.print("Check add_row with global table ... ");
        result = c.callProcedure("test_whais_api_add_row", Value.createUInt8(1));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        System.out.print("Check add_row with global table 2 ... ");
        result = c.callProcedure("test_whais_api_add_row", Value.createUInt8(2));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");


        System.out.print("Check add_row with NULL local table ... ");
        result = c.callProcedure("test_whais_api_add_row", Value.createUInt8(3));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");


        System.out.print("Check add_row with NULL local table 2 ... ");
        result = c.callProcedure("test_whais_api_add_row", Value.createUInt8(4));
        expected = Value.createBool(true);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result + " rather than expected " + expected + ")");
            return false;
        }
        else
            System.out.println("OK");

        return true;
    }

    public List<WhaisApiTestCases> GetTests()
    {
        return testCases;
    }

    private List<WhaisApiTestCases> testCases = new ArrayList<>();

    @Override
    public boolean DoTest( Connection c) throws IOException
    {
        return true;
    }
}
