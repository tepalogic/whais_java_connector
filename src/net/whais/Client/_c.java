package net.whais.Client;

class _c
{
    final static int CLIENT_VERSIONS             = 0x00000001;
    
    final static int MIN_FRAME_SIZE              = 512;
    final static int MAX_FRAME_SIZE              = 65535;
    final static int DEFAULT_FRAME_SIZE          = 4096;

    final static byte FRAME_SIZE_OFF             = 0x00;
    final static byte FRAME_TYPE_OFF             = 0x02;
    final static byte FRAME_ENCTYPE_OFF          = 0x03;
    final static byte FRAME_ID_OFF               = 0x04;
    final static byte FRAME_HDR_SIZE             = 0x08;
    
    final static byte FRAME_TYPE_NORMAL          = (byte) 0x00;
    final static byte FRAME_TYPE_AUTH_CLNT       = (byte) 0x01;
    final static byte FRAME_TYPE_AUTH_CLNT_RSP   = (byte) 0x02;
    final static byte FRAME_TYPE_COMM_NOSYNC     = (byte) 0xFD;
    final static byte FRAME_TYPE_TIMEOUT         = (byte) 0xFE;
    final static byte FRAME_TYPE_SERV_BUSY       = (byte) 0xFF;

    final static byte FRAME_ENCTYPE_PLAIN        = 0x01;
    final static byte FRAME_ENCTYPE_3K           = 0x02;

    final static byte ENC_3K_FIRST_KING_OFF      = 0x00;
    final static byte ENC_3K_SECOND_KING_OFF     = 0x04;
    final static byte ENC_3K_PLAIN_SIZE_OFF      = 0x08;
    final static byte ENC_3K_SPARE_OFF           = 0x0A;
    final static byte ENC_3K_HDR_SIZE            = 0x0C;

    final static byte PLAIN_CLNT_COOKIE_OFF      = 0x00;
    final static byte PLAIN_SERV_COOKIE_OFF      = 0x04;
    final static byte PLAIN_TYPE_OFF             = 0x08;
    final static byte PLAIN_CRC_OFF              = 0x0A;
    final static byte PLAIN_HDR_SIZE             = 0x0C;
    
    final static byte FRAME_AUTH_VER_OFF         = 0x00;
    final static byte FRAME_AUTH_SIZE_OFF        = 0x04;
    final static byte FRAME_AUTH_SPARE_1_OFF     = 0x06;
    final static byte FRAME_AUTH_ENC_OFF         = 0x08;
    final static byte FRAME_AUTH_SPARE_2_OFF     = 0x09;
    final static byte FRAME_AUTH_SIZE            = 0x0C;

    final static byte FRAME_AUTH_RSP_VER_OFF     = 0x00;
    final static byte FRAME_AUTH_RSP_USR_OFF     = 0x04;
    final static byte FRAME_AUTH_RSP_ENC_OFF     = 0x05;
    final static byte FRAME_AUTH_RSP_SPARE_OFF   = 0x06;
    final static byte FRAME_AUTH_RSP_FIXED_SIZE  = 0x08;
    
    
    final static short ADMIN_CMD_BASE            = 0x0000;
    final static short USER_CMD_BASE             = 0x1000;
    
    final static short CMD_INVALID               = ADMIN_CMD_BASE;
    final static short CMD_INVALID_RSP           = CMD_INVALID + 1;

    final static short CMD_LIST_GLOBALS          = CMD_INVALID_RSP + 1;
    final static short CMD_LIST_GLOBALS_RSP      = CMD_LIST_GLOBALS + 1;

    final static short CMD_LIST_PROCEDURE        = CMD_LIST_GLOBALS_RSP + 1;
    final static short CMD_LIST_PROCEDURE_RSP    = CMD_LIST_PROCEDURE + 1;

    final static short CMD_DESC_PROC_PARAM       = CMD_LIST_PROCEDURE_RSP + 1;
    final static short CMD_DESC_PROC_PARAM_RSP   = CMD_DESC_PROC_PARAM + 1;

    final static short CMD_CLOSE_CONN            = USER_CMD_BASE;
    final static short CMD_CLOSE_CONN_RSP        = CMD_CLOSE_CONN + 1;

    final static short CMD_GLOBAL_DESC           = CMD_CLOSE_CONN_RSP + 1;
    final static short CMD_GLOBAL_DESC_RSP       = CMD_GLOBAL_DESC + 1;


    final static short CMD_READ_STACK            = CMD_GLOBAL_DESC_RSP + 1;
    final static short CMD_READ_STACK_RSP        = CMD_READ_STACK + 1;

    final static short CMD_UPDATE_STACK          = CMD_READ_STACK_RSP + 1;
    final static short CMD_UPDATE_STACK_RSP      = CMD_UPDATE_STACK + 1;

    final static byte CMD_UPDATE_FUNC_POP        = 1;
    final static byte CMD_UPDATE_FUNC_PUSH       = 2;
    final static byte CMD_UPDATE_FUNC_CHTOP      = 3;

    final static short CMD_EXEC_PROC             = CMD_UPDATE_STACK_RSP + 1;
    final static short CMD_EXEC_PROC_RSP         = CMD_EXEC_PROC + 1;

/* Ping command */
    final static short CMD_PING_SERVER           = CMD_EXEC_PROC_RSP + 1;
    final static short CMD_PING_SERVER_RSP       = CMD_PING_SERVER + 1;
}
