import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.ConnException;
import net.whais.Client.Connection;
import net.whais.Client.Value;
import net.whais.Client.ValueType;
import test.net.whais.Client.CommnandLine;


public class ConnectorTestBasicValueUpdates
{
    class TestValue
    {
        public TestValue (ValueType type, String ... values)
        {
            this.type   = type;
            this.values = values;
        }

        final ValueType type;
        final String[]  values;
    };

    ConnectorTestBasicValueUpdates () throws ConnException
    {
        this.testValues = new TestValue[501];

        this.testValues[0] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[1] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[2] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[3] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[4] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[5] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[6] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[7] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[8] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[9] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[10] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[11] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[12] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[13] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[14] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[15] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[16] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[17] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[18] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[19] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[20] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[21] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[22] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[23] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[24] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[25] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[26] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[27] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[28] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[29] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[30] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[31] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[32] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[33] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[34] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[35] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[36] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[37] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[38] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[39] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[40] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[41] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[42] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[43] = new TestValue(ValueType.create (ValueType.REAL), "-0.1");
        this.testValues[44] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[45] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[46] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[47] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[48] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[49] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[50] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[51] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[52] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[53] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[54] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[55] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[56] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[57] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[58] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[59] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[60] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[61] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[62] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[63] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[64] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[65] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[66] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[67] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[68] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[69] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[70] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[71] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[72] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[73] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[74] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[75] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[76] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[77] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[78] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[79] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[80] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[81] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[82] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[83] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[84] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[85] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[86] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[87] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[88] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[89] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[90] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[91] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[92] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[93] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[94] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[95] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[96] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[97] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[98] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[99] = new TestValue(ValueType.create (ValueType.INT16), "-10234");


        this.testValues[100] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[101] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[102] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[103] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[104] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[105] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[106] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[107] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[108] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[109] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[110] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[111] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[112] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[113] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[114] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[115] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[116] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[117] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[118] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[119] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[120] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[121] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[122] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[123] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[124] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[125] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[126] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[127] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[128] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[129] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[130] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[131] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[132] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[133] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[134] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[135] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[136] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[137] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[138] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[139] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[140] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[141] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[142] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[143] = new TestValue(ValueType.create (ValueType.TEXT), "Gandul info!");
        this.testValues[144] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[145] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[146] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[147] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[148] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[149] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[150] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[151] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[152] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[153] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[154] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[155] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[156] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[157] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[158] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[159] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[160] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[161] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[162] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[163] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[164] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[165] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[166] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[167] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[168] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[169] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[170] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[171] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[172] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[173] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[174] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[175] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[176] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[177] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[178] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[179] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[180] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[181] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[182] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[183] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[184] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[185] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[186] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[187] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[188] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[189] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[190] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[191] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[192] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[193] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[194] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[195] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[196] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[197] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[198] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[199] = new TestValue(ValueType.create (ValueType.INT16), "-10234");

        this.testValues[200] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[201] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[202] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[203] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[204] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[205] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[206] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[207] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[208] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[209] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[210] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[211] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[212] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[213] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[214] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[215] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[216] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[217] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[218] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[219] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[220] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[221] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[222] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[223] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[224] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[225] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[226] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[227] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[228] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[229] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[230] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[231] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[232] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[233] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[234] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[235] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[236] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[237] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[238] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[239] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[240] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[241] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[242] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[243] = new TestValue(ValueType.create (ValueType.INT8), "-90");
        this.testValues[244] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[245] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[246] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[247] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[248] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[249] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[250] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[251] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[252] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[253] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[254] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[255] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[256] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[257] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[258] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[259] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[260] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[261] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[262] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[263] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[264] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[265] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[266] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[267] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[268] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[269] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[270] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[271] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[272] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[273] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[274] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[275] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[276] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[277] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[278] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[279] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[280] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[281] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[282] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[283] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[284] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[285] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[286] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[287] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[288] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[289] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[290] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[291] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[292] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[293] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[294] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[295] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[296] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[297] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[298] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[299] = new TestValue(ValueType.create (ValueType.INT16), "-10234");


        this.testValues[300] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[301] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[302] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[303] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[304] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[305] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[306] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[307] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[308] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[309] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[310] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[311] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[312] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[313] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[314] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[316] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[315] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[317] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[318] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[319] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[320] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[321] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[322] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[323] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[324] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[325] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[326] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[327] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[328] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[329] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[330] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[331] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[332] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[333] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[334] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[335] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[336] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[337] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[338] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[339] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[340] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[341] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[342] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[344] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[345] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[346] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[347] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[348] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[349] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[350] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[351] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[352] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[353] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[354] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[355] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[356] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[357] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[358] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[359] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[360] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[361] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[362] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[363] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[364] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[365] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[366] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[367] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[368] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[369] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[370] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[371] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[372] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[373] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[374] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[375] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[376] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[377] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[378] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[379] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[380] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[381] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[382] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[383] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[384] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[385] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[386] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[387] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[388] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[389] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[390] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[391] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[392] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[393] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[394] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[395] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[396] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[397] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[398] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[399] = new TestValue(ValueType.create (ValueType.INT16), "-10234");

        this.testValues[300] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[301] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[302] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[303] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[304] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[305] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[306] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[307] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[308] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[309] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[310] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[311] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[312] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[313] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[314] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[315] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[315] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[317] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[318] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[319] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[320] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[321] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[322] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[323] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[324] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[325] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[326] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[327] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[328] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[329] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[330] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[331] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[332] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[333] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[334] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[335] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[336] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[337] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[338] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[339] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[340] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[341] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[342] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[343] = new TestValue(ValueType.create (ValueType.CHAR), "C");
        this.testValues[344] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[345] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[346] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[347] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[348] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[349] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[350] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[351] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[352] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[353] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[354] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[355] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[356] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[357] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[358] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[359] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[360] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[361] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[362] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[363] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[364] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[365] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[366] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[367] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[368] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[369] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[370] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[371] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[372] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[373] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[374] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[375] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[376] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[377] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[378] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[379] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[380] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[381] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[382] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[383] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[384] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[385] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[386] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[387] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[388] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[389] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[390] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[391] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[392] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[393] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[394] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[395] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[396] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[397] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[398] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[399] = new TestValue(ValueType.create (ValueType.INT16), "-10234");


        this.testValues[400] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[401] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[402] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[403] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[404] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[405] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[406] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[407] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[408] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[409] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[410] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[411] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[412] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[413] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[414] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[415] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[416] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[417] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[418] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[419] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[420] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[421] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[422] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[423] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[424] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[425] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[426] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[427] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[428] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[429] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[430] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[431] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[432] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[433] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[434] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[435] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[436] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[437] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[438] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[439] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[440] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[441] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[442] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[443] = new TestValue(ValueType.create (ValueType.BOOL), "1");
        this.testValues[444] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[445] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[446] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[447] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[448] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[449] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[450] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[451] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[452] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[453] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[454] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[455] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[456] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[457] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[458] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[459] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[460] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[461] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[462] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[463] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[464] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[465] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[466] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[467] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[468] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[469] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[470] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[471] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[472] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[473] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[474] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.190124");
        this.testValues[475] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[476] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[477] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[478] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[479] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[480] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[481] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[482] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[483] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[484] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[485] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[486] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[487] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[488] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[489] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[490] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[491] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[492] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[493] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[494] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[495] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[496] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[497] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[498] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[499] = new TestValue(ValueType.create (ValueType.INT16), "-10234");

        this.testValues[400] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[401] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[402] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[403] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[404] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[405] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[406] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[407] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[408] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[409] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[410] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[411] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[412] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[413] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[414] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[415] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[415] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[417] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[418] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[419] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[420] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[421] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[422] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[423] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[424] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[425] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[426] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[427] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.000004");
        this.testValues[428] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[429] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[430] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[431] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[432] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[433] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[434] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[435] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[436] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[437] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[438] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[439] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[440] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[441] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[442] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[444] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[445] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[446] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");


        this.testValues[447] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[448] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[449] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[450] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[451] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.90124");
        this.testValues[452] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[453] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[454] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[455] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[456] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[457] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[458] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[459] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[460] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[461] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[462] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[463] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[464] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[465] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[466] = new TestValue(ValueType.create (ValueType.REAL), "0.1");
        this.testValues[467] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[468] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[469] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[470] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[471] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[472] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[473] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 14:23:12");
        this.testValues[474] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.01");
        this.testValues[475] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[476] = new TestValue(ValueType.create (ValueType.INT16), "-10234");
        this.testValues[477] = new TestValue(ValueType.create (ValueType.INT32), "-34125");
        this.testValues[478] = new TestValue(ValueType.create (ValueType.INT64), "-1233411244563200");
        this.testValues[479] = new TestValue(ValueType.create (ValueType.UINT8), "245");
        this.testValues[480] = new TestValue(ValueType.create (ValueType.UINT16), "45678");
        this.testValues[481] = new TestValue(ValueType.create (ValueType.UINT32), "3099012231");
        this.testValues[482] = new TestValue(ValueType.create (ValueType.UINT64), "923341124456320012");
        this.testValues[483] = new TestValue(ValueType.create (ValueType.REAL), "-122.123");
        this.testValues[484] = new TestValue(ValueType.create (ValueType.RICHREAL), "538.154454223");
        this.testValues[485] = new TestValue(ValueType.create (ValueType.INT8), "123");
        this.testValues[486] = new TestValue(ValueType.create (ValueType.INT16), "561");
        this.testValues[487] = new TestValue(ValueType.create (ValueType.INT32), "134512334");
        this.testValues[488] = new TestValue(ValueType.create (ValueType.INT64), "923341124456320012");
        this.testValues[489] = new TestValue(ValueType.create (ValueType.REAL), "10.1");
        this.testValues[490] = new TestValue(ValueType.create (ValueType.REAL), "1");
        this.testValues[491] = new TestValue(ValueType.create (ValueType.RICHREAL), "-0.1");
        this.testValues[492] = new TestValue(ValueType.create (ValueType.RICHREAL), "-1");

        this.testValues[493] = new TestValue(ValueType.create (ValueType.BOOL), "0");
        this.testValues[494] = new TestValue(ValueType.create (ValueType.CHAR), "h");
        this.testValues[495] = new TestValue(ValueType.create (ValueType.DATE), "1201/12/10");
        this.testValues[496] = new TestValue(ValueType.create (ValueType.DATETIME), "2091/10/11 4:3:2");
        this.testValues[497] = new TestValue(ValueType.create (ValueType.HIRESTIME), "7812/2/3 14:31:12.012340");
        this.testValues[498] = new TestValue(ValueType.create (ValueType.INT8), "-67");
        this.testValues[499] = new TestValue(ValueType.create (ValueType.INT16), "-10234");

        this.testValues[500] = new TestValue (ValueType.create (ValueType.TEXT), "Test by Iulian");
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

        ConnectorTestBasicValueUpdates test =
                new ConnectorTestBasicValueUpdates ();

        boolean testResult = test.checkBasicTypes ();
        testResult = testResult && test.checkValueUpdatesOneByOne (c);
        testResult = testResult && test.checkValueUpdatesBulk (c);

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

    boolean checkValueUpdatesBulk (Connection c) throws IOException
    {
        System.out.println ("Checking bulk stack update...");

        for ( int i = 0; i < this.testValues.length; ++i)
        {
            c.pushStackValue (this.testValues[i].type);
            c.updateStackTop (Value.createBasic (this.testValues[i].type,
                                                 this.testValues[i].values[0]));
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

            Value value = c.retrieveStackTop ();
            Value refValue = Value.createBasic (this.testValues[i].type,
                                                this.testValues[i].values[0]);

            Value wrngNull = Value.createBasic (this.testValues[i].type);
            Value wrngNull2 = Value.createBasic (this.testValues[i].type);

            Value wrng = Value.createBasic (this.testValues[i].type,
                                            this.testValues[i].values[0].substring (1));
            if (! (value.equals (refValue) && refValue.equals (value)))
            {
                System.out.println ("For test value " + i
                                    + " failed to retreive its value correctly "
                                    + '\'' + value + "' vs. '" + refValue + '\'');
                return false;
            }

            if ((value.equals (wrng) || wrng.equals (value)) && (i >= 470))
            {
                System.out.println ("For test value " + i
                        + " failed to use the 'equals' test correctly '"
                        + value + "' vs. '" + wrng + '\'');

                return false;
            }

            if (value.equals (wrngNull)
                || wrngNull.equals (value)
                || ( ! wrngNull.isNull ())
                || ( ! (wrngNull.equals (wrngNull2))))
           {
               System.out.println ("For test value " + i
                       + " agains null condition: '" + value + '\'');

               return false;
           }

            c.popStackValues (1);
        }

        c.flushStackUpdates ();
        return true;
    }

    boolean checkBasicTypes () throws ConnException
    {
        System.out.println ("Testing value types...");

        ValueType[] types = new ValueType[17];

        types[0] = ValueType.create (ValueType.BOOL);
        types[1] = ValueType.create (ValueType.CHAR);
        types[2] = ValueType.create (ValueType.DATE);
        types[3] = ValueType.create (ValueType.DATETIME);
        types[4] = ValueType.create (ValueType.HIRESTIME);
        types[5] = ValueType.create (ValueType.INT8);
        types[6] = ValueType.create (ValueType.INT16);
        types[7] = ValueType.create (ValueType.INT32);
        types[8] = ValueType.create (ValueType.INT64);
        types[9] = ValueType.create (ValueType.UINT8);
        types[10] = ValueType.create (ValueType.UINT16);
        types[11] = ValueType.create (ValueType.UINT32);
        types[12] = ValueType.create (ValueType.UINT64);
        types[13] = ValueType.create (ValueType.REAL);
        types[14] = ValueType.create (ValueType.RICHREAL);
        types[15] = ValueType.create (ValueType.TEXT);
        types[16] = ValueType.create (ValueType.TYPE_NOTSET);

        for (int i = 0; i < types.length; ++i)
        {
            for (int j = 0; j < types.length; ++j)
            {
                if (i == j)
                {
                    if ( ! types[i].equals(types[j]))
                        return false;
                }
                else
                {
                    if (types[i].equals (types[j]))
                        return false;
                }
            }
        }

        return true;
    }

    TestValue[] testValues;
}
