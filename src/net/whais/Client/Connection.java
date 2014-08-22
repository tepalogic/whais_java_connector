package net.whais.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


/**
 * Represents a connection to a Whais data server.
 *
 * The class Connection is used to instantiate a communication channel to a
 * Whais sever and to manage the data it holds.
 *
 * @version 1.0
 *
 */
public class Connection {

    /**
     * Instantiate a communication channel.
     *
     * @param host              The host where the Whais server resides.
     * @param port              The port where the server listens for TCP/IP connections.
     * @param database          The name of the database to use.
     * @param key               The authentication data. Based on the used authentication option this could be a
     *                          a password or cryptographic key.
     * @param userId            Set this to {@link #ADMIN} to connect as a database administrator or to {@link #USER} as a regular user.
     * @param maxFrameSize      The maximum communication frame size the client may use. Based on client's needs
     *                          or network limitations the client has a possibility to hint the server what frame
     *                          size to use. This value should be set from 512 to 65535 (inclusive).
     *
     * @throws ConnException
     * @throws IOException
     *
     * @since 1.0
     */
    public Connection (String       host,
                       String       port,
                       String       database,
                       byte[]       key,
                       byte         userId,
                       int          maxFrameSize) throws IOException
    {
        final InetAddress[] addresses = InetAddress.getAllByName (host);

        //Try every address until we manage to connect.
        Throwable t = null;
        Socket s = null;
        for (InetAddress address : addresses) {
            try {
                 s = new Socket (address, Integer.parseInt (port));
                break; //We found a good one
            } catch (IOException e) {
                t = e;
            }
        }

        //Throw the last exception in case we fund nothing.
        if (s == null)
            throw new IOException (t);

        this.userId = userId;
        this.frame = new CommunicationFrame (s, userId, maxFrameSize, database, key);
    }

    /**
     * Constructor wrapper to instantiate a communication channel with a default frame size set to 65535 bytes.
     *
     * @since 1.0
     */
    public Connection (String       host,
                       String       port,
                       String       database,
                       byte[]       key,
                       byte         userId) throws IOException
    {
        this (host, port, database, key, userId, _c.DEFAULT_FRAME_SIZE);
    }

    /**
     * Constructor wrapper to instantiate a communication channel with a default frame size set to 65535 bytes and
     * to automatically convert the password from a string to a byte array.
     *
     * @since 1.0
     */
    public Connection (String       host,
                       String       port,
                       String       database,
                       String       password,
                       byte         userId) throws IOException
    {
        this (host,
              port,
              database,
              password.getBytes (StandardCharsets.UTF_8),
              userId);
    }

    /**
     * Constructor wrapper to automatically convert the password from a string to a byte array.
     *
     * @since 1.0
     */
    public Connection (String   host,
                       String   port,
                       String   database,
                       String   password,
                       byte     userId,
                       int      maxFrameSize) throws IOException
    {
        this (host,
              port,
              database,
              password.getBytes (StandardCharsets.UTF_8),
              userId,
              maxFrameSize);

    }

    /**
     * Wrapped for close (true).
     *
     * @since 1.0
     */
    public void close ()
    {
        this.close (false);
    }

    /**
     * Ends the communication session with the Whais servers.
     * @param discardCmds   Set to true to attempt to flush existent cached commands commands to to false to discard them.
     *
     * @since 1.0
     */
    public void close (boolean discardCmds)
    {
        if (this.frame == null)
            return ;

        try {
            if (discardCmds)
                this.frame.discardPendingCommand ();
            else
                this.frame.flushPendingCommand ();

            this.frame.sendCommand(_c.CMD_CLOSE_CONN, false);
        } catch (IOException e) {
            //Do nothing here ... just ignore it.
        } finally {
            this.frame.Close ();
            this.frame = null;
        }
    }

    /*
     * Make sure to free the communication channel if it was taken.
     *
     * @since 1.0
     *
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize () throws Throwable
    {
        super.finalize ();
        this.close (true);
    }

    /**
     *  Check if the connection is healthy or signal the server to keep the this connection open.
     *
     *  @throws ConnException
     *  @throws IOException
     *
     *  @see ConnException
     *
     *  @since 1.0
     */
    public void pingServer () throws IOException
    {
        if (this.frame.hasPendingCommands ())
            throw new ConnException (CmdResult.INCOMPLETE_CMD);

        this.frame.sendCommand (_c.CMD_PING_SERVER);

        ByteBuffer buffer = this.frame.getCmdBuffer ();

        int cmdResult = buffer.getInt ();

        this.frame.discardPendingCommand ();
        if (cmdResult != CmdResult.OK)
            throw new ConnException (cmdResult);
    }

    /**
     * Retrieve the names of the global values defined in the connected database namespace.
     *
     * <br/>
     * This {@link Connection} must be initiated by an administrator to successfully complete this method call.
     *
     * @since 1.0
     *
     * @return An array holding the names of the global names defined by the connected database.
     *         If no names are available this will return an array with 0 elements.
     *
     * @throws ConnException
     * @throws IOException
     */
    public String[] retrieveGlobalNames () throws IOException
    {
        String[] result = Connection.emptyList;

        if (this.userId != ADMIN)
            throw new ConnException (CmdResult.OP_NOTPERMITED);

        else if (this.frame.hasPendingCommands ())
            throw new ConnException (CmdResult.INCOMPLETE_CMD);

        ByteBuffer buffer = null;
        int glbIndex = 0, glbsCount = 0;
        do
        {
            if ((glbIndex == 0)
                || (buffer.position () >= this.frame.getCmdLastPosition ()))
            {
                this.frame.discardPendingCommand ();
                buffer = this.frame.getCmdBuffer ();

                buffer.putInt (glbIndex);
                this.frame.markBufferPositionValid ();
                this.frame.sendCommand(_c.CMD_LIST_GLOBALS);
                buffer = this.frame.getCmdBuffer ();

                final int cmdResult = buffer.getInt ();
                if (cmdResult != CmdResult.OK)
                    throw new ConnException (cmdResult);

                if (glbIndex == 0)
                {
                    glbsCount = buffer.getInt ();
                    if (glbsCount != 0)
                        result = new String[glbsCount];
                    else
                        return null;

                    buffer.position (buffer.position () - 4);
                }

                if ((buffer.getInt () != glbsCount)
                    || (buffer.getInt () != glbIndex))
                {
                    throw new ConnException (CmdResult.GENERAL_ERR, "Unexpected answer format received from server.");
                }
            }

            final int startOffset = buffer.position ();
            while (buffer.get () != 0)
                ; //Just let the buffer's position advance.
            final int endOffset = buffer.position () - 1;

            assert startOffset < endOffset;

            result[glbIndex] = new String (buffer.array (),
                                           startOffset,
                                           endOffset - startOffset,
                                           StandardCharsets.UTF_8);
        } while (++glbIndex < glbsCount);

        return result;
    }

    /**
     * Describe a global value defined in the connected database namespace.
     *
     * <br/>
     * This {@link Connection} must be initiated by an administrator to successfully complete this method call.
     *
     * @param name      The name of the global value.
     * @return          A class the represent the type of the specified value.
     *
     * @throws ConnException
     * @throws IOException
     *
     * @since 1.0
     */
    public ValueType describeGlobal (String name) throws IOException
    {
        if (this.frame.hasPendingCommands ())
            throw new ConnException (CmdResult.INCOMPLETE_CMD);

        else if (this.userId != ADMIN)
            throw new ConnException (CmdResult.OP_NOTPERMITED);

        else if ((name == null) || (name.length () == 0))
            throw new ConnException (CmdResult.INVALID_ARGS);

       return this.internalDescribeValue(name);
    }

    public String[] retrieveProceduresNames () throws IOException
    {
        String[] result = null;

        if (this.userId != ADMIN)
            throw new ConnException (CmdResult.OP_NOTPERMITED);

        else if (this.frame.hasPendingCommands ())
            throw new ConnException (CmdResult.INCOMPLETE_CMD);

        ByteBuffer buffer = null;
        int procIndex = 0, procsCount = 0;
        do
        {
            if ((procIndex == 0)
                || (buffer.position () >= this.frame.getCmdLastPosition ()))
            {
                this.frame.discardPendingCommand ();
                buffer = this.frame.getCmdBuffer ();

                buffer.putInt (procIndex);
                this.frame.markBufferPositionValid ();
                this.frame.sendCommand(_c.CMD_LIST_PROCEDURE);
                buffer = this.frame.getCmdBuffer ();

                final int cmdResult = buffer.getInt ();
                if (cmdResult != CmdResult.OK)
                    throw new ConnException (cmdResult);

                if (procIndex == 0)
                {
                    procsCount = buffer.getInt ();
                    if (procsCount != 0)
                        result = new String[procsCount];
                    else
                        return null;

                    buffer.position (buffer.position () - 4);
                }

                if ((buffer.getInt () != procsCount)
                    || (buffer.getInt () != procIndex))
                {
                    throw new ConnException (CmdResult.GENERAL_ERR, "Unexpected answer format received from server.");
                }
            }

            final int startOffset = buffer.position ();
            while (buffer.get () != 0)
                ; //Just let the buffer's position advance.
            final int endOffset = buffer.position () - 1;

            assert startOffset < endOffset;;

            result[procIndex] = new String (buffer.array (),
                                            startOffset,
                                            endOffset - startOffset,
                                            StandardCharsets.UTF_8);
        } while (++procIndex < procsCount);

        return result;
    }

	public ProcedureParameters describeProcedure (String name) throws IOException
	{
        if (this.frame.hasPendingCommands ())
            throw new ConnException (CmdResult.INCOMPLETE_CMD);

        else if (this.userId != ADMIN)
            throw new ConnException (CmdResult.OP_NOTPERMITED);

        else if ((name == null) || (name.length () == 0))
            throw new ConnException (CmdResult.INVALID_ARGS);

        this.frame.discardPendingCommand ();
        ByteBuffer b = this.frame.getCmdBuffer ();
        byte[] n = name.getBytes (StandardCharsets.UTF_8);
        if (this.frame.availableCmdSize () < 2 + 2 + n.length + 1)
            throw new ConnException(CmdResult.LARGE_ARGS, "Procedure name is too long for the configured communication frame size.");

        ValueType[] paramTypes = null;
        int param = 0, paramsCount = 0;
        do
        {
            if ((paramTypes == null) || (b.position() >= this.frame.getCmdLastPosition ()))
            {
                this.frame.discardPendingCommand ();
                b = this.frame.getCmdBuffer ();

                b.putShort ((short) param);
                b.putShort ((short) 0);
                b.put (n);
                b.put ((byte) 0);
                this.frame.markBufferPositionValid ();
                this.frame.sendCommand (_c.CMD_DESC_PROC_PARAM);
                b = this.frame.getCmdBuffer ();
                int cmdRsp = b.getInt ();
                if (cmdRsp != CmdResult.OK)
                    throw new ConnException (cmdRsp);

                b.position (b.position () + n.length + 1);
                if (paramTypes == null)
                {
                    paramsCount  = b.getShort ();
                    paramsCount &= 0x0000FFFF;

                    if (paramsCount <= 0)
                        throw new ConnException (CmdResult.GENERAL_ERR, "Unexpected answer from server.");

                    paramTypes = new ValueType[paramsCount];
                    b.position (b.position () + 2);
                }
                else
                    b.position (b.position () + 4);
            }

            short ptype = b.getShort ();
            if (ValueType.isTable (ptype))
            {
                int fieldsCount = b.getShort ();
                TableFieldType[] fields = null;

                if (fieldsCount > 0)
                {
                    fields = new TableFieldType[fieldsCount];

                    for (int i = 0; i < fieldsCount; ++i)
                    {
                        int startOff = b.position ();
                        while (b.get() != 0)
                                ; //Let the buffer's position advance
                        int endOff = b.position ();

                        final short fieldType = b.getShort ();
                        fields[i] = new TableFieldType (new String (b.array (),
                                                                    startOff,
                                                                    endOff - startOff,
                                                                    StandardCharsets.UTF_8),
                                                        ValueType.create (fieldType));
                    }
                }

                if ((fields != null) && (fields.length > 0))
                        paramTypes[param] = ValueType.create (fields);

                paramTypes[param] = ValueType.create (ptype);
            }
            else
                paramTypes[param] = ValueType.create (ptype);

            assert paramTypes[param] != null;
        }
        while (++param < paramsCount);

        assert paramTypes.length > 0;

        return new ProcedureParameters (paramTypes);
	}


	public ValueType describeStackTop () throws IOException
	{
       if (this.frame.hasPendingCommands ())
            throw new ConnException (CmdResult.INCOMPLETE_CMD);

	    return this.internalDescribeValue ("");
	}

	public void pushStackValue (ValueType type) throws ConnException
	{
	    if (this.frame.hasPendingCommands ()
	        && (this.frame.getPendingCommand () != _c.CMD_UPDATE_STACK))
	    {
	        throw new ConnException (CmdResult.INCOMPLETE_CMD);
	    }

	    if (type.isField ())
	        throw new ConnException (CmdResult.INVALID_ARGS, "Field values cannot be pushed on stack.");

	    if (this.frame.availableCmdSize () < 3)
        {
	        assert this.frame.hasPendingCommands();

	        this.frame.flushPendingCommand ();
	        this.pushStackValue (type);
	        return ;
        }

	    ByteBuffer buffer = this.frame.getCmdBuffer ();
	    buffer.position (this.frame.getCmdLastPosition ());

        buffer.put (_c.CMD_UPDATE_FUNC_PUSH);
        buffer.putShort (type.getTypeId ());

	    if (type.isTable () && (type.getFields() != null))
	    {
	        for (TableFieldType f : type.getFields ())
	        {
	            final byte[] utf8FieldName = f.getName ().getBytes (StandardCharsets.UTF_8);
	            final int spaceRequired = utf8FieldName.length + 1 + 2;

	            if (buffer.position () + spaceRequired >= buffer.capacity ())
	            {
	                if ( ! this.frame.hasPendingCommands ())
	                {
	                    throw new ConnException (CmdResult.LARGE_ARGS,
	                                             "The communication frame size does not  to allow the push of this table value.");
	                }

	                this.frame.flushPendingCommand ();
	                this.pushStackValue (type);
	                return ;
	            }

	            buffer.put (utf8FieldName);
	            buffer.put ((byte) 0);
	            buffer.putShort (f.getType ().getTypeId ());
	        }
	    }
	    this.frame.markBufferPositionValid ();
	    this.frame.setPendingCommand(_c.CMD_UPDATE_STACK);
	}

	public void popStackValues (int count) throws ConnException
	{
       if (this.frame.hasPendingCommands ()
           && (this.frame.getPendingCommand () != _c.CMD_UPDATE_STACK))
       {
           throw new ConnException (CmdResult.INCOMPLETE_CMD);
       }

       if (this.frame.availableCmdSize () < 5)
       {
           assert this.frame.hasPendingCommands();

           this.frame.flushPendingCommand ();
           this.popStackValues (count);
           return ;
       }

       ByteBuffer buffer = this.frame.getCmdBuffer ();
       buffer.position (this.frame.getCmdLastPosition ());

       buffer.put (_c.CMD_UPDATE_FUNC_POP);
       buffer.putInt (count);

       this.frame.markBufferPositionValid ();
       this.frame.setPendingCommand(_c.CMD_UPDATE_STACK);
	}

	public void popStackValues () throws ConnException
	{
	    this.popStackValues (-1);
	}

	public void updateStackTop (Value val) throws ConnException
	{
	    if (this.frame.hasPendingCommands ()
	        && (this.frame.getPendingCommand() != _c.CMD_UPDATE_STACK))
	    {
	        throw new ConnException (CmdResult.INCOMPLETE_CMD);
	    }

	    if (val.type ().equals (ValueType.textType ()))
	        this.updateStackTopText (val.toString (), 0);

	    else if (val.type ().isBasic ())
	        this.updateStackTopBasic (val);

	    else if (val.type ().isArray ())
	        this.updateStackTopBasicArray (((ArrayValue) val).toArray (), 0);

	    else if (val.type ().isTable ())
	    {
	        if (val.isNull())
	            return ;

	        final TableValue table = (TableValue) val;
	        final int rowsCount = table.getRowsCount ();
	        for (int row = 0; row < rowsCount; ++row)
	        {
	            for (TableFieldType fields : table.getFields() )
	            {
	                Value v = table.get (fields.getName (), row);
	                if ((v == null) || v.isNull ())
	                    continue;

	                this.updateStackTop (v, fields.getName(), row);
	            }
	        }
	    }
        else
            throw new ConnException (CmdResult.INVALID_ARGS, "Unexpected value type to update the top of stack.");
	}

	public void updateStackTop (Value val, String fieldName, long row) throws ConnException
	{
	    if (this.frame.hasPendingCommands ()
	        && (this.frame.getPendingCommand() != _c.CMD_UPDATE_STACK))
	    {
	        throw new ConnException (CmdResult.INCOMPLETE_CMD);
	    }
	    else if ((fieldName == null) || (fieldName.length () == 0))
	        throw new  ConnException (CmdResult.INVALID_FIELD);

	    else if (row < 0)
	        throw new ConnException (CmdResult.INVALID_ROW);


       if (val.type ().equals (ValueType.textType ()))
            this.updateStackTopText (val.toString (), 0, fieldName, row);

        else if (val.type ().isBasic ())
            this.updateStackTopBasic (val, fieldName, row);

        else if (val.type ().isArray ())
            this.updateStackTopBasicArray (((ArrayValue) val).toArray (), 0, fieldName, row);

        else
            throw new ConnException (CmdResult.INVALID_ARGS, "Unexpected value type to update the top of stack.");
	}

	public long retrieveStackTopRowsCount () throws IOException
	{
        if (this.frame.hasPendingCommands ())
            throw new ConnException(CmdResult.INCOMPLETE_CMD);

        if (this.frame.getCachedResponse () != _c.CMD_INVALID_RSP)
            this.refreshReadCache (IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, IGNORE_OFFSET);

        ByteBuffer b = this.frame.getCmdBuffer ();
        b.position (b.position () + 4);

        short type = b.getShort ();
        if ( ! ValueType.isTable (type) && ! ValueType.isField (type))
            return -1;

        return b.getLong ();
	}

    public Value retrieveStackTop (final long row) throws IOException
    {
        if (this.frame.hasPendingCommands ())
            throw new ConnException(CmdResult.INCOMPLETE_CMD);

        if (this.frame.getCachedResponse () != _c.CMD_INVALID_RSP)
            this.refreshReadCache (IGNORE_FIELD, row, IGNORE_OFFSET, IGNORE_OFFSET);

        ByteBuffer b = this.frame.getCmdBuffer ();
        b.position (b.position () + 4);

        short type = b.getShort ();
        if ( ValueType.isTable (type))
        {
            throw new ConnException (CmdResult.INVALID_FIELD);
        }
        else if ( ! ValueType.isField (type))
            throw new ConnException (CmdResult.INVALID_ROW);

        if ((b = this.skipUntilCachedRow(row)) == null)
        {
            this.refreshReadCache (IGNORE_FIELD, row, IGNORE_OFFSET, IGNORE_OFFSET);
            if ((b = this.skipUntilCachedRow(row)) == null)
                throw new ConnException (CmdResult.GENERAL_ERR);
        }

        if (ValueType.isArray (type))
        {
            long elementsCount = b.getLong ();
            type = (short) ValueType.getBaseType (type);

            ArrayValue result = (ArrayValue) Value.create (ValueType.create (type | ValueType.ARRAY_MASK));

            if (elementsCount == 0)
                return result;

            long arrayOffset = b.getLong ();
            if (arrayOffset != 0)
            {
                arrayOffset = 0;
                this.refreshReadCache (IGNORE_FIELD, row, 0, IGNORE_OFFSET);
                if ((b = this.skipUntilCachedRow(row)) == null)
                    throw new ConnException (CmdResult.GENERAL_ERR);

                if ((elementsCount != b.getLong ())
                    || (arrayOffset != b.getLong ()))
                {
                    throw new ConnException (CmdResult.GENERAL_ERR);
                }

                while (arrayOffset < elementsCount)
                {
                    if (b.position() >= b.capacity ())
                    {
                        this.refreshReadCache (IGNORE_FIELD, row, arrayOffset, IGNORE_OFFSET);
                        if ((b = this.skipUntilCachedRow(row)) == null)
                            throw new ConnException (CmdResult.GENERAL_ERR);

                        if ((elementsCount != b.getLong ())
                            || (arrayOffset != b.getLong ()))
                        {
                            throw new ConnException (CmdResult.GENERAL_ERR);
                        }
                    }
                    result.add (Value.create (b.array(), b.position (), ValueType.create ( type)));

                    while (b.get () != 0)
                        ; //Just let the buffer advance

                    ++arrayOffset;
                }
            }
            return result;
        }
        else if (ValueType.getBaseType (type) == ValueType.TEXT)
        {
            long charsCount = b.getLong ();

            if (charsCount == 0)
                return Value.create (ValueType.textType ());

            long charOffset = b.getLong ();
            String text = "";
            if (charOffset != 0)
            {
                charOffset = 0;
                this.refreshReadCache (IGNORE_FIELD, row, IGNORE_OFFSET, 0);
                if ((b = this.skipUntilCachedRow (row)) == null)
                    throw new ConnException (CmdResult.GENERAL_ERR);

                if ((charsCount != b.getLong ())
                    || (charOffset != b.getLong ()))
                {
                    throw new ConnException (CmdResult.GENERAL_ERR);
                }

                while (charOffset < charsCount)
                {
                    if (b.position() >= b.capacity ())
                    {
                        this.refreshReadCache (IGNORE_FIELD, row, IGNORE_OFFSET, charOffset);
                        if ((b = this.skipUntilCachedRow (row)) == null)
                            throw new ConnException (CmdResult.GENERAL_ERR);

                        if ((charsCount != b.getLong ())
                            || (charOffset != b.getLong ()))
                        {
                            throw new ConnException (CmdResult.GENERAL_ERR);
                        }
                    }

                    int startOffset = b.position ();
                    while (b.get () != 0)
                        ; //Just let the buffer position to advance
                    int endOffset = b.position ();

                    String t = new String (b.array (),
                                           startOffset,
                                           endOffset - startOffset,
                                           StandardCharsets.UTF_8);
                    charOffset += t.codePointCount (0, t.length ());
                    text += t;
                }
            }
            return Value.create (text, ValueType.textType ());
        }

        return Value.create (b.array (),
                             b.position (),
                             ValueType.create (ValueType.getBaseType (type)));
    }

	public Value retrieveStackTop (final String field, final long row) throws IOException
	{
	    if (field == null || field.equals (IGNORE_FIELD))
	        return this.retrieveStackTop (row);

	    if (this.frame.hasPendingCommands ())
	        throw new ConnException(CmdResult.INCOMPLETE_CMD);

	    if (this.frame.getCachedResponse () != _c.CMD_INVALID_RSP)
	        this.refreshReadCache (field, row, IGNORE_OFFSET, IGNORE_OFFSET);

	    ByteBuffer b = this.frame.getCmdBuffer ();
	    b.position (b.position () + 4);

	    if ( ! ValueType.isTable (b.getShort ()))
	        throw new ConnException (CmdResult.INVALID_FIELD, "The server stack value is not a table.");

	    if ((b = this.skipUntilFieldValue(field, row)) == null)
	    {
	        this.refreshReadCache (field, row, IGNORE_OFFSET, IGNORE_OFFSET);
	        if ((b = this.skipUntilFieldValue(field, row)) == null)
	            throw new ConnException (CmdResult.GENERAL_ERR);
	    }

	    ValueType type = ValueType.create (b.getShort ());
	    if (type.isArray ())
	    {
	        long elementsCount = b.getLong ();
	        ArrayValue result = (ArrayValue) Value.create (type);

	        type = ValueType.create (type.getBaseType ());

	        if (elementsCount == 0)
	            return result;

	        long arrayOffset = b.getLong ();
	        if (arrayOffset != 0)
	        {
	            arrayOffset = 0;
	            this.refreshReadCache (field, row, 0, IGNORE_OFFSET);
	            if ((b = this.skipUntilFieldValue(field, row)) == null)
	                throw new ConnException (CmdResult.GENERAL_ERR);

	            if ((type.getTypeId () != b.getShort ())
	                || (elementsCount != b.getLong ())
	                || (arrayOffset != b.getLong ()))
	            {
	                throw new ConnException (CmdResult.GENERAL_ERR);
	            }

	            while (arrayOffset < elementsCount)
	            {
	                if (b.position() >= b.capacity ())
	                {
	                    this.refreshReadCache (field, row, arrayOffset, IGNORE_OFFSET);
	                    if ((b = this.skipUntilFieldValue(field, row)) == null)
	                        throw new ConnException (CmdResult.GENERAL_ERR);

	                    if ((type.getTypeId () != b.getShort ())
	                        || (elementsCount != b.getLong ())
	                        || (arrayOffset != b.getLong ()))
	                    {
	                        throw new ConnException (CmdResult.GENERAL_ERR);
	                    }
	                }
	                result.add (Value.create (b.array(), b.position (), type));

                    while (b.get () != 0)
                        ; //Just let the buffer advance

	                ++arrayOffset;
	            }
	        }
	        return result;
	    }
	    else if (type.getBaseType () == ValueType.TEXT)
	    {
	        type = ValueType.textType ();
            long charsCount = b.getLong ();

            if (charsCount == 0)
                return Value.create (type);

            long charOffset = b.getLong ();
            String text = "";
            if (charOffset != 0)
            {
                charOffset = 0;
                this.refreshReadCache (field, row, IGNORE_OFFSET, 0);
                if ((b = this.skipUntilFieldValue(field, row)) == null)
                    throw new ConnException (CmdResult.GENERAL_ERR);

                if ((type.getTypeId () != b.getShort ())
                    || (charsCount != b.getLong ())
                    || (charOffset != b.getLong ()))
                {
                    throw new ConnException (CmdResult.GENERAL_ERR);
                }

                while (charOffset < charsCount)
                {
                    if (b.position() >= b.capacity ())
                    {
                        this.refreshReadCache (field, row, IGNORE_OFFSET, charOffset);
                        if ((b = this.skipUntilFieldValue(field, row)) == null)
                            throw new ConnException (CmdResult.GENERAL_ERR);

                        if ((type.getTypeId () != b.getShort ())
                            || (charsCount != b.getLong ())
                            || (charOffset != b.getLong ()))
                        {
                            throw new ConnException (CmdResult.GENERAL_ERR);
                        }
                    }

                    int startOffset = b.position ();
                    while (b.get () != 0)
                        ; //Just let the buffer position to advance
                    int endOffset = b.position ();

                    String t = new String (b.array (),
                                           startOffset,
                                           endOffset - startOffset,
                                           StandardCharsets.UTF_8);
                    charOffset += t.codePointCount (0, t.length ());
                    text += t;
                }
            }
            return Value.create (text, type);
	    }

	    type = ValueType.create (type.getBaseType ());
	    return Value.create (b.array (), b.position (), type);
	}

	public Value retrieveStackTop () throws IOException
	{
	    if (this.frame.hasPendingCommands ())
	        throw new ConnException (CmdResult.INCOMPLETE_CMD);

	    this.refreshReadCache (IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, IGNORE_OFFSET);
	    ByteBuffer buffer = this.frame.getCmdBuffer ();
	    buffer.position (buffer.position () + 4); //We already know the query was successful.

	    ValueType type = ValueType.create (buffer.getShort ());
	    if (type.isTable () || type.isField ())
	    {
	        throw new ConnException (CmdResult.LARGE_RESPONSE,
	                          "The server stack value is either a table or a field. " +
	                          "Those values may be read row by by and field by field.");
	    }
	    else if (type.isArray ())
	    {
	        ValueType baseType = ValueType.create (type.getBaseType ());
	        ArrayValue result = (ArrayValue) Value.create (type);

	        final long elementsCount = buffer.getLong ();
	        long currentOffset = buffer.getLong ();

	        while (currentOffset < elementsCount)
	        {
	            if (buffer.position ()  >= buffer.capacity())
                {
                    this.refreshReadCache (IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, currentOffset);
                    buffer = this.frame.getCmdBuffer ();
                    buffer.position (buffer.position () + 4); //We already know the query was successful.

                    if ((elementsCount != buffer.getLong ())
                        || (currentOffset != buffer.getLong ()))
                    {
                        throw new ConnException (CmdResult.GENERAL_ERR, "Unexpected response frame format.");
                    }
                }

                final int startOffset = buffer.position ();

                while (buffer.get () != 0)
                    ; //Just let the buffer's position advance.

                result.add (Value.create (buffer.array(), startOffset, baseType));
                ++currentOffset;
	        }

	        return result;
	    }
	    else if (type.equals (ValueType.textType ()))
	    {
	        final long charsCount = buffer.getLong ();
	        long currentOffset = buffer.getLong ();

	        assert currentOffset == 0;

	        if (charsCount == 0)
	            return Value.create (ValueType.textType ());

	        String text = "";
	        while (currentOffset < charsCount)
	        {
	            if (buffer.position ()  >= buffer.capacity())
	            {
	                this.refreshReadCache (IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, currentOffset);
	                buffer = this.frame.getCmdBuffer ();
	                buffer.position (buffer.position () + 4); //We already know the query was successful.

	                if ((charsCount != buffer.getLong ())
	                    || (currentOffset != buffer.getLong ()))
	                {
	                    throw new ConnException (CmdResult.GENERAL_ERR, "Unexpected response frame format.");
	                }
	            }

                final int startOffset = buffer.position ();
                while (buffer.get () != 0)
                    ; //Just let the buffer's position advance.
                final int endOffset = buffer.position ();

                String t = new String (buffer.array (),
                                          startOffset,
                                          endOffset - startOffset,
                                          StandardCharsets.UTF_8);
                currentOffset += t.codePointCount (0, t.length ());
                text += t;

                assert currentOffset <= charsCount;
	        }

	        assert text.length () > 0;

	        return Value.create (text, ValueType.textType ());
	    }

	    assert type.isBasic();

        return Value.create (buffer.array(), buffer.position(), type);
	}

   	private final void updateStackTopBasic (Value value) throws ConnException
	{
	    assert ! value.type().equals (ValueType.textType ());
	    assert ! (value.type ().isArray() || value.type().isField() || value.type().isTable());
	    assert ! value.isNull ();

	    ByteBuffer buffer = this.frame.getCmdBuffer ();
	    buffer.position (this.frame.getCmdLastPosition ());

	    final byte[] v = value.toString ().getBytes (StandardCharsets.UTF_8);
	    if (buffer.position () + 1 + 2 + v.length + 1 > buffer.capacity ())
	    {
	        assert this.frame.hasPendingCommands();

	        this.frame.flushPendingCommand ();

	        buffer = this.frame.getCmdBuffer ();
	    }

	    buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
	    buffer.putShort (value.type ().getTypeId ());
	    buffer.put (v);
	    buffer.put((byte) 0);

        this.frame.markBufferPositionValid ();
        this.frame.setPendingCommand(_c.CMD_UPDATE_STACK);
	}

	private final void updateStackTopBasic (Value value, String fieldName, long row) throws ConnException
	{
	    assert ! value.type().equals (ValueType.textType ());
	    assert ! (value.type ().isArray() || value.type().isField() || value.type().isTable());
	    assert ! value.isNull ();

	    byte[] f = fieldName.getBytes (StandardCharsets.UTF_8);
	    byte[] v = value.toString ().getBytes (StandardCharsets.UTF_8);

        ByteBuffer buffer = this.frame.getCmdBuffer ();
        buffer.position (this.frame.getCmdLastPosition ());

	    final int spaceReq = 1 + 2 + f.length + 1 + 8 + v.length + 1;
	    if (buffer.position () + spaceReq > buffer.capacity ())
	    {
	        if ( this.frame.hasPendingCommands ())
	            this.frame.flushPendingCommand ();

	        buffer = this.frame.getCmdBuffer ();

	        if (buffer.position () + spaceReq > buffer.capacity ())
	            throw new ConnException (CmdResult.LARGE_ARGS, "Command to large to update the field value.");
	    }

       buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
	   buffer.putShort ((short) (value.type ().getTypeId () & ValueType.FIELD_MASK));
	   buffer.put (f);
	   buffer.put((byte) 0);
	   buffer.putLong (row);
	   buffer.put (v);
	   buffer.put((byte) 0);

	   this.frame.markBufferPositionValid ();
	   this.frame.setPendingCommand(_c.CMD_UPDATE_STACK);
	}

	private final void updateStackTopBasicArray (Value[] values, long arrayOffset) throws ConnException
	{
	    assert (values != null) && (values.length > 0);

	    for (int i = 0; i < values.length; ++i)
	    {
	        if (values[i].type ().equals (values[i - 1].type ()))
	            throw new ConnException (CmdResult.INVALID_ARGS, "The array's elements used to update the stack top are of different types.");
	    }

	    assert values[0].type ().isBasic ();

	    ByteBuffer buffer = this.frame.getCmdBuffer ();
        buffer.position (this.frame.getCmdLastPosition ());

        int subCmdOffset = buffer.position (), count = 0;
	    for (Value value : values)
	    {
	        assert ! value.isNull ();

	        final byte[] v = value.toString ().getBytes (StandardCharsets.UTF_8);
	        final int spaceReq = v.length + ((count == 0) ? (1 + 2 + 2 + 8 + 1) : 1);

            if ((buffer.position () + spaceReq > buffer.capacity ())
                || (count >= 0xFFFF))
            {
                assert this.frame.hasPendingCommands ();

                this.frame.flushPendingCommand ();

                buffer = this.frame.getCmdBuffer ();
                subCmdOffset = buffer.position ();
                count = 0;
            }

            if (count++ == 0)
            {
                buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
                buffer.putShort ((short) (value.type ().getTypeId () & ValueType.ARRAY_MASK));
                buffer.putShort ((short) count); //Just reserve space
                buffer.putLong (arrayOffset);
            }
            else
                buffer.putShort (subCmdOffset + 3, (short) count);

            buffer.put (v);
            buffer.put ((byte) 0);
            ++arrayOffset;

            this.frame.markBufferPositionValid ();
            this.frame.setPendingCommand (_c.CMD_UPDATE_STACK);
	    }
	}

	private final void updateStackTopBasicArray (Value[] values, long arrayOffset, String fieldName, long row) throws ConnException
	{
	       assert (values != null) && (values.length > 0);

	        for (int i = 0; i < values.length; ++i)
	        {
	            if (values[i].type ().equals (values[i - 1].type ()))
	                throw new ConnException (CmdResult.INVALID_ARGS, "The array's elements used to update the stack top are of different types.");
	        }

	        assert values[0].type ().isBasic ();

	        byte[] f = fieldName.getBytes (StandardCharsets.UTF_8);
	        ByteBuffer buffer = this.frame.getCmdBuffer ();
	        buffer.position (this.frame.getCmdLastPosition ());

	        int subCmdOffset = buffer.position (), count = 0;
	        for (Value value : values)
	        {
	            assert ! value.isNull ();

	            final byte[] v = value.toString ().getBytes (StandardCharsets.UTF_8);
	            final int spaceReq = v.length + ((count == 0) ? (1 + 2 + 2 + f.length + 1 + 8 + 8 + 1) : 1);

	            if ((buffer.position () + spaceReq > buffer.capacity ())
	                || (count >= 0xFFFF))
	            {
	                if (this.frame.hasPendingCommands ())
	                {
    	                this.frame.flushPendingCommand ();

    	                buffer = this.frame.getCmdBuffer ();
    	                subCmdOffset = buffer.position ();

    	                count = 0;
	                }

	                if (buffer.position () + spaceReq > buffer.capacity ())
	                    throw new ConnException (CmdResult.LARGE_ARGS, "Field name too long to allow a value update.");
	            }

	            if (count++ == 0)
	            {
	                buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
	                buffer.putShort ((short) (value.type ().getTypeId () &
	                                            ValueType.ARRAY_MASK   &
	                                            ValueType.FIELD_MASK));
	                buffer.put (f);
	                buffer.put ((byte) 0);
	                buffer.putLong (row);
	                buffer.putLong (arrayOffset);
	                buffer.putShort ((short) count); //Just reserve space
	                buffer.putLong (arrayOffset);
	            }
	            else
	                buffer.putShort (subCmdOffset + 1 + 2 + f.length + 1 + 8, (short) count);

	            buffer.put (v);
	            buffer.put ((byte) 0);
	            ++arrayOffset;

	            this.frame.markBufferPositionValid ();
	            this.frame.setPendingCommand (_c.CMD_UPDATE_STACK);
	        }
	}

	private final void updateStackTopText (String value, long textOffset) throws ConnException
	{
	    final byte[] text = value.getBytes (StandardCharsets.UTF_8);

        ByteBuffer buffer = this.frame.getCmdBuffer ();
        buffer.position (this.frame.getCmdLastPosition ());

        if (buffer.position () + 1 + 2 + 8 + 4 + 1 > buffer.capacity ())
        {
            // + 4 -> max Unicode code units count in utf8
            // + 1 -> the ending 0
            assert this.frame.hasPendingCommands ();
            this.frame.flushPendingCommand ();

            buffer = this.frame.getCmdBuffer ();
        }

        buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
        buffer.putShort ((short) ValueType.TEXT);
        buffer.putLong (textOffset);

	    int textCount = 0;
	    while (textCount < text.length)
	    {
	        final int cuCount = utf8CUCount (text[textCount]);
	        if (cuCount == 0)
	            throw new ConnException (CmdResult.GENERAL_ERR);

	        if (buffer.position () + cuCount + 1 > buffer.capacity ())
	        {
	            assert textCount > 0;
	            assert this.frame.hasPendingCommands ();

	            this.frame.flushPendingCommand ();

	            buffer = this.frame.getCmdBuffer ();

	            buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
	            buffer.putShort ((short) ValueType.TEXT);
	            buffer.putLong (textOffset);
	        }
	        buffer.put (text, textCount, cuCount);
	        buffer.put ((byte) 0);
	        ++textOffset;
	        textCount += cuCount;

            this.frame.markBufferPositionValid ();
            this.frame.setPendingCommand (_c.CMD_UPDATE_STACK);
	    }

	    assert textCount == text.length;
	}

	private final void updateStackTopText (String value, long textOffset, String fieldName, long row) throws ConnException
	{
	    final byte[] text = value.getBytes (StandardCharsets.UTF_8);
	    final byte[] f = fieldName.getBytes (StandardCharsets.UTF_8);

	    final int minSpace = 1 + 2 + f.length + 1 + 8 + 8 + 4 + 1;

        ByteBuffer buffer = this.frame.getCmdBuffer ();
        buffer.position (this.frame.getCmdLastPosition ());

        if (buffer.position () + minSpace > buffer.capacity ())
        {
            // + 4 -> max Unicode code units count in utf8
            // + 1 -> the ending 0
            assert this.frame.hasPendingCommands ();
            this.frame.flushPendingCommand ();

            buffer = this.frame.getCmdBuffer ();

            if (buffer.position () + minSpace > buffer.capacity ())
                throw new ConnException (CmdResult.LARGE_ARGS, "Field name too long to allow a value update.");
        }

        buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
        buffer.putShort ((short) (ValueType.TEXT & ValueType.FIELD_MASK));
        buffer.put(f);
        buffer.put((byte) 0);
        buffer.putLong (row);
        buffer.putLong (textOffset);

        int textCount = 0;
        while (textCount < text.length)
        {
            final int cuCount = utf8CUCount (text[textCount]);
            if (cuCount == 0)
                throw new ConnException (CmdResult.GENERAL_ERR);

            if (buffer.position () + minSpace + cuCount + 1 > buffer.capacity ())
            {
                assert textCount > 0;
                assert this.frame.hasPendingCommands ();

                this.frame.flushPendingCommand ();

                buffer = this.frame.getCmdBuffer ();

                buffer.put (_c.CMD_UPDATE_FUNC_CHTOP);
                buffer.putShort ((short) (ValueType.TEXT & ValueType.FIELD_MASK));
                buffer.put(f);
                buffer.put((byte) 0);
                buffer.putLong (row);
                buffer.putLong (textOffset);;
            }

            buffer.put (text, textCount, cuCount);
            buffer.put ((byte) 0);
            ++textOffset;
            textCount += cuCount;

            this.frame.markBufferPositionValid ();
            this.frame.setPendingCommand (_c.CMD_UPDATE_STACK);
        }

        assert textCount == text.length;
	}


	private final void refreshReadCache (String field, long rowHint, long arrayOffsetHint, long textOffsetHint) throws IOException
	{
	    assert ! this.frame.hasPendingCommands ();

	    final int reqSize = ((field == null) ? 0 : field.length()) + 1 + 8 + 8 +8;
	    if (reqSize > this.frame.maxCmdSize ())
	        throw new ConnException (CmdResult.LARGE_ARGS);

	    this.frame.discardPendingCommand ();

	    ByteBuffer buffer = this.frame.getCmdBuffer ();
	    buffer.put (field.getBytes (StandardCharsets.UTF_8));
	    buffer.put ((byte) 0);
	    buffer.putLong (rowHint);
	    buffer.putLong (arrayOffsetHint);
	    buffer.putLong (textOffsetHint);

	    this.frame.markBufferPositionValid ();
	    this.frame.sendCommand (_c.CMD_READ_STACK);
	    final int reponse = this.frame.getCmdBuffer ().getInt ();
	    if (reponse != CmdResult.OK)
	        throw new ConnException(reponse);
	}

	private final ByteBuffer skipUntilCachedRow (long row) throws ConnException
	{
	    assert this.frame.getCachedResponse () == _c.CMD_READ_STACK_RSP;

	    ByteBuffer b = this.frame.getCmdBuffer ();
	    b.position (b.position () + 4);

	    final short type = b.getShort ();

	    if (row >= b.getLong ())
	        throw new ConnException (CmdResult.INVALID_ROW);

	    long currentRow = b.getLong ();
	    if (currentRow > row)
	        return null;

	    if (ValueType.isTable (type))
	    {
	        int fieldsCount = b.getShort ();
	        fieldsCount &= 0xFFFF; //Sign correction

	        while (currentRow++ < row)
	        {
    	        for (int i = 0; i < fieldsCount; ++i)
    	        {
    	            if ( ! skipCachedFieldValue (b))
    	                return null;
    	        }
	        }
	    }
	    else if (ValueType.isField (type) && ValueType.isArray (type))
	    {
	        while (currentRow++ < row)
	        {
	            final long elemsCount = b.getLong ();
	            if (elemsCount == 0)
	                break;

	            long offset = b.getLong ();
	            while (offset++ < elemsCount)
	            {
	                if (b.position () >= b.capacity ())
	                    return null;

	                while (b.get () != 0)
	                    ; //Just let the buffer position to advance.
	            }
	        }
	    }
	    else if (ValueType.isField (type)
	            && ValueType.getBaseType (type) == ValueType.TEXT)
	    {
	           while (currentRow++ < row)
	            {
	                final long charsCount = b.getLong ();
	                if (charsCount == 0)
	                    break;

	                long offset = b.getLong ();

	                assert (offset < charsCount);

                    while (b.get () != 0)
                        ; //Just let the buffer's position to advance.
	            }
	    }
	    else
	    {
	        while (b.get () != 0)
                ; //Just let the buffer's position to advance.
	    }

	    if (b.position() >= b.capacity ())
	        return null;

	    return b;
	}

	private final ByteBuffer skipUntilFieldValue (String field, long row) throws ConnException
	{
	    ByteBuffer b = this.skipUntilCachedRow (row);

	    if (b == null)
	        return null;

	    if (! skipUntilCachedField (field, b))
	        return null;

	    while (b.get () != 0)
	        ; //Ignore the field name

	    return b;
	}

    private final ValueType internalDescribeValue (String name) throws IOException
    {
       assert name != null;

       ByteBuffer buffer = null;
       TableFieldType[] fields = null;
       int field = 0, fieldsCount = 0;
       short type = 0;
       do
       {
           if ((field == 0)
               || (buffer.position () >= this.frame.getCmdLastPosition ()))
           {
               this.frame.discardPendingCommand ();
               buffer = this.frame.getCmdBuffer ();

               buffer.putShort ((short) field);     //Field hint
               buffer.putShort ((short) 0);         //Reserved
               buffer.put (name.getBytes (StandardCharsets.UTF_8));
               buffer.put ((byte) 0);
               this.frame.markBufferPositionValid ();
               this.frame.sendCommand(_c.CMD_GLOBAL_DESC);
               buffer = this.frame.getCmdBuffer ();

               int cmdResult = buffer.getInt ();
               if (cmdResult != CmdResult.OK)
                   throw new ConnException (cmdResult);

               while (buffer.get() != 0)
                   ; //Ignore the global variable name

               if (field == 0)
               {
                   type = buffer.getShort ();

                   if ( ! ValueType.isTable (type))
                       return ValueType.create (type);

                   fieldsCount = buffer.getShort ();
                   fieldsCount &= 0x0000FFFF; //Sign correction

                   if (fieldsCount == 0)
                       return ValueType.create (type);

                   fields = new TableFieldType[fieldsCount];
                   buffer.position (buffer.position() - (2 + 2));
               }

              if ((type != buffer.getShort ())
                  || (fieldsCount != (buffer.getShort () & 0x0000FFFF))
                  || (field != (buffer.getShort () & 0x0000FFFF)))
               {
                   throw new ConnException (CmdResult.GENERAL_ERR, "Unexpected answer format received from server.");
               }
           }

           final int startOffset = buffer.position ();
           while (buffer.get () != 0)
               ; //Just let the buffer's position advance.
           final int endOffset = buffer.position () - 1;

           assert startOffset < endOffset;

           fields[field] = new TableFieldType (new String (buffer.array (),
                                                            startOffset,
                                                            endOffset - startOffset,
                                                            StandardCharsets.UTF_8),
                                               ValueType.create (buffer.getShort ()));
       } while (++field < fieldsCount);

       this.frame.discardPendingCommand ();

       if ((fields != null) && (fields.length > 0))
           return ValueType.create (fields);

       return ValueType.create (type);
    }

    private static final boolean skipCachedFieldValue (ByteBuffer b) throws ConnException
    {
        if (b.position () >= b.capacity ())
            return false;

        while (b.get () != 0)
            ; //Just let the buffer position advance

        final short type = b.getShort ();

        if (ValueType.isArray (type))
        {
            final long elemsCount = b.getLong ();
            if (elemsCount > 0)
            {
                long offset = b.getLong ();

                assert offset < elemsCount;

                while (offset < elemsCount)
                {
                   if (b.position () >= b.capacity ())
                       return false;

                   while (b.get () != 0)
                       ; //Just let the buffer's position advance;

                   ++offset;
                }
            }
        }
        else if (type == ValueType.TEXT)
        {
            final long charsCount = b.getLong ();
            if (charsCount > 0)
            {
                b.position (b.position () + 8);
                while (b.get () != 0)
                    ; //Just let the buffer advance
            }
        }
        else if (ValueType.isBasic (type))
        {
            while (b.get () != 0)
                ; //Just let the buffer advance
        }
        else
            throw new ConnException (CmdResult.GENERAL_ERR);

        return true;
    }

    private static final boolean skipUntilCachedField (String f, ByteBuffer b) throws ConnException
    {
        assert f.length () > 0;

        byte[] fb = f.getBytes (StandardCharsets.UTF_8);

        do
        {
            if (b.position () >= b.capacity ())
                return false;

            int fieldStart = b.position (), offset  = 0;
            while (offset < fb.length)
            {
                if (b.get () != fb[offset])
                    break;

                ++offset;
            }

            if ((offset == fb.length) && (b.get () == 0))
            {
                b.position (fieldStart);
                return true;
            }
            else
                b.position (fieldStart);

        } while (skipCachedFieldValue (b));

        return false;
    }

	private static final int utf8CUCount (final byte firstCodeUnit)
	{
	    short unit = firstCodeUnit;
	    unit &= 0x00FF; //Sign correction.

	    if (unit < 0x80)
	        return 1;
	    else if (unit < 0xC0)
	        return 0;
	    else if (unit < 0xE0)
	        return 2;
	    else if (unit < 0xF0)
	        return 3;
	    else if (unit < 0xF8)
	        return 4;

	    assert false;

	    return 0;
	}

	private CommunicationFrame   frame;
	private byte                 userId;

	/**
	 * Used to connect as a database administrator.
	 */
	public static final byte ADMIN                 = 0;

	/**
     * Used to connect as a regular user.
     */
    public static final byte USER                  = 1;

    public static final String IGNORE_FIELD        = null;
    public static final long   IGNORE_ROW          = -1;
    public static final long   IGNORE_OFFSET       = -1;

    private static final String[] emptyList        = new String[0];
}