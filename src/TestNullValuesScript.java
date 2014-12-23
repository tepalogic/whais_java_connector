import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.Connection;
import net.whais.Client.TableFieldType;
import net.whais.Client.TableValue;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;


public class TestNullValuesScript
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

        boolean testResult = test.executeTestNull_1 (c);
        testResult &= test.executeTestNullGeneric (c, "null_test_1_1");
        testResult &= test.executeTestNull_1_2 (c);
        testResult &= test.executeTestNullGeneric (c, "null_test_1_3");
        testResult &= test.executeTestNullGeneric (c, "null_test_1_4");
        testResult &= test.executeTestNullGeneric (c, "null_test_1_5");
        testResult &= test.executeTestNull_2 (c);
        testResult &= test.executeTestNullGeneric (c, "null_test_2_1");
        testResult &= test.executeTestNull_2_2 (c);
        testResult &= test.executeTestNullGeneric (c, "null_test_2_3");
        testResult &= test.executeTestNullGeneric (c, "null_test_3");
        testResult &= test.executeTestNullGeneric (c, "null_test_3_1");
        testResult &= test.executeTestNullGeneric (c, "null_test_4");
        testResult &= test.executeTestNullGeneric (c, "null_test_4_1");

        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }

    boolean executeTestNull_1 (Connection c) throws IOException {
        System.out.print ("Executing test procedure 'null_test_1' ... ");

        TableFieldType[] fields = new TableFieldType[16];

        fields[0] = new TableFieldType ("field1",ValueType.boolType());
        fields[1] = new TableFieldType ("field2",ValueType.charType());
        fields[2] = new TableFieldType ("field3",ValueType.dateType());
        fields[3] = new TableFieldType ("field4",ValueType.datetimeType());
        fields[4] = new TableFieldType ("field5",ValueType.hirestimeType());
        fields[5] = new TableFieldType ("field6",ValueType.int8Type());
        fields[6] = new TableFieldType ("field7",ValueType.int16Type());
        fields[7] = new TableFieldType ("field8",ValueType.int32Type());
        fields[8] = new TableFieldType ("field9",ValueType.int64Type());
        fields[9] = new TableFieldType ("field10",ValueType.uint8Type());
        fields[10] = new TableFieldType ("field11",ValueType.uint16Type());
        fields[11] = new TableFieldType ("field12",ValueType.uint32Type());
        fields[12] = new TableFieldType ("field13",ValueType.uint64Type());
        fields[13] = new TableFieldType ("field14",ValueType.realType());
        fields[14] = new TableFieldType ("field15",ValueType.richrealType());
        fields[15] = new TableFieldType ("field16",ValueType.textType());
        TableValue expectedResult = Value.createTable(fields);

        expectedResult.addRows( 1);
        expectedResult.put( Value.createBasic (ValueType.boolType(), "1"), "field1", 0);
        expectedResult.put( Value.createBasic (ValueType.charType(), "C"), "field2", 0);
        expectedResult.put( Value.createBasic (ValueType.dateType(), "1800/10/14"), "field3", 0);
        expectedResult.put( Value.createBasic (ValueType.datetimeType(), "-1/10/14 10:31:12"), "field4", 0);
        expectedResult.put( Value.createBasic (ValueType.hirestimeType(), "1/10/14 10:31:12.111222"), "field5", 0);
        expectedResult.put( Value.createBasic (ValueType.int8Type(), "-8"), "field6", 0);
        expectedResult.put( Value.createBasic (ValueType.int16Type(), "-16"), "field7", 0);
        expectedResult.put( Value.createBasic (ValueType.int32Type(), "-32"), "field8", 0);
        expectedResult.put( Value.createBasic (ValueType.int64Type(), "-64"), "field9", 0);
        expectedResult.put( Value.createBasic (ValueType.uint8Type(), "8"), "field10", 0);
        expectedResult.put( Value.createBasic (ValueType.uint16Type(), "16"), "field11", 0);
        expectedResult.put( Value.createBasic (ValueType.uint32Type(), "32"), "field12", 0);
        expectedResult.put( Value.createBasic (ValueType.uint64Type(), "64"), "field13", 0);
        expectedResult.put( Value.createBasic (ValueType.realType(), "-1.1"), "field14", 0);
        expectedResult.put( Value.createBasic (ValueType.richrealType(), "2.12"), "field15", 0);
        expectedResult.put( Value.createBasic (ValueType.textType(), "This should be a text"), "field16", 0);

        c.executeProcedure ( "null_test_1");
        Value result = c.retrieveStackTop ();
        if ( ! result.equals(expectedResult)) {
            System.out.println ("FAIL: The expected result does not match");
            return false;
        }

        c.popStackValues( Connection.ALL);
        c.executeProcedure ( "null_test_1");
        result = c.retrieveStackTop ();
        if ( ! expectedResult.equals(result)) {
            System.out.println ("FAIL: At the second execution, the expected result does not match.");
            return false;
        }

        System.out.println (" OK");
        return true;
    }

    boolean executeTestNullGeneric (Connection c, String test) throws IOException {

        System.out.print ("Executing test procedure '" + test + "' ... ");
        Value expectedResult = Value.createBasic (ValueType.boolType(), "1");

        c.executeProcedure ( test);
        Value result = c.retrieveStackTop ();
        if ( ! result.equals(expectedResult)) {
            System.out.println ("FAIL: The expected result does not match");
            return false;
        }

        c.popStackValues( Connection.ALL);
        c.executeProcedure ( test);
        result = c.retrieveStackTop ();
        if ( ! expectedResult.equals(result)) {
            System.out.println ("FAIL: At the second execution, the expected result does not match.");
            return false;
        }

        System.out.println (" OK");
        return true;
    }

    boolean executeTestNull_1_2 (Connection c) throws IOException {
        System.out.print ("Executing test procedure 'null_test_1_2' ... ");

        TableFieldType[] fields = new TableFieldType[16];

        fields[0] = new TableFieldType ("field1",ValueType.arrayBoolType());
        fields[1] = new TableFieldType ("field2",ValueType.arrayCharType());
        fields[2] = new TableFieldType ("field3",ValueType.arrayDateType());
        fields[3] = new TableFieldType ("field4",ValueType.arrayDatetimeType());
        fields[4] = new TableFieldType ("field5",ValueType.arrayHirestimeType());
        fields[5] = new TableFieldType ("field6",ValueType.arrayInt8Type());
        fields[6] = new TableFieldType ("field7",ValueType.arrayInt16Type());
        fields[7] = new TableFieldType ("field8",ValueType.arrayInt32Type());
        fields[8] = new TableFieldType ("field9",ValueType.arrayInt64Type());
        fields[9] = new TableFieldType ("field10",ValueType.arrayUInt8Type());
        fields[10] = new TableFieldType ("field11",ValueType.arrayUInt16Type());
        fields[11] = new TableFieldType ("field12",ValueType.arrayUInt32Type());
        fields[12] = new TableFieldType ("field13",ValueType.arrayUInt64Type());
        fields[13] = new TableFieldType ("field14",ValueType.arrayRealType());
        fields[14] = new TableFieldType ("field15",ValueType.arrayRichrealType());
        fields[15] = new TableFieldType ("field16",ValueType.textType());
        TableValue expectedResult = Value.createTable(fields);

        expectedResult.addRows( 1);
        expectedResult.put( Value.createArray (ValueType.boolType(), "1", "0"), "field1", 0);
        expectedResult.put( Value.createArray (ValueType.charType(), "C", "D"), "field2", 0);
        expectedResult.put( Value.createArray (ValueType.dateType(), "1800/10/14", "1801/11/13"), "field3", 0);
        expectedResult.put( Value.createArray (ValueType.datetimeType(), "-1/10/14 10:31:12"), "field4", 0);
        expectedResult.put( Value.createArray (ValueType.hirestimeType(), "1/10/14 10:31:12.111222"), "field5", 0);
        expectedResult.put( Value.createArray (ValueType.int8Type(), "-8"), "field6", 0);
        expectedResult.put( Value.createArray (ValueType.int16Type(), "-16"), "field7", 0);
        expectedResult.put( Value.createArray (ValueType.int32Type(), "-32"), "field8", 0);
        expectedResult.put( Value.createArray (ValueType.int64Type(), "-64"), "field9", 0);
        expectedResult.put( Value.createArray (ValueType.uint8Type(), "8"), "field10", 0);
        expectedResult.put( Value.createArray (ValueType.uint16Type(), "16"), "field11", 0);
        expectedResult.put( Value.createArray (ValueType.uint32Type(), "32"), "field12", 0);
        expectedResult.put( Value.createArray (ValueType.uint64Type(), "64"), "field13", 0);
        expectedResult.put( Value.createArray (ValueType.realType(), "-1.1"), "field14", 0);
        expectedResult.put( Value.createArray (ValueType.richrealType(), "2.12"), "field15", 0);
        expectedResult.put( Value.createBasic (ValueType.textType(), "Text field"), "field16", 0);

        c.executeProcedure ("null_test_1_2");
        Value result = c.retrieveStackTop ();
        if ( ! result.equals(expectedResult)) {
            System.out.println ("FAIL: The expected result does not match");
            return false;
        }

        c.popStackValues( Connection.ALL);
        c.executeProcedure ( "null_test_1_2");
        result = c.retrieveStackTop ();
        if ( ! expectedResult.equals(result)) {
            System.out.println ("FAIL: At the second execution, the expected result does not match.");
            return false;
        }

        System.out.println (" OK");
        return true;
    }

    boolean executeTestNull_2 (Connection c) throws IOException {

        System.out.print ("Executing test procedure 'null_test_2' ... ");
        Value expectedResult = Value.createArray (ValueType.int32Type (), "-1000", "20");

        c.executeProcedure ( "null_test_2");
        Value result = c.retrieveStackTop ();
        if ( ! result.equals(expectedResult)) {
            System.out.println ("FAIL: The expected result does not match");
            return false;
        }

        c.popStackValues( Connection.ALL);
        c.executeProcedure ( "null_test_2");
        result = c.retrieveStackTop ();
        if ( ! expectedResult.equals(result)) {
            System.out.println ("FAIL: At the second execution, the expected result does not match.");
            return false;
        }

        System.out.println (" OK");
        return true;
    }

    boolean executeTestNull_2_2 (Connection c) throws IOException {

        System.out.print ("Executing test procedure 'null_test_2_2' ... ");
        Value expectedResult = Value.createArray (ValueType.int8Type (), "-10");

        c.executeProcedure ( "null_test_2_2");
        Value result = c.retrieveStackTop ();
        if ( ! result.equals(expectedResult)) {
            System.out.println ("FAIL: The expected result does not match");
            return false;
        }

        c.popStackValues( Connection.ALL);
        c.executeProcedure ( "null_test_2_2");
        result = c.retrieveStackTop ();
        if ( ! expectedResult.equals(result)) {
            System.out.println ("FAIL: At the second execution, the expected result does not match.");
            return false;
        }

        System.out.println (" OK");
        return true;
    }

}
