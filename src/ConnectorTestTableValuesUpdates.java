import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.ArrayValue;
import net.whais.Client.ConnException;
import net.whais.Client.Connection;
import net.whais.Client.TableFieldType;
import net.whais.Client.TableValue;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;


public class ConnectorTestTableValuesUpdates
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

        Value
        getBasicValue (int i) throws ConnException
        {
            if (i >= this.values.length)
                return Value.createBasic (this.type);

            return Value.createBasic (this.type, this.values[i]);
        }

        final ValueType type;
        final String[]  values;
    };

    public ConnectorTestTableValuesUpdates() throws ConnException
    {
        this.tableFields = new TableFieldType[31];

        this.tableFields[0] = new TableFieldType ("field_bool", ValueType.boolType ());
        this.tableFields[1] = new TableFieldType ("field_char", ValueType.charType ());
        this.tableFields[2] = new TableFieldType ("field_date", ValueType.dateType ());
        this.tableFields[3] = new TableFieldType ("field_date_time", ValueType.datetimeType ());
        this.tableFields[4] = new TableFieldType ("field_hires_time", ValueType.hirestimeType ());
        this.tableFields[5] = new TableFieldType ("field_int8", ValueType.int8Type ());
        this.tableFields[6] = new TableFieldType ("field_int16", ValueType.int16Type ());
        this.tableFields[7] = new TableFieldType ("field_int32", ValueType.int32Type ());
        this.tableFields[8] = new TableFieldType ("field_int64", ValueType.int64Type ());
        this.tableFields[9] = new TableFieldType ("field_uint8", ValueType.uint8Type ());
        this.tableFields[10] = new TableFieldType ("field_uint16", ValueType.uint16Type ());
        this.tableFields[11] = new TableFieldType ("field_uint32", ValueType.uint32Type ());
        this.tableFields[12] = new TableFieldType ("field_uint64", ValueType.uint64Type ());
        this.tableFields[13] = new TableFieldType ("field_real", ValueType.realType ());
        this.tableFields[14] = new TableFieldType ("field_rich_real", ValueType.richrealType ());
        this.tableFields[15] = new TableFieldType ("field_text", ValueType.richrealType ());

        this.tableFields[16] = new TableFieldType ("field_a_bool", ValueType.arrayBoolType ());
        this.tableFields[17] = new TableFieldType ("field_a_char", ValueType.arrayCharType ());
        this.tableFields[18] = new TableFieldType ("field_a_date", ValueType.arrayDateType ());
        this.tableFields[19] = new TableFieldType ("field_a_date_time", ValueType.arrayDatetimeType ());
        this.tableFields[20] = new TableFieldType ("field_a_hires_time", ValueType.arrayHirestimeType ());
        this.tableFields[21] = new TableFieldType ("field_a_int8", ValueType.arrayInt8Type ());
        this.tableFields[22] = new TableFieldType ("field_a_int16", ValueType.arrayInt16Type ());
        this.tableFields[23] = new TableFieldType ("field_a_int32", ValueType.arrayInt32Type ());
        this.tableFields[24] = new TableFieldType ("field_a_int64", ValueType.arrayInt64Type ());
        this.tableFields[25] = new TableFieldType ("field_a_uint8", ValueType.arrayUInt8Type ());
        this.tableFields[26] = new TableFieldType ("field_a_uint16", ValueType.arrayUInt16Type ());
        this.tableFields[27] = new TableFieldType ("field_a_uint32", ValueType.arrayUInt32Type ());
        this.tableFields[28] = new TableFieldType ("field_a_uint64", ValueType.arrayUInt64Type ());
        this.tableFields[29] = new TableFieldType ("field_a_real", ValueType.arrayRealType ());
        this.tableFields[30] = new TableFieldType ("field_a_rich_real", ValueType.arrayRichrealType ());

        this.testValues = new TestValue[16];

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

        this.testValues[15] = new TestValue (
                ValueType.create (ValueType.TEXT),
                                  "A fost unul dintre acele momente astrale clipa în care am ridicat de la poștă "
                                  + "o scrisoare venită de dincolo de ocean. În clipa când am deschis-o nu mi-a venit "
                                  + "să îmi cred ochilor și m-a cuprins o atât de mare amețeală, "
                                  + "încât a trebuit să mă sprijin de un perete ca să nu cad. Scrisoarea conținea o "
                                  + "invitație semnată personal de un personaj foarte drag mie, unul care a contribuit "
                                  + "din plin la formarea mea. Nu știu de unde au aflat, dar a doua zi la ușa mea s-au înființat "
                                  + "doi ofițeri de securitate, care au început să mă descoasă cu amabilitate oarecum "
                                  + "amenințătoare. Probabil că răspunsurile mele le-au sugerat că nu sunt un "
                                  + "element nesănătos și că am trăsăturile omului nou, care nu poate trăi departe "
                                  + "de patrie, pentru că în scurt timp am fost invitat să completez formularele pentru "
                                  + "pașaport. Ciudat lucru ... eu nu depusesem nici o cerere în acest sens."
                                  + "Călătoria a fost una de vis, o aventură minunată, cum numai în visul unui copil poate "
                                  + "exista. Am avut parte de tot luxul pe care mi-l puteam imagina. Așa că m-am "
                                  + "înfruptat din toate acele bunătăți din care nu mai gustasem demult. "
                                  + "Vă dați seama? La bordul avionului aveau până și portocale! Dar să nu ne mai lungim "
                                  + "cu vorba. Avionul m-a lăsat la aeroportul JFK de lângă New York. Aici mă aștepta "
                                  + "deja un mic avion cu reacție, care m-a purtat către Ithaca, orașul lui Sagan. "
                                  + "Am aterizat pe aeroportul regional Tompkins, iar de aici am fost luat cu o "
                                  + "limuzină, care m-a dus la casa lui Sagan, pe Stewart Avenue la numărul 900."
                                  + "Peisajul din jur era de-a dreptul încântător, iar în apropierea casei o mică, "
                                  + "dar spectaculoasă, cascadă mă făcea, pe mine, omul asfaltului, să mă simt "
                                  + "ca pe o altă planetă. Ajunsesem lângă casa celui care mă invitase la el. Acum, "
                                  + "după ce v-am lăsat destul de mult timp pradă suspansului, vă pot spune despre "
                                  + "cine este vorba. Am fost invitat la o \"discuție ca între prieteni buni\" "
                                  + "(așa scria în invitația pe care o primisem), de către omul de la care am învățat că, "
                                  + "alături de rigoare, știința are dimensiunea frumuseții. Am învățat de la el că "
                                  + "știința poate fi povestită, întocmai cum o făceau povestitorii din vechime.",

                                  "My name is Bond, James Bond!",
                                  "Acest text este in chineză cred: 表中有關罷免、死亡的內容應為 倒文! Sper că nu am jignit pe nimeni."


                                            );
    }

    public static void
    main (String[] args) throws IOException
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

        ConnectorTestTableValuesUpdates test =
                new ConnectorTestTableValuesUpdates ();


        boolean testResult = true;

        testResult = testResult && test.testTables (c, 0, false);
        testResult = testResult && test.testTables (c, 1, false);
        testResult = testResult && test.testTables (c, 10, false);
        testResult = testResult && test.testTables (c, 64, false);
        testResult = testResult && test.testTables (c, 128, false);
        testResult = testResult && test.testTables (c, 1024, false);

        testResult = testResult && test.testTables (c, 0, true);
        testResult = testResult && test.testTables (c, 1, true);
        testResult = testResult && test.testTables (c, 10, true);
        testResult = testResult && test.testTables (c, 64, true);
        testResult = testResult && test.testTables (c, 128, true);
        testResult = testResult && test.testTables (c, 1024, true);
        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }

    TableValue
    getOneFieldTable (int fieldId) throws ConnException
    {
        TableFieldType[] fields = { this.tableFields[fieldId] };

        return Value.createTable (fields);
    }

    TableValue
    getAllFieldsTable () throws ConnException
    {
        return Value.createTable (this.tableFields);
    }

    void
    fillTableCell (TableValue value, int row, int field) throws ConnException
    {
        if (row == value.getRowsCount ())
            value.addRows (1);

        final ValueType fieldType = value.getFields ()[field].getType ();
        TestValue refValue = null;
        switch (fieldType.getBaseType ())
        {
        case ValueType.BOOL:
            refValue = this.testValues [0];
            break;

        case ValueType.CHAR:
            refValue = this.testValues [1];
            break;

        case ValueType.DATE:
            refValue = this.testValues [2];
            break;

        case ValueType.DATETIME:
            refValue = this.testValues [3];
            break;

        case ValueType.HIRESTIME:
            refValue = this.testValues [4];
            break;

        case ValueType.INT8:
            refValue = this.testValues [5];
            break;

        case ValueType.INT16:
            refValue = this.testValues [6];
            break;

        case ValueType.INT32:
            refValue = this.testValues [7];
            break;

        case ValueType.INT64:
            refValue = this.testValues [8];
            break;

        case ValueType.UINT8:
            refValue = this.testValues [9];
            break;

        case ValueType.UINT16:
            refValue = this.testValues [10];
            break;

        case ValueType.UINT32:
            refValue = this.testValues [11];
            break;

        case ValueType.UINT64:
            refValue = this.testValues [12];
            break;

        case ValueType.REAL:
            refValue = this.testValues [13];
            break;

        case ValueType.RICHREAL:
            refValue = this.testValues [14];
            break;

        case ValueType.TEXT:
            refValue = this.testValues [15];
            break;
        }

        if ( fieldType.isArray ())
        {
            ArrayValue r = refValue.getArray ();
            ArrayValue cell = Value.createArray (r.type ());

            for (int i = row % r.size (); i < r.size (); ++i)
                cell.add (r.get (i));

            value.put (cell, value.getFields ()[field].getName (), row);
        }
        else
        {
           value.put (refValue.getBasicValue (row),
                      value.getFields ()[field].getName (),
                      row);
        }
    }

    TableValue
    fillTable (TableValue table, int rowsCount) throws ConnException
    {
        for (int row = 0; row <rowsCount; ++row)
        {
            for (int field = 0; field < table.getFields ().length; ++field)
                this.fillTableCell (table, row, field);
        }
        return table;
    }

    boolean
    testTables (Connection c, int rows, boolean bulk) throws IOException
    {
        System.out.println ("Testing "
                + (bulk ? "bulk " : "non bulk ")
                + "tables values updates with " + rows + " row(s) ...");

        for (int f = 0; f < this.tableFields.length; ++f)
        {
            TableValue table = this.getOneFieldTable (f);

            c.pushStackValue (table.type ());
            if ( ! bulk)
                c.flushStackUpdates ();

            this.fillTable (table, rows);
            if (! table.isNull ())
            {
                c.updateStackTop (table);
                if ( ! bulk)
                    c.flushStackUpdates ();
            }
        }

        TableValue largeTable = this.getAllFieldsTable ();
        c.pushStackValue (largeTable.type ());
        if (! bulk)
            c.flushStackUpdates ();

        this.fillTable (largeTable, rows);
        if ( ! largeTable.isNull ())
            c.updateStackTop (largeTable);

        c.flushStackUpdates ();

        ValueType type = c.describeStackTop ();
        if (! (type.equals (largeTable.type ())
               && largeTable.type ().equals (type)))
        {
            System.out.println ("Failed to check large table field type.");
            return false;
        }

        Value largeStackTable = c.retrieveStackTop ();

        if (rows == 0)
        {
            if (! (largeStackTable.isNull ()
                   && largeTable.isNull ()))
            {
                System.out.println ("Failed to retrieve/create null tables");
                return false;
            }
        }

        if (! (largeTable.equals (largeStackTable)
               && largeStackTable.equals (largeTable)))
        {
            System.out.println ("Failed to retrieve a proper table value "
                                + "for large table test");
            return false;
        }

        c.popStackValues (1);

        for (int f = this.tableFields.length - 1; f >= 0; --f)
        {
            c.flushStackUpdates ();

            TableValue ref = this.getOneFieldTable (f);
            ValueType stackType = c.describeStackTop ();

            if (! (stackType.equals (ref.type ())
                   && ref.type ().equals (stackType)))
            {
                System.out.println ("Failed to check table field types for "
                                     + "field " + f + '.');
                return false;
            }

            Value stack = c.retrieveStackTop ();
            this.fillTable (ref, rows);

            if (rows == 0)
            {
                if (! (ref.isNull ()
                       && stack.isNull ()))
                {
                    System.out.println ("Failed to retrieve/create null tables"
                                         + "for field " + f + '.');
                    return false;
                }
            }

            if (! (ref.equals (stack)
                   && stack.equals (ref)))
            {
                System.out.println ("Failed to retrieve a proper table value "
                                    + "for field " + f + '.');
                return false;
            }

            c.popStackValues (1);
        }


        return true;
    }

    TestValue[] testValues;
    TableFieldType[] tableFields;

}
