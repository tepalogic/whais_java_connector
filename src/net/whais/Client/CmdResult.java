package net.whais.Client;

/**
 * This class is used to hold and interpret the codes resulted using the
 * APIs from this package.
 *
 * @version 1.0
 */
public final class CmdResult
{
    /**
     * Get a description for a result code.
     * @param resultCode
     *            The result code to get a representation for it.
     * @return
     *            A string description corresponding to the default result.
     */
    final static public String translateResultCode( final int resultCode)
    {
        String s = "(Unknown error code.)";

        switch (resultCode) {
        case CmdResult.OK:
            s = "No errors encountered.";
            break;

        case CmdResult.INVALID_ARGS:
            s = "Invalid arguments.";
            break;

        case CmdResult.OP_NOTSUPP:
            s = "Operation not supported";
            break;

        case CmdResult.OP_NOTPERMITED:
            s = "Operation not permitted.";
            break;

        case CmdResult.DROPPED:
            s = "Connection dropped by peer.";
            break;

        case CmdResult.PROTOCOL_NOTSUPP:
            s = "No suitable protocol to communicate with the server.";
            break;

        case CmdResult.ENCTYPE_NOTSUPP:
            s = "Could not agree on a supported encryption type.";
            break;

        case CmdResult.UNEXPECTED_FRAME:
            s = "Unexpected communication frame received.";
            break;

        case CmdResult.INVALID_FRAME:
            s = "A communication frame with invalid content received.";
            break;

        case CmdResult.COMM_OUT_OF_SYNC:
            s = "Communication with peer is out of sync.";
            break;

        case CmdResult.LARGE_ARGS:
            s = "Size of the request arguments is big.";
            break;

        case CmdResult.LARGE_RESPONSE:
            s = "Size of the request's response is too big.";
            break;

        case CmdResult.CONNECTION_TIMEOUT:
            s = "Peer is taking to long time to respond.";
            break;

        case CmdResult.SERVER_BUSY:
            s = "The server rejected our connection request because is too busy.";
            break;

        case CmdResult.INCOMPLETE_CMD:
            s = "The requested command could not be handled. The previous one should be completed first.";
            break;

        case CmdResult.INVALID_ARRAY_OFF:
            s = "An invalid array index was used.";
            break;

        case CmdResult.INVALID_TEXT_OFF:
            s = "An invalid text index was used.";
            break;

        case CmdResult.INVALID_ROW:
            s = "An invalid row index was used.";
            break;

        case CmdResult.INVALID_FIELD:
            s = "An invalid table field was used.";
            break;

        case CmdResult.TYPE_MISMATCH:
            s = "The request command cannot be completed due to an unexpected type of a value.";
            break;

        case CmdResult.PROC_NOTFOUND:
            s = "Procedure not found.";
            break;

        case CmdResult.PROC_RUNTIME_ERR:
            s = "Procedure execution has failed because of a runtime error.";
            break;

        case CmdResult.GENERAL_ERR:
            s = "Unexpected internal error.";
            break;
            
        case CmdResult.VALUE_OUT_OF_RANGE:
            s = "A value cannot be created because the specified type cannot hold it.";

        default:
            assert false;
        }

        return s;
    }

    final static public int OK = 0;
    final static public int INVALID_ARGS = 1;
    final static public int OP_NOTSUPP = 2;
    final static public int OP_NOTPERMITED = 3;
    final static public int DROPPED = 4;
    final static public int PROTOCOL_NOTSUPP = 5;
    final static public int ENCTYPE_NOTSUPP = 6;
    final static public int UNEXPECTED_FRAME = 7;
    final static public int INVALID_FRAME = 8;
    final static public int COMM_OUT_OF_SYNC = 9;
    final static public int LARGE_ARGS = 10;
    final static public int LARGE_RESPONSE = 11;
    final static public int CONNECTION_TIMEOUT = 12;
    final static public int SERVER_BUSY = 13;
    final static public int INCOMPLETE_CMD = 14;
    final static public int INVALID_ARRAY_OFF = 15;
    final static public int INVALID_TEXT_OFF = 16;
    final static public int INVALID_ROW = 17;
    final static public int INVALID_FIELD = 18;
    final static public int TYPE_MISMATCH = 19;
    final static public int PROC_NOTFOUND = 20;
    final static public int PROC_RUNTIME_ERR = 21;
    final static public int VALUE_OUT_OF_RANGE = 22;
    final static public int GENERAL_ERR = 0x0FFF;
}
