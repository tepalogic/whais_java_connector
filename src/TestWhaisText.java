import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.ArrayValue;
import net.whais.Client.Connection;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;

public class TestWhaisText 
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
        
        TestWhaisText t = new TestWhaisText();
        testResult &= t.executeTestIsUpper(c);
        testResult &= t.executeTestIsLower(c);
        testResult &= t.executeTestIsDigit(c);
        testResult &= t.executeTestIsAlpha(c);
        testResult &= t.executeTestIsSpace(c);
        testResult &= t.executeTestIsPunct(c);
        testResult &= t.executeTestUpper(c);
        testResult &= t.executeTestLower(c);
        testResult &= t.executeTestUpperAll(c);
        testResult &= t.executeTestLowerAll(c);
        testResult &= t.executeTestTextToUtf8(c);
        testResult &= t.executeTestTextFromUtf8(c);
        testResult &= t.executeTestTextToUtf16(c);
        testResult &= t.executeTestTextFromUtf16(c);
        testResult &= t.executeTestTextToUtf32(c);
        testResult &= t.executeTestTextFromUtf32(c);
        testResult &= t.executeTestTextLength(c);
        testResult &= t.executeTestTextFindChar(c);
        testResult &= t.executeTestTextFindSubstring(c);
        testResult &= t.executeTestTextReplaceSubstring(c);
        testResult &= t.executeTestTextCompare(c);
        testResult &= t.executeTestTextHash(c);

        
        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }
    
    boolean executeTestIsUpper(Connection c) throws IOException {
        System.out.print("Check is_upper with NULL parameter ... ");
        Value result = c.callProcedure( "is_upper", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.BOOL);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_upper with 'e' parameter ... ");
        result = c.callProcedure( "is_upper", Value.createChar("e"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("0"))) {
            System.out.println("FAIL (got " + result + " rather than a false value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_upper with 'E' parameter ... ");
        result = c.callProcedure( "is_upper", Value.createChar("E"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("1"))) {
            System.out.println("FAIL (got " + result + " rather than a true value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestIsLower(Connection c) throws IOException {
        System.out.print("Check is_lower with NULL parameter ... ");
        Value result = c.callProcedure( "is_lower", Value.createChar(null));
        final ValueType exptected = ValueType.create(ValueType.BOOL);
        if (! result.type().equals(exptected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + exptected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_lower with 'f' parameter ... ");
        result = c.callProcedure( "is_lower", Value.createChar("f"));
        if (! result.type().equals(exptected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + exptected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("1"))) {
            System.out.println("FAIL (got " + result + " rather than a true value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_lower with 'X' parameter ... ");
        result = c.callProcedure( "is_lower", Value.createChar("X"));
        if (! result.type().equals(exptected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + exptected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("0"))) {
            System.out.println("FAIL (got " + result + " rather than a false value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestIsDigit(Connection c) throws IOException {
        System.out.print("Check is_digit with NULL parameter ... ");
        Value result = c.callProcedure( "is_digit", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.BOOL);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_digit with '1' parameter ... ");
        result = c.callProcedure( "is_digit", Value.createChar("1"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("1"))) {
            System.out.println("FAIL (got " + result + " rather than a true value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_digit with 'X' parameter ... ");
        result = c.callProcedure( "is_digit", Value.createChar("X"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("0"))) {
            System.out.println("FAIL (got " + result + " rather than a false value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    
    boolean executeTestIsAlpha(Connection c) throws IOException {
        System.out.print("Check is_alpha with NULL parameter ... ");
        Value result = c.callProcedure( "is_alpha", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.BOOL);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_alpha with 'y' parameter ... ");
        result = c.callProcedure( "is_alpha", Value.createChar("y"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("1"))) {
            System.out.println("FAIL (got " + result + " rather than a true value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_alpha with '0' parameter ... ");
        result = c.callProcedure( "is_alpha", Value.createChar("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("0"))) {
            System.out.println("FAIL (got " + result + " rather than a false value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestIsSpace(Connection c) throws IOException {
        System.out.print("Check is_space with NULL parameter ... ");
        Value result = c.callProcedure( "is_space", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.BOOL);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_space with '\\t' parameter ... ");
        result = c.callProcedure( "is_space", Value.createChar("\t"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("1"))) {
            System.out.println("FAIL (got " + result + " rather than a true value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_space with '0' parameter ... ");
        result = c.callProcedure( "is_space", Value.createChar("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("0"))) {
            System.out.println("FAIL (got " + result + " rather than a false value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestIsPunct(Connection c) throws IOException {
        System.out.print("Check is_punct with NULL parameter ... ");
        Value result = c.callProcedure( "is_punct", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.BOOL);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_punct with '!' parameter ... ");
        result = c.callProcedure( "is_punct", Value.createChar("!"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("1"))) {
            System.out.println("FAIL (got " + result + " rather than a true value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check is_punct with '0' parameter ... ");
        result = c.callProcedure( "is_punct", Value.createChar("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals( Value.createBool("0"))) {
            System.out.println("FAIL (got " + result + " rather than a false value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestUpper(Connection c) throws IOException {
        System.out.print("Check upper with NULL parameter ... ");
        Value result = c.callProcedure( "upper", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.CHAR);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check upper with 'a' parameter ... ");
        result = c.callProcedure( "upper", Value.createChar("a"));
        if (! result.equals( Value.createChar("A"))) {
            System.out.println("FAIL (got " + result + " rather than A value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check upper with 'B' parameter ... ");
        result = c.callProcedure( "upper", Value.createChar("B"));
        if ( ! result.equals( Value.createChar("B"))) {
            System.out.println("FAIL (got " + result + " rather than B value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestLower(Connection c) throws IOException {
        System.out.print("Check lower with NULL parameter ... ");
        Value result = c.callProcedure( "lower", Value.createChar(null));
        final ValueType expected = ValueType.create(ValueType.CHAR);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check lower with 'a' parameter ... ");
        result = c.callProcedure( "lower", Value.createChar("a"));
        if ( ! result.equals( Value.createChar("a"))) {
            System.out.println("FAIL (got " + result + " rather than 'a' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check lower with 'B' parameter ... ");
        result = c.callProcedure( "lower", Value.createChar("B"));
        
        if ( ! result.equals( Value.createChar("b"))) {
            System.out.println("FAIL (got " + result + " rather than 'b; value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestUpperAll(Connection c) throws IOException {
        System.out.print("Check upper_all with NULL parameter ... ");
        Value result = c.callProcedure( "upper_all", Value.createText(null));
        final ValueType expected = ValueType.create(ValueType.TEXT);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check upper_all with 'aAbBcCdD' parameter ... ");
        result = c.callProcedure( "upper_all", Value.createText("aAbBcCdD"));
        if ( ! result.equals( Value.createText("AABBCCDD"))) {
            System.out.println("FAIL (got " + result + " rather than AABBCCDD value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check upper_all with 'ABCD' parameter ... ");
        result = c.callProcedure( "upper_all", Value.createText("ABCD"));
        if ( ! result.equals( Value.createText("ABCD"))) {
            System.out.println("FAIL (got " + result + " rather than ABCD value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check upper_all with 'abcd' parameter ... ");
        result = c.callProcedure( "upper_all", Value.createText("abcd"));
        if ( ! result.equals( Value.createText("ABCD"))) {
            System.out.println("FAIL (got " + result + " rather than ABCD value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestLowerAll(Connection c) throws IOException {
        System.out.print("Check lower_all with NULL parameter ... ");
        Value result = c.callProcedure( "lower_all", Value.createText(null));
        final ValueType expected = ValueType.create(ValueType.TEXT);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check lower_all with 'aAbBcCdD' parameter ... ");
        result =c.callProcedure( "lower_all", Value.createText("aAbBcCdD"));
        if (! result.equals( Value.createText("aabbccdd"))) {
            System.out.println("FAIL (got " + result + " rather than aabbccdd null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check lower_all with 'ABCD' parameter ... ");
        result = c.callProcedure( "lower_all", Value.createText("ABCD"));
        if ( ! result.equals( Value.createText("abcd"))) {
            System.out.println("FAIL (got " + result + " rather than abcd null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check lower_all with 'abcd' parameter ... ");
        result = c.callProcedure( "lower_all", Value.createText("abcd"));
        if ( ! result.equals( Value.createText("abcd"))) {
            System.out.println("FAIL (got " + result + " rather than abcd null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }

    boolean executeTestTextToUtf8 (Connection c) throws IOException {
        System.out.print("Check text_to_utf8 with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.ARRAY_MASK | ValueType.UINT8);
        
        Value result = c.callProcedure( "text_to_utf8", Value.createText(null));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        System.out.print("Check text_to_utf8 with special string parameter ... ");
        
        result = c.callProcedure( "text_to_utf8", Value.createText(testString));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + " rather than expected." + expected + ")");
            return false;
        }
        else if (result.isNull()) {
            System.out.println("FAIL (got a null value.");
            return false;
        }
        else 
        {  
            ArrayValue r = (ArrayValue)result;
            byte[] chs = testString.getBytes("UTF8");
            if (r.size() != chs.length) {
                System.out.println("FAIL: array size does not match (got " + r.size() + " instead of " + chs.length + "bytes");
                return false;
            }
            for (int i = 0 ; i < chs.length ; ++i) {
                int codeUnit = chs[i];
                codeUnit &= 0xFF;
                if (codeUnit != Integer.parseInt(r.get(i).toString()))  {
                    System.out.println("FAIL: at " + i + " got " + r.get(i) +" instead of " + codeUnit);
                    return false;
                }
            }
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestTextToUtf16 (Connection c) throws IOException {
        System.out.print("Check text_to_utf16 with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.ARRAY_MASK | ValueType.UINT16);
        
        Value result = c.callProcedure( "text_to_utf16", Value.createText(null));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        System.out.print("Check text_to_utf16 with special string parameter ... ");
        
        result = c.callProcedure( "text_to_utf16", Value.createText(testString));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + " rather than expected." + expected + ")");
            return false;
        }
        else if (result.isNull()) {
            System.out.println("FAIL (got a null value.");
            return false;
        }
        else 
        {  
            ArrayValue r = (ArrayValue)result;
            char[] chs = testString.toCharArray();
            if (r.size() != chs.length) {
                System.out.println("FAIL: array size does not match (got " + r.size() + " instead of " + chs.length + "bytes");
                return false;
            }
            for (int i = 0 ; i < chs.length ; ++i) {
                int codeUnit = chs[i];
                codeUnit &= 0xFFFF;            
                if (codeUnit != Integer.parseInt(r.get(i).toString()))  {
                    System.out.println("FAIL: at " + i + " got " + r.get(i) +" instead of " + codeUnit);
                    return false;
                }
            }
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestTextFromUtf8 (Connection c) throws IOException {
        System.out.print("Check text_from_utf8 with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.TEXT);
        
        ArrayValue array = Value.createArray(ValueType.uint8Type());
        Value result = c.callProcedure( "text_from_utf8", array);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        System.out.print("Check text_from_utf8 with special string parameter ... ");
        byte[] chs = testString.getBytes("UTF8");
        
        for (byte b : chs) {
            int codeUnit = b;
            codeUnit &= 0xFF;
            array.add(Value.createUInt8(codeUnit));
        }
        
        result = c.callProcedure( "text_from_utf8", array);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if (result.isNull()) {
            System.out.println("FAIL (got a null value.");
            return false;
        }
        else if (testString.compareTo(result.toString()) != 0)
        {  
            System.out.println("FAIL (got a '" + result + "'");
            return false;
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestTextFromUtf16 (Connection c) throws IOException {
        System.out.print("Check text_from_utf16 with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.TEXT);
        
        ArrayValue array = Value.createArray(ValueType.uint16Type());
        Value result = c.callProcedure( "text_from_utf16", array);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        System.out.print("Check text_from_utf16 with special string parameter ... ");
        char[] chs = testString.toCharArray();
        
        for (char ch : chs) {
            int codeUnit = ch;
            codeUnit &= 0xFFFF;
            array.add(Value.createUInt16("" + codeUnit));
        }
        
        result = c.callProcedure("text_from_utf16", array);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if (result.isNull()) {
            System.out.println("FAIL (got a null value.");
            return false;
        }
        else if (testString.compareTo(result.toString()) != 0)
        {  
            System.out.println("FAIL (got a '" + result + "'");
            return false;
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestTextToUtf32 (Connection c) throws IOException {
        System.out.print("Check text_to_array with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.ARRAY_MASK | ValueType.CHAR);
        
        Value result = c.callProcedure( "text_to_array", Value.createText(null));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        System.out.print("Check text_to_array with special string parameter ... ");
        
        result = c.callProcedure( "text_to_array", Value.createText(testString));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if (result.isNull()) {
            System.out.println("FAIL (got a null value.");
            return false;
        }
        else 
        {  
            ArrayValue r = (ArrayValue)result;
            int[] codePoints = new int[r.size()];
            for (int i = 0; i < codePoints.length; ++i) {
                codePoints[i] = Integer.parseInt(c.callProcedure("unicode_cp", r.get(i)).toString());
            }
            final String testR = new String(codePoints, 0, codePoints.length);
            if (testR.compareTo(testString) != 0) {
                System.out.println("FAIL: I did not got an exact copy of the test string (' " + r + "')!");
                return false;
            }
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestTextFromUtf32 (Connection c) throws IOException {
        System.out.print("Check text_from_array with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.TEXT);
        
        ArrayValue array = Value.createArray(ValueType.charType());
        Value result = c.callProcedure( "text_from_array", array);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a null value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        System.out.print("Check text_from_array with special string parameter ... ");
        
        for (int i = 0; i < testString.length();) {
           final int codePoint = testString.codePointAt(i);
           final String s = String.valueOf(Character.toChars( codePoint));
           array.add(Value.createChar(s));
           i += Character.charCount(codePoint);
        }
        
        result = c.callProcedure("text_from_array", array);
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if (result.isNull()) {
            System.out.println("FAIL (got a null value.");
            return false;
        }
        else if (testString.compareTo(result.toString()) != 0)
        {  
            System.out.println("FAIL (got a '" + result + "'");
            return false;
        }
        
        System.out.println("OK");
        return true;
    }
    
    boolean executeTestTextLength(Connection c) throws IOException {
        System.out.print("Check length with NULL parameter ... ");
        final ValueType expected = ValueType.create(ValueType.UINT64);

        Value result = c.callProcedure( "length", Value.createText(null));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + " rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(Value.createUInt64("0"))) {
            System.out.println("FAIL (got " + result + " rather than '0' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check length with 'π' parameter ... ");
        result = c.callProcedure( "length", Value.createText("π"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(Value.createUInt64("1"))) {
            System.out.println("FAIL (got " + result + " rather than '1' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check length with 'This is π greek' parameter ... ");
        result = c.callProcedure( "length", Value.createText("This is π!"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(Value.createUInt64("10"))) {
            System.out.println("FAIL (got " + result + " rather than '10' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestTextFindChar(Connection c) throws IOException {
        System.out.print("Check find_char with NULL string parameter ... ");
        final ValueType expected = ValueType.create(ValueType.UINT64);

        Value result = c.callProcedure( "find_char", Value.createText(null), 
                                                     Value.createChar("a"),
                                                     Value.createBool(),
                                                     Value.createUInt64(),
                                                     Value.createUInt64());
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than 'null' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check find_char with NULL char parameter ... ");
        result = c.callProcedure( "find_char", Value.createText("Some string"), 
                                      Value.createChar(),
                                      Value.createBool(),
                                      Value.createUInt64(),
                                      Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than 'null' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check find_char with char parameters not in interval ... ");
        result = c.callProcedure( "find_char", Value.createText("Some string"), 
                                      Value.createChar('s'),
                                      Value.createBool(null),
                                      Value.createUInt64(),
                                      Value.createUInt64(2l));
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than 'null' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check find_char with char parameters not in interval 2 ... ");
        result = c.callProcedure( "find_char", Value.createText("Some string"), 
                                      Value.createChar("s"),
                                      Value.createBool(null),
                                      Value.createUInt64(7),
                                      Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than 'null' value.");
            return false;
        } else
            System.out.println( "OK");


        System.out.print("Check find_char with char parameters in interval ... ");
        result = c.callProcedure( "find_char", Value.createText("Some string"), 
                                      Value.createChar("s"),
                                      Value.createBool("0"),
                                      Value.createUInt64(),
                                      Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createUInt64("5"))) {
            System.out.println( "FAIL (got " + result + " rather than '5' value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print("Check find_char with char (case insesitive) parameters in interval ... ");
        result = c.callProcedure( "find_char", Value.createText("Some string"), 
                                      Value.createChar("s"),
                                      Value.createBool(true),
                                      Value.createUInt64(),
                                      Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createUInt64("0"))) {
            System.out.println( "FAIL (got " + result + " rather than '0' value.");
            return false;
        } else
            System.out.println( "OK");

        return true;
    }
    
    boolean executeTestTextFindSubstring(Connection c) throws IOException {
        System.out.print("Check find with NULL string parameter ... ");
        final ValueType expected = ValueType.create(ValueType.UINT64);

        Value result = c.callProcedure( "find", Value.createText(null), 
                                                Value.createText("AB"),
                                                Value.createBool(false),
                                                Value.createUInt64(),
                                                Value.createUInt64());
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than 'null' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check find with NULL char parameter ... ");
        result = c.callProcedure( "find", Value.createText("Some string"), 
                                 Value.createText(),
                                 Value.createBool(),
                                 Value.createUInt64(),
                                 Value.createUInt64());
        if (!result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than 'null' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check find with str parameters not in interval ... ");
        result =  c.callProcedure( "find", Value.createText("Some string"), 
                                 Value.createText("so"),
                                 Value.createBool(),
                                 Value.createUInt64(),
                                 Value.createUInt64("6"));
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than 'null' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check find with str parameters not in interval 2 ... ");
        result = c.callProcedure( "find", Value.createText("Some string"), 
                                 Value.createText("so"),
                                 Value.createBool(null),
                                 Value.createUInt64("4"),
                                 Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than 'null' value.");
            return false;
        } else
            System.out.println( "OK");


        System.out.print("Check find with parameters in interval ... ");
        result = c.callProcedure( "find", Value.createText("Some string"), 
                                      Value.createText("s"),
                                      Value.createBool("0"),
                                      Value.createUInt64(),
                                      Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createUInt64("5"))) {
            System.out.println( "FAIL (got " + result + " rather than '5' value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print("Check find with char (case insesitive) parameters in interval ... ");
        result = c.callProcedure( "find", Value.createText("Some string"), 
                                 Value.createText("sT"),
                                 Value.createBool("1"),
                                 Value.createUInt64(),
                                 Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createUInt64("5"))) {
            System.out.println( "FAIL (got " + result + " rather than '5' value.");
            return false;
        } else
            System.out.println( "OK");

        return true;
    }
    
    boolean executeTestTextReplaceSubstring(Connection c) throws IOException {
        System.out.print("Check replace with NULL source string parameter ... ");
        final ValueType expected = ValueType.create(ValueType.TEXT);

        Value result = c.callProcedure( "replace", Value.createText(null), 
                                                   Value.createText("AB"),
                                                   Value.createText("CC"),
                                                   Value.createBool(null),
                                                   Value.createUInt64(),
                                                   Value.createUInt64());
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than 'null' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check replace with NULL old str parameter ... ");
        result = c.callProcedure( "replace", Value.createText("Some string"), 
                                    Value.createText(null),
                                    Value.createText("CC"),
                                    Value.createBool(null),
                                    Value.createUInt64(),
                                    Value.createUInt64());
        if (!result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (! result.equals(Value.createText("Some string"))) {
            System.out.println( "FAIL (got " + result + " rather than 'Some string' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check replace with NULL new str parameters not interval ... ");
        result = c.callProcedure( "replace", Value.createText("Some string"), 
                                    Value.createText("s"),
                                    Value.createText(null),
                                    Value.createBool(null),
                                    Value.createUInt64(),
                                    Value.createUInt64(6));
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.equals(Value.createText("Some tring"))) {
            System.out.println( "FAIL (got " + result + " rather than 'Some tring' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check replace with NULL new str parameters not interval 2 ... ");
        result = c.callProcedure( "replace", Value.createText("Some string"), 
                                    Value.createText("s"),
                                    Value.createText("a"),
                                    Value.createBool(null),
                                    Value.createUInt64(),
                                    Value.createUInt64("4"));
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.equals(Value.createText("Some string"))) {
            System.out.println( "FAIL (got " + result + " rather than 'Some string' value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print("Check replace with parameters in interval ... ");
        result = c.callProcedure( "replace", Value.createText("Some string"), 
                                    Value.createText("s"),
                                    Value.createText("nice s"),
                                    Value.createBool("0"),
                                    Value.createUInt64(),
                                    Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createText("Some nice string"))) {
            System.out.println( "FAIL (got " + result + " rather than 'Some nice string' value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print("Check replace (case insesitive) parameters in interval ... ");
        result = c.callProcedure( "replace", Value.createText("Some string"), 
                                    Value.createText("sT"),
                                    Value.createText("nice st"),
                                    Value.createBool("1"),
                                    Value.createUInt64(),
                                    Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createText("Some nice string"))) {
            System.out.println( "FAIL (got " + result + " rather than 'Some nice string' value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check replace with null (case insesitive) parameters in interval ... ");
        result = c.callProcedure( "replace", Value.createText("Some string"), 
                                    Value.createText("soMe "),
                                    Value.createText(null),
                                    Value.createBool("1"),
                                    Value.createUInt64(),
                                    Value.createUInt64());
        if (!result.type().equals( expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if ( ! result.equals(Value.createText("string"))) {
            System.out.println( "FAIL (got " + result + " rather than 'string' value.");
            return false;
        } else
            System.out.println( "OK");

        return true;
    }
    
    
    boolean executeTestTextCompare(Connection c) throws IOException {
        System.out.print("Check compare with first string NULL ... ");
        final ValueType expected = ValueType.create(ValueType.INT8);
        final Value less = Value.createInt8("-1");
        final Value greater = Value.createInt8("1");
        final Value same = Value.createInt8("0");

        Value result = c.callProcedure( "compare", Value.createText(null), 
                                                   Value.createText("AB"),
                                                   Value.createBool(null),
                                                   Value.createBool("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(less)) {
            System.out.println("FAIL (got " + result + " rather than " + less);
            return false;
        }
        else 
            System.out.println("OK");

        System.out.print("Check compare with second string NULL ... ");
        
        result = c.callProcedure( "compare", Value.createText("AB"), 
                                    Value.createText(null),
                                    Value.createBool(null),
                                    Value.createBool("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(greater)) {
            System.out.println("FAIL (got " + result + " rather than " + greater);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings NULL ... ");
        
        result = c.callProcedure( "compare", Value.createText(null), 
                                    Value.createText(null),
                                    Value.createBool(null),
                                    Value.createBool("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(same)) {
            System.out.println("FAIL (got " + result + " rather than " + same);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL ... ");
        
        result = c.callProcedure( "compare", Value.createText("aA"), 
                                    Value.createText("DD"),
                                    Value.createBool(null),
                                    Value.createBool("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(greater)) {
            /* a is smaller than 'B' in UNICODE */
            System.out.println("FAIL (got " + result + " rather than " + greater);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (2)... ");
        
        result = c.callProcedure( "compare", Value.createText("AA"), 
                                    Value.createText("Dd"),
                                    Value.createBool(null),
                                    Value.createBool("0"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(less)) {
            /* B is smaller than 'a' in UNICODE */
            System.out.println("FAIL (got " + result + " rather than " + less);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (case ignore)... ");
        
        result = c.callProcedure( "compare", Value.createText("aA"), 
                                    Value.createText("Dd"),
                                    Value.createBool(null),
                                    Value.createBool("1"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(less)) {
            /* B is smaller than 'a' in UNICODE */
            System.out.println("FAIL (got " + result + " rather than " + less);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (case ignore 2)... ");
        
        result = c.callProcedure( "compare", Value.createText("BF"), 
                                    Value.createText("bb"),
                                    Value.createBool(null),
                                    Value.createBool("1"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(greater)) {
            /* B is smaller than 'a' in UNICODE */
            System.out.println("FAIL (got " + result + " rather than " + greater);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (case ignore 3)... ");
        
        result = c.callProcedure( "compare", Value.createText("a"), 
                                    Value.createText("A"),
                                    Value.createBool(null),
                                    Value.createBool("1"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(same)) {
            System.out.println("FAIL (got " + result + " rather than " + same);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (alph)... ");
        
        result = c.callProcedure( "compare", Value.createText("a"), 
                                    Value.createText("A"),
                                    Value.createBool(true),
                                    Value.createBool());
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(less)) {
            System.out.println("FAIL (got " + result + " rather than " + less);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (alph 2)... ");
        
        result = c.callProcedure( "compare", Value.createText("a"), 
                                    Value.createText("B"),
                                    Value.createBool(null),
                                    Value.createBool("1"));
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(less)) {
            /* B is smaller than 'a' in UNICODE */
            System.out.println("FAIL (got " + result + " rather than " + less);
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check compare with both strings non-NULL (alph + ignore case)... ");
        result = c.callProcedure( "compare", Value.createText("a"), 
                                    Value.createText("B"),
                                    Value.createBool("1"),
                                    Value.createBool("1"));
        if (!result.type().equals( expected)) {
            System.out.println(
                    "FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.equals( less)) {
            /* B is smaller than 'a' in UNICODE */
            System.out.println( "FAIL (got " + result + " rather than " + less);
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print("Check compare with both strings non-NULL (alph + ignore case 2)... ");
        result = c.callProcedure( "compare", Value.createText("b"), 
                                    Value.createText("B"),
                                    Value.createBool("1"),
                                    Value.createBool("1"));
        if (!result.type().equals( expected)) {
            System.out.println(
                    "FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.equals(same)) {
            /* B is smaller than 'a' in UNICODE */
            System.out.println( "FAIL (got " + result + " rather than " + same);
            return false;
        } else
            System.out.println( "OK");
        
        return true;
    }
    
    
    boolean executeTestLength(Connection c) throws IOException {
        System.out.print("Check count_chars with NULL parameter ... ");
        Value result = c.callProcedure( "count_chars", Value.createText(null));
        final ValueType expected = ValueType.create(ValueType.UINT64);
        final Value zero = Value.createInt64("0");
        final String testString = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        final Value testSize = Value.createInt64(new Integer(testString.length() + 1).toString());

        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + " rather than expected " + expected + ")");
            return false;
        }
        else if ( ! result.equals(zero)) {
            System.out.println("FAIL (got " + result + " rather than a 0 value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.println("Check count_chars with non NULL parameter ... ");
        result = c.callProcedure( "count_chars", Value.createText(testString));
        
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + " rather than expected " + expected + ")");
            return false;
        }
        else if ( ! result.equals(testSize)) {
            System.out.println("FAIL (got " + result + " rather than a " + testSize + " value.");
            return false;
        }
        else 
            System.out.println("OK");
       
        return true;
    }
    
    boolean executeTestTextHash(Connection c) throws IOException {
        System.out.print("Check hash_text with NULL parameter ... ");
        Value result = c.callProcedure( "hash_text", Value.createText());
        final ValueType expected = ValueType.create(ValueType.UINT64);
        final Value hash1 = Value.createUInt64(1111971797248785981l);
        final Value hash2 = Value.createUInt64(6791012167670732276l);
        final Value hash3 = Value.createUInt64(5403826621455100014l);
        final String testString1 = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics!"; 
        final String testString2 = "Another string!";
        final String testString3 = "π, Φ and \uD835\uDD0A are spacial chars used in mathematics"; 

        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.isNull()) {
            System.out.println("FAIL (got " + result + " rather than a 0 value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check hash_text with non NULL string ");
        result = c.callProcedure( "hash_text", Value.createText(testString1));
        
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(hash1)) {
            System.out.println("FAIL (got " + result + " rather than a " + hash1 + " value.");
            return false;
        }
        else 
            System.out.println("OK");
       
        System.out.print("Check hash_text with non NULL string (2) ");
        result = c.callProcedure( "hash_text", Value.createText(testString2));
        
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(hash2)) {
            System.out.println("FAIL (got " + result + " rather than a " + hash2 + " value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check hash_text with non NULL string (3) ");
        result = c.callProcedure( "hash_text", Value.createText(testString3));
        
        if (! result.type().equals(expected)) {
            System.out.println("FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        }
        else if ( ! result.equals(hash3)) {
            System.out.println("FAIL (got " + result + " rather than a " + hash3 + " value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    

}

    
    