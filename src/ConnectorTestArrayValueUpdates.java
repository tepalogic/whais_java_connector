import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.ArrayValue;
import net.whais.Client.ConnException;
import net.whais.Client.Connection;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;


public class ConnectorTestArrayValueUpdates
{
    class TestValue
    {
        public TestValue (ValueType type, String ... values)
        {
            this.type   = type;
            this.values = values;
        }

        ArrayValue getArray () throws ConnException
        {
            return Value.createArray (this.type, this.values);
        }

        final ValueType type;
        final String[]  values;
    };

    public ConnectorTestArrayValueUpdates()
    {
        this.testValues = new TestValue[58];

        this.testValues[0] = new TestValue (ValueType.boolType (),
                                            "1",
                                            "0",
                                            "T",
                                            "F",
                                            "true",
                                            "TRUE",
                                            "1",
                                            "false",
                                            "FALSE");

        this.testValues[1] = new TestValue (ValueType.charType(),
                                            "P",
                                            "p",
                                            "a",
                                            "A",
                                            "b",
                                            "B",
                                            "_",
                                            "+",
                                            "\u00C0",
                                            "\u00FC",
                                            "\u0100",
                                            "\u017F",
                                            "\u0400",
                                            "\u04FF",
                                            "\u05D0",
                                            "\u05F4",
                                            "\u0620",
                                            "\u06FF",
                                            "\u2200",
                                            "\u20A8",
                                            "\u20AC",
                                            "\u3041",
                                            "\u30E8",
                                            "\u2EC6",
                                            "\uFE3C",
                                            "\uFFFF");

        this.testValues[2] = new TestValue (ValueType.dateType(),
                                            "1620/03/17",
                                            "1651/10/09",
                                            "1660/05/05",
                                            "1746/10/25",
                                            "1763/01/06",
                                            "1854/02/28",
                                            "1924/05/14",
                                            "1955/02/17",
                                            "1968/02/26",
                                            "2005/12/06",
                                            "-1637/02/06",
                                            "-1664/10/10",
                                            "-1773/03/30",
                                            "-1784/08/06",
                                            "-1860/03/29",
                                            "-1861/09/03",
                                            "-1914/12/30",
                                            "-1926/03/05",
                                            "-1944/04/14",
                                            "-1968/04/11");

        this.testValues[3] = new TestValue (ValueType.datetimeType (),
                                            "1620/03/17 0:0:0",
                                            "1651/10/09 23:59:59",
                                            "1660/05/05 1:21:11",
                                            "1746/10/25 21:22:45",
                                            "1763/01/06 02:42:54",
                                            "1854/02/28 07:22:30",
                                            "1924/05/14 13:53:56",
                                            "1955/02/17 15:58:30",
                                            "1968/02/26 3:9:9",
                                            "2005/12/06 17:10:11",
                                            "-1637/02/06 19:36:42",
                                            "-1664/10/10 8:20:46",
                                            "-1773/03/30 5:45:8",
                                            "-1784/08/06 9:36:13",
                                            "-1860/03/29 4:50:6",
                                            "-1861/09/03 1:29:20",
                                            "-1914/12/30 10:40:00",
                                            "-1926/03/05 1:2:3",
                                            "-1944/04/14 01:02:03",
                                            "-1968/04/11 7:9:0");
        this.testValues[4] = new TestValue (ValueType.hirestimeType (),
                                            "1620/03/17 0:0:0.595222",
                                            "1651/10/09 23:59:59.696339",
                                            "1660/05/05 1:21:11.17428",
                                            "1746/10/25 21:22:45.3628",
                                            "1763/01/06 02:42:54.9450",
                                            "1854/02/28 07:22:30.09610",
                                            "1924/05/14 13:53:56.090000",
                                            "1955/02/17 15:58:30.010000",
                                            "1968/02/26 20:49:59.00100",
                                            "2005/12/06 17:10:11.290157",
                                            "-1637/02/06 19:36:42.497315",
                                            "-1664/10/10 8:20:46.172585",
                                            "-1773/03/30 5:45:8.42757",
                                            "-1784/08/06 9:36:13.367111",
                                            "-1860/03/29 4:50:6.529578",
                                            "-1861/09/03 1:29:20.179908",
                                            "-1914/12/30 0:48:0.748323",
                                            "-1926/03/05 1:2:3.91433",
                                            "-1944/04/14 01:02:03.0000",
                                            "-1968/04/11 7:9:00.0");
        this.testValues[5] = new TestValue (ValueType.int8Type (),
                                            "64",
                                            "30",
                                            "-63",
                                            "-96",
                                            "-37",
                                            "24",
                                            "124",
                                            "-45",
                                            "-3",
                                            "19",
                                            "-11",
                                            "-66",
                                            "89",
                                            "10",
                                            "89",
                                            "50");

        this.testValues[6] = new TestValue (ValueType.int16Type (),
                                            "19712",
                                            "-747",
                                            "26091",
                                            "-2209",
                                            "6112",
                                            "27515",
                                            "27211",
                                            "30430",
                                            "11311",
                                            "-16505",
                                            "11324",
                                            "-30693",
                                            "-4755",
                                            "31371",
                                            "12019",
                                            "-18660",
                                            "31708",
                                            "9051",
                                            "13807",
                                            "-31719",
                                            "31033",
                                            "26221",
                                            "16066");

        this.testValues[7] = new TestValue (ValueType.int32Type (),
                                            "558043300",
                                            "-733183314",
                                            "651249333",
                                            "-336618202",
                                            "736309700",
                                            "565899840",
                                            "-579235296",
                                            "57844493",
                                            "498773737",
                                            "-348942816",
                                            "-279628119",
                                            "-969527876",
                                            "290114381",
                                            "-355550703",
                                            "-25188561");

        this.testValues[8] = new TestValue (ValueType.int64Type (),
                                            "-618440080502",
                                            "-366820000393",
                                            "-387293476",
                                            "9036030032411",
                                            "190083122846",
                                            "3565843453206",
                                            "6201222312359",
                                            "-42182186",
                                            "-178718542567",
                                            "71947111351",
                                            "17788478",
                                            "4472822440724",
                                            "-1322875664214396",
                                            "-68584914545486",
                                            "9924814521485",
                                            "-81194385632106",
                                            "1232501455211760",
                                            "-52345994532181",
                                            "561458027854782",
                                            "170038544106",
                                            "-64211745524954",
                                            "13752728454274",
                                            "432645245221566",
                                            "-47070585454110",
                                            "454985654533326");
        this.testValues[9] = new TestValue (ValueType.uint8Type (),
                                            "57",
                                            "165",
                                            "53",
                                            "16",
                                            "138",
                                            "45",
                                            "82",
                                            "153",
                                            "8",
                                            "186",
                                            "24",
                                            "218",
                                            "134");
        this.testValues[10] = new TestValue (ValueType.uint16Type (),
                                             "39654",
                                             "26144",
                                             "27979",
                                             "10471",
                                             "20703",
                                             "7031",
                                             "5278",
                                             "46163",
                                             "5864",
                                             "42585",
                                             "4518",
                                             "17893",
                                             "576");
        this.testValues[11] = new TestValue (ValueType.uint32Type (),
                                             "775036026",
                                             "537806721",
                                             "654312075",
                                             "216900374");
        this.testValues[12] = new TestValue (ValueType.uint64Type (),
                                             "169335245703",
                                             "9545450702205",
                                             "268470497",
                                             "57674545415323",
                                             "426932548",
                                             "14460454449458",
                                             "404365063",
                                             "59893744454136",
                                             "502923444426",
                                             "431863455",
                                             "6119764445507",
                                             "622775620",
                                             "918144438352",
                                             "305931459",
                                             "691744457499",
                                             "278248589",
                                             "197142620",
                                             "611417566",
                                             "2043915544182",
                                             "843578090",
                                             "50450162",
                                             "125317079",
                                             "698778945479",
                                             "921987670",
                                             "213247697",
                                             "62747404",
                                             "506803358453",
                                             "369625010",
                                             "113617587571450",
                                             "228828221",
                                             "217114780");

        this.testValues[13] = new TestValue (ValueType.realType (),
                                             "5722.527019",
                                             "-1.647008",
                                             "946875979.8",
                                             "-0.069158",
                                             "-82362090.71",
                                             "0557.784294",
                                             "0.526550",
                                             "-0.292816",
                                             "-0.221960",
                                             "4102.006376",
                                             "0.0",
                                             "-0.0",
                                             "1.0",
                                             "-1.00");

        this.testValues[14] = new TestValue (ValueType.richrealType (),
                                             "316.17635757123",
                                             "-4395.657287",
                                             "395.1140709",
                                             "-4841.0799",
                                             "-0.56465457552828",
                                             "-1.691453213",
                                             "-0.73222444570584",
                                             "1.776564167",
                                             "0.01856913117",
                                             "-0.03537775171415",
                                             "0.08875877718831",
                                             "0.543197577497",
                                             "4514.557477554",
                                             "0.6015752234",
                                             "-94337092611.0",
                                             "0.0",
                                             "-0.0",
                                             "1.0",
                                             "-1.00");

        this.testValues[15] = new TestValue (ValueType.boolType ());
        this.testValues[16] = new TestValue (ValueType.boolType (), "T");
        this.testValues[17] = new TestValue (ValueType.charType (), "z");
        this.testValues[18] = new TestValue (ValueType.dateType());
        this.testValues[19] = new TestValue (ValueType.dateType(), "191/11/11");
        this.testValues[20] = new TestValue (ValueType.datetimeType());
        this.testValues[21] = new TestValue (ValueType.datetimeType(), "191/11/11 1:2:3");
        this.testValues[22] = new TestValue (ValueType.hirestimeType());
        this.testValues[23] = new TestValue (ValueType.hirestimeType(), "191/11/11 1:2:3.12111");
        this.testValues[24] = new TestValue (ValueType.int8Type ());
        this.testValues[25] = new TestValue (ValueType.int8Type (), "0");
        this.testValues[26] = new TestValue (ValueType.int8Type (), "0", "1");
        this.testValues[27] = new TestValue (ValueType.int16Type ());
        this.testValues[28] = new TestValue (ValueType.int16Type (), "0");
        this.testValues[29] = new TestValue (ValueType.int16Type (), "0", "1");
        this.testValues[30] = new TestValue (ValueType.int32Type ());
        this.testValues[31] = new TestValue (ValueType.int32Type (), "0");
        this.testValues[32] = new TestValue (ValueType.int32Type (), "0", "1");
        this.testValues[33] = new TestValue (ValueType.int64Type ());
        this.testValues[34] = new TestValue (ValueType.int64Type (), "0");
        this.testValues[35] = new TestValue (ValueType.int64Type (), "0", "1");
        this.testValues[36] = new TestValue (ValueType.uint8Type ());
        this.testValues[37] = new TestValue (ValueType.uint8Type (), "0");
        this.testValues[38] = new TestValue (ValueType.uint8Type (), "0", "1");
        this.testValues[39] = new TestValue (ValueType.uint16Type ());
        this.testValues[40] = new TestValue (ValueType.uint16Type (), "0");
        this.testValues[41] = new TestValue (ValueType.uint16Type (), "0", "1");
        this.testValues[42] = new TestValue (ValueType.uint32Type ());
        this.testValues[43] = new TestValue (ValueType.uint32Type (), "0");
        this.testValues[44] = new TestValue (ValueType.uint32Type (), "0", "1");
        this.testValues[45] = new TestValue (ValueType.uint64Type ());
        this.testValues[46] = new TestValue (ValueType.uint64Type (), "0");
        this.testValues[47] = new TestValue (ValueType.uint64Type (), "0", "1");
        this.testValues[48] = new TestValue (ValueType.uint64Type (), "0", "2");

        this.testValues[49] = new TestValue (ValueType.realType ());
        this.testValues[50] = new TestValue (ValueType.realType (), "0");
        this.testValues[51] = new TestValue (ValueType.realType (), "0", "1");

        this.testValues[52] = new TestValue (ValueType.richrealType ());
        this.testValues[53] = new TestValue (ValueType.richrealType (), "0");
        this.testValues[54] = new TestValue (ValueType.richrealType (), "0", "1");

        this.testValues[55] = new TestValue (ValueType.dateType (), "1/1/1");
        this.testValues[56] = new TestValue (ValueType.datetimeType (), "1/1/1 0:0:0");
        this.testValues[57] = new TestValue (ValueType.hirestimeType (), "1/1/1 0:0:0.0");
    }

    public static void main (String[] args) throws IOException
    {
        String[]  customArgs = {
                                "--root",
                                "-d",
                                "test_list_db_frame_size",
                                "--fs",
                                "65535"
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

        ConnectorTestArrayValueUpdates test =
                new ConnectorTestArrayValueUpdates ();

        boolean testResult = test.checkValuesOp ();

        testResult = testResult && test.checkStackValuesUpdates(c, false, 1024);
        testResult = testResult && test.checkStackValuesUpdates(c, true, 1024);

        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }

    boolean checkValueUpdatesOneByOne (Connection c) throws IOException
    {
        System.out.println ("Checking one by one stack update...");

        for ( int i = 0; i < this.testValues.length; ++i)
        {
            c.pushStackValue (this.testValues[i].type);
            c.flushStackUpdates ();
            c.updateStackTop (Value.createBasic (this.testValues[i].type,
                                                 this.testValues[i].values[0]));
            c.flushStackUpdates ();
        }

        for ( int i = this.testValues.length - 1; i >= 0; --i)
        {
            c.flushStackUpdates ();

            ValueType type = c.describeStackTop();
            if (! (type.equals (this.testValues[i].type)
                   && this.testValues[i].type.equals(type)))
            {
                System.out.println ("For test value " + i
                                    + " failed to retreive its type:\n "
                                    + type + " vs. " + this.testValues[i].type);
                return false;
            }

            if (c.retrieveStackTopRowsCount () >= 0)
            {
                System.out.println ("Invalid rows count for value " + i + '.');
                return false;
            }

            Value value = c.retrieveStackTop ();
            Value refValue = Value.createBasic (this.testValues[i].type,
                                                this.testValues[i].values[0]);
            if (! (value.equals (refValue) && refValue.equals (value)))
            {
                System.out.println ("For test value " + i
                                    + " failed to retreive its value correctly '"
                                    + value + "' vs. '" + refValue + '\'');
                return false;
            }

            c.popStackValues (1);
        }

        c.flushStackUpdates ();
        return true;
    }

    boolean pushStackValues (Connection c,
                             boolean    bulk,
                             int        count) throws IOException
    {
        for (int i = 0; i < count; ++i)
        {
            final ArrayValue v = this.testValues[i % this.testValues.length]
                                     .getArray ();
            c.pushStackValue (v.type ());
            if ( ! bulk )
            {
                c.flushStackUpdates ();
                if (! c.describeStackTop ().equals (v.type ()))
                {
                    System.out.println (
                        "Non bulk update, invalid retreived type for " + i + '.'
                                       );
                    return false;
                }

                final ArrayValue nv = (ArrayValue) c.retrieveStackTop ();
                if ( ! (nv.isNull ()
                        &&  nv.type ().equals (v.type ())
                        &&  nv.type().isArray ()))
                {
                    System.out.println (
                        "Non bulk update, invalid retreived value for " + i
                                       );
                    return false;
                }
            }

            if (i < count / 2)
            {
                if ( ! v.isNull ())
                    c.updateStackTop (v);
            }
            else
            {
                if ( ! v.isNull ())
                {
                    c.updateStackTop (v,
                                      Connection.IGNORE_FIELD,
                                      Connection.IGNORE_ROW);
                }
            }

            if ( ! bulk)
                c.flushStackUpdates ();
        }

        c.flushStackUpdates ();

        return true;
    }

    boolean checkStackValuesUpdates (Connection c,
                                     boolean    bulk,
                                     int        count) throws IOException
    {
        System.out.println ("Testing "
                            + (bulk ? "bulk " : "non bulk ")
                            + "values updates ... ");

        if ( ! this.pushStackValues (c, bulk, count))
            return false;

        for (int i = count - 1; i >= 0; --i)
        {
            final ArrayValue v = this.testValues[i % this.testValues.length]
                                     .getArray ();
            ArrayValue r =  (ArrayValue) (i < (count / 2)
                                ? c.retrieveStackTop ()
                                : c.retrieveStackTop (Connection.IGNORE_FIELD,
                                                      Connection.IGNORE_ROW));
            if (! v.type ().equals (r.type ()))
            {
                System.out.println ("Unexpected value type retrieved at " + i);
                return false;
            }
            else if ( ! (v.equals(r)
                         && r.equals(v)))
            {
                System.out.println ("Unexpected value retrieved at " + i);

                return false;
            }

            c.popStackValues (1);
            c.flushStackUpdates ();
        }

        return true;
    }

    boolean checkValuesOp () throws ConnException
    {
        System.out.println ("Testing equality result ... ");

        for ( int i = 0; i < this.testValues.length; ++i)
        {
            for (int j = 0; j < this.testValues.length; ++j)
            {
                final ArrayValue vi = this.testValues[i].getArray ();
                final ArrayValue vj = this.testValues[j].getArray ();

                if (i == j)
                {
                    if (! (vi.equals (vj)
                           && (vj.equals (vi))))
                    {
                        System.out.println ("Unexpected equality result for "
                                             + i + " value.");
                        return false;
                    }
                }
                else if (i != j)
                {
                    if (vi.equals (vj)
                        || vj.equals (vi))
                    {
                        System.out.println ("Unexpected inequality result for ("
                                             + i + ", " + j + ") values.");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    TestValue[] testValues;
}
