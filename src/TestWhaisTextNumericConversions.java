import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.Connection;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;

public class TestWhaisTextNumericConversions
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
        TestWhaisTextNumericConversions t = new TestWhaisTextNumericConversions();
        testResult &= t.executeTestDigit(c);
        testResult &= t.executeTestToUInt(c);
        testResult &= t.executeTestFromUInt(c);
        
        c.close ();
    
        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }
    
        System.out.println ("TEST RESULT: PASS");
    }
    
    
    boolean executeTestDigit(Connection c) throws IOException {
        System.out.print("Check digit with NULL parameter ... ");
        Value result = c.callProcedure( "digit", Value.createChar(), Value.createUInt8());
        final ValueType expected = ValueType.create(ValueType.UINT8);
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
        
        System.out.print("Check digit with '0' parameter ... ");
        result = c.callProcedure( "digit", Value.createChar('0'), Value.createUInt8(3));
        if (! result.equals( Value.createUInt8(0))) {
            System.out.println("FAIL (got " + result + " rather than '0' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with 'a' parameter (base 16)... ");
        result = c.callProcedure( "digit", Value.createChar('a'), Value.createUInt8(16));
        if (! result.equals( Value.createUInt8(10))) {
            System.out.println("FAIL (got " + result + " rather than '10' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with 'B' parameter (base 16)... ");
        result = c.callProcedure( "digit", Value.createChar('B'), Value.createUInt8(16));
        if (! result.equals( Value.createUInt8(11))) {
            System.out.println("FAIL (got " + result + " rather than '11' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with 'a' parameter (base 32)... ");
        result = c.callProcedure( "digit", Value.createChar('a'), Value.createUInt8(32));
        if (! result.equals( Value.createUInt8(0))) {
            System.out.println("FAIL (got " + result + " rather than '0' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with 'A' parameter (base 32)... ");
        result = c.callProcedure( "digit", Value.createChar('A'), Value.createUInt8(32));
        if (! result.equals( Value.createUInt8(0))) {
            System.out.println("FAIL (got " + result + " rather than '0' value.");
            return false;
        }
        else 
            System.out.println("OK");

        System.out.print("Check digit with '2' parameter (base 32)... ");
        result = c.callProcedure( "digit", Value.createChar('2'), Value.createUInt8(32));
        if (! result.equals( Value.createUInt8(26))) {
            System.out.println("FAIL (got " + result + " rather than '26' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with '1' parameter (base 32)... ");
        result = c.callProcedure( "digit", Value.createChar('1'), Value.createUInt8(32));
        if (! result.equals( Value.createUInt8())) {
            System.out.println("FAIL (got " + result + " rather than 'null' value.");
            return false;
        }
        else 
            System.out.println("OK");

        System.out.print("Check digit with 'b' parameter (base 64)... ");
        result = c.callProcedure( "digit", Value.createChar('b'), Value.createUInt8(64));
        if (! result.equals( Value.createUInt8(27))) {
            System.out.println("FAIL (got " + result + " rather than '27' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with 'B' parameter (base 64)... ");
        result = c.callProcedure( "digit", Value.createChar('B'), Value.createUInt8(64));
        if (! result.equals( Value.createUInt8(1))) {
            System.out.println("FAIL (got " + result + " rather than '1' value.");
            return false;
        }
        else 
            System.out.println("OK");

        System.out.print("Check digit with '2' parameter (base 64)... ");
        result = c.callProcedure( "digit", Value.createChar('2'), Value.createUInt8(64));
        if (! result.equals( Value.createUInt8(54))) {
            System.out.println("FAIL (got " + result + " rather than '54' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with '+' parameter (base 64)... ");
        result = c.callProcedure( "digit", Value.createChar('+'), Value.createUInt8(64));
        if (! result.equals( Value.createUInt8(62))) {
            System.out.println("FAIL (got " + result + " rather than '62' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        System.out.print("Check digit with '/' parameter (base 64)... ");
        result = c.callProcedure( "digit", Value.createChar('/'), Value.createUInt8(64));
        if (! result.equals( Value.createUInt8(63))) {
            System.out.println("FAIL (got " + result + " rather than '63' value.");
            return false;
        }
        else 
            System.out.println("OK");
        
        return true;
    }
    
    boolean executeTestToUInt( Connection c) throws IOException
    {
        System.out.print( "Check to_uint with NULL parameter ... ");
        Value result = c.callProcedure( "to_uint", Value.createText(), Value.createUInt8( 2), Value.createUInt64( 0));
        final ValueType expected = ValueType.create( ValueType.UINT64);
        if (!result.type().equals( expected)) {
            System.out.println(
                    "FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than a null value.");
            return false;
        } else
            System.out.println( "OK");

        Value expResult = null;
        System.out.print( "Check to_uint with '0' parameter (base 2)... ");
        result = c.callProcedure( "to_uint", Value.createText( "0"), Value.createUInt8( 2), Value.createUInt64());
        if (!result.equals( Value.createUInt64( 0))) {
            System.out.println( "FAIL (got " + result + " rather than '0' value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with 's21' parameter (base 3, position 1)... ");
        result = c.callProcedure( "to_uint", Value.createText( "s21"), Value.createUInt8( 3), Value.createUInt64( 1));
        expResult = Value.createUInt64( 7);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '-21' parameter (base 8, position 1)... ");
        result = c.callProcedure( "to_uint", Value.createText( "-21"), Value.createUInt8( 8), Value.createUInt64( 1));
        expResult = Value.createUInt64( 17);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '201' parameter (base 10, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "201"), Value.createUInt8( 10), Value.createUInt64());
        expResult = Value.createUInt64( 201);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with 'AFaF09' parameter (base 16, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "AFaF09"), Value.createUInt8( 16), Value.createUInt64());
        expResult = Value.createUInt64( 11513609);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with 'AFaF07' parameter (base 32, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "AFaF07"), Value.createUInt8( 32), Value.createUInt64());
        expResult = Value.createUInt64();
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '0AFaF07' parameter (base 16, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "AFaF07"), Value.createUInt8( 16), Value.createUInt64());
        expResult = Value.createUInt64( 11513607);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with 's0AZaz27' parameter (base 32, position 2)... ");
        result = c.callProcedure( "to_uint", Value.createText( "s0AZaz27"), Value.createUInt8(32), Value.createUInt64( 2));
        expResult = Value.createUInt64( 26240863);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '0AZaz09' parameter (base 64, position 1)... ");
        result = c.callProcedure( "to_uint", Value.createText( "0AZaz09"), Value.createUInt8(64), Value.createUInt64( 1));
        expResult = Value.createUInt64( Long.parseLong("426458429"));
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with 'A+/A' parameter (base 64, position 0)... ");
        result = c.callProcedure( "to_uint", Value.createText( "A+/A"), Value.createUInt8( 64), Value.createUInt64( 0));
        expResult = Value.createUInt64( 257984);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '101' parameter (base 2, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "101"), Value.createUInt8( 2), Value.createUInt64());
        expResult = Value.createUInt64( 5);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '102' parameter (base 3, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "102"), Value.createUInt8( 3), Value.createUInt64());
        expResult = Value.createUInt64( 11);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '103' parameter (base 4, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "103"), Value.createUInt8( 4), Value.createUInt64());
        expResult = Value.createUInt64( 19);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '104' parameter (base 5, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "104"), Value.createUInt8( 5), Value.createUInt64());
        expResult = Value.createUInt64( 29);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '105' parameter (base 6, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "105"), Value.createUInt8( 6), Value.createUInt64());
        expResult = Value.createUInt64( 41);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '106' parameter (base 7, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "106"), Value.createUInt8( 7), Value.createUInt64());
        expResult = Value.createUInt64( 55);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '107' parameter (base 8, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "107"), Value.createUInt8( 8), Value.createUInt64());
        expResult = Value.createUInt64( 71);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '108' parameter (base 9, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "108"), Value.createUInt8( 9), Value.createUInt64());
        expResult = Value.createUInt64( 89);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check to_uint with '109' parameter (base 10, position NULL)... ");
        result = c.callProcedure( "to_uint", Value.createText( "109"), Value.createUInt8( 10), Value.createUInt64());
        expResult = Value.createUInt64( 109);
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        return true;
    }
    
    
    boolean executeTestFromUInt( Connection c) throws IOException
    {
        System.out.print( "Check from_uint with NULL parameter ... ");
        Value result = c.callProcedure( "from_uint", Value.createUInt64(), Value.createUInt8(2));
        final ValueType expected = ValueType.create( ValueType.TEXT);
        if (!result.type().equals( expected)) {
            System.out.println(
                    "FAIL (the type retrieved is " + result.type() + "rather than expected." + expected + ")");
            return false;
        } else if (!result.isNull()) {
            System.out.println( "FAIL (got " + result + " rather than a null value.");
            return false;
        } else
            System.out.println( "OK");

        Value expResult = null;
        System.out.print( "Check from_uint with '0' parameter (base 2)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(2));
        if (!result.equals( Value.createText("0"))) {
            System.out.println( "FAIL (got " + result + " rather than '0' value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '7' parameter (base 3)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(7), Value.createUInt8( 3));
        expResult = Value.createText("21");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '17' parameter (base 8)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(17), Value.createUInt8( 8));
        expResult = Value.createText("21");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '201' parameter (base 10)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(201), Value.createUInt8());
        expResult = Value.createText("201");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '11513609' parameter (base 16)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(11513609), Value.createUInt8( 16));
        expResult = Value.createText("AFAF09");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '11513607' parameter (base 16, position NULL)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(11513607), Value.createUInt8( 16));
        expResult = Value.createText("AFAF07");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '26240863' parameter (base 32)");
        result = c.callProcedure( "from_uint", Value.createUInt64(26240863), Value.createUInt8( 32));
        expResult = Value.createText("ZAZ27");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '426458429' parameter (base 64)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(426458429), Value.createUInt8( 64));
        expResult = Value.createText("Zaz09");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '257984' parameter (base 64)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(257984), Value.createUInt8( 64));
        expResult = Value.createText("+/A");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '5' parameter (base 2)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(5), Value.createUInt8( 2));
        expResult = Value.createText("101");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '11' parameter (base 3)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(11), Value.createUInt8( 3));
        expResult = Value.createText("102");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '19' parameter (base 4)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(19), Value.createUInt8( 4));
        expResult = Value.createText("103");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '29' parameter (base 5)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(29), Value.createUInt8( 5));
        expResult = Value.createText("104");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '41' parameter (base 6) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(41), Value.createUInt8( 6));
        expResult = Value.createText("105");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '55' parameter (base 7)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(55), Value.createUInt8( 7));
        expResult = Value.createText("106");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '71' parameter (base 8)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(71), Value.createUInt8( 8));
        expResult = Value.createText("107");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '108' parameter (base 9)... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(89), Value.createUInt8( 9));
        expResult = Value.createText("108");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");

        System.out.print( "Check from_uint with '109' parameter (base 10) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(109), Value.createUInt8( 10));
        expResult = Value.createText("109");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        
        System.out.print( "Check from_uint with '0' parameter (base 2) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(2));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        
             
        System.out.print( "Check from_uint with '0' parameter (base 3) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(3));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
                
        
        System.out.print( "Check from_uint with '0' parameter (base 4) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(4));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 5) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(5));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 6) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(6));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 7) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(7));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 8) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(8));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 9) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(9));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        
        System.out.print( "Check from_uint with '0' parameter (base 10) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8());
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 16) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(16));
        expResult = Value.createText("0");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 32) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(32));
        expResult = Value.createText("A");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Check from_uint with '0' parameter (base 64) ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(64));
        expResult = Value.createText("A");
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than " + expResult + " value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Test form_int with invalid base 1 ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(1));
        expResult = Value.createText();
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than null value.");
            return false;
        } else
            System.out.println( "OK");
        
        System.out.print( "Test form_int with invalid base 11 ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(11));
        expResult = Value.createText();
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than null value.");
            return false;
        } else
            System.out.println( "OK");
     
        System.out.print( "Test form_int with invalid base 53 ... ");
        result = c.callProcedure( "from_uint", Value.createUInt64(0), Value.createUInt8(53));
        expResult = Value.createText();
        if (!result.equals( expResult)) {
            System.out.println( "FAIL (got " + result + " rather than null value.");
            return false;
        } else
            System.out.println( "OK");
     
        return true;
    }
}
