package net.whais.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Represents a connection to a WHAIS data server.
 *
 * The class Connection is used to instantiate a communication channel to a
 * WHAIS sever and to manage the data it holds.
 *
 * @version 1.0
 */
public class Connection
{
    /**
     * Instantiate a communication channel.
     * <p>
     * This is the main constructor used to create a communication channel with
     * a WHAIS server.</p>
     *
     * @param host
     *            The host where the Whais server resides.
     * @param port
     *            The port where the server listens for TCP/IP connections.
     * @param database
     *            The name of the database to use.
     * @param key
     *            The authentication data. Based on the used authentication
     *            option this could be a password(the UTF-8 string
     *            representation) or a cryptographic key.
     * @param userId
     *            Set this to {@link #ADMIN} to connect as a database
     *            administrator or to {@link #USER} as a regular user.
     * @param maxFrameSize
     *            The maximum communication frame size the client may use. Based
     *            on client's needs or network's limitations the client has a
     *            possibility to hint the server what frame size to use. This
     *            value should be set from {@code 512} to {@code 65536} bytes.
     *             The actual maximum frame size used
     *
     *            to exchange data depends also on servers' settings (e.g. its
     *            value set for maximum communication frame size and by the used
     *            cipher).
     *
     * @throws IOException
     * @throws net.whais.Client.ConnException
     *
     * @since 1.0
     */
    public Connection( String    host,
                       String    port,
                       String    database,
                       byte[]    key,
                       byte      userId,
                       int       maxFrameSize) throws IOException
    {
        final InetAddress[] addresses = InetAddress.getAllByName( host);

        Throwable t = null;
        Socket    s = null;
        for (InetAddress address : addresses) {
            try {
                s = new Socket( address, Integer.parseInt( port));
                break;
            } catch (IOException e) {
                t = e;
            }
        }

        if (s == null) {
            throw new IOException( t);
        }

        mUserId = userId;
        mFrame  = new CommunicationFrame( s, userId, maxFrameSize, database, key);
    }

    /**
     * Instantiate a communication channel.
     * <p>
     * Wrapper constructor used to instantiate a communication channel with a
     * default frame size set to <code>32 KBytes</code>.</p>
     *
     * @see #Connection(String, String, String, byte[], byte, int)
     * @since 1.0
     */
    public Connection( String    host,
                       String    port,
                       String    database,
                       byte[]    key,
                       byte      userId) throws IOException
    {
        this( host, port, database, key, userId, _c.DEFAULT_FRAME_SIZE);
    }

    /**
     * Instantiate a communication channel.
     *
     * <p>
     * Wrapper constructor used to instantiate a communication channel with a
     * frame size set of <code>32 KBytes</code> and to automatically convert
     * the password from a Java string to the equivalent UTF-8 byte array.</p>
     *
     * @see #Connection(String, String, String, byte[], byte, int)
     *
     * @since 1.0
     */
    public Connection( String    host,
                       String    port,
                       String    database,
                       String    password,
                       byte      userId) throws IOException
    {
        this( host, port, database, password.getBytes( StandardCharsets.UTF_8), userId);
    }

    /**
     * Instantiate a communication channel.
     * <p>
     * Wrapper constructor to automatically convert the password to the UTF-8
     * representation byte array.</p>
     *
     * @see #Connection(String, String, String, byte[], byte, int)
     * @since 1.0
     */
    public Connection( String    host,
                       String    port,
                       String    database,
                       String    password,
                       byte      userId,
                       int       maxFrameSize) throws IOException
    {
        this( host, port, database, password.getBytes( StandardCharsets.UTF_8), userId, maxFrameSize);
    }

    /**
     * Ends the communication session with the WHAIS server.
     *
     * @param discardCmds
     *            Set to {@code true} to attempt to flush any existent cached
     *            commands prior closing the channel or to {@code false} to
     *            discard them.
     *
     * @since 1.0
     */
    public void close( boolean discardCmds)
    {
        if (mFrame == null)
            return;

        try {
            if (discardCmds)
                mFrame.discardCommandBuffer();
            else
                mFrame.flushPendingCommand();

            mFrame.sendCommand( _c.CMD_CLOSE_CONN, false);
        } catch (IOException e) {
            // Do nothing here ... just ignore it.
        } finally {
            mFrame.Close();
            mFrame = null;
        }
    }

    /**
     * Wrapper for {@code close( true)}.
     *
     * @see #close(boolean)
     *
     * @since 1.0
     */
    public void close()
    {
        close( true);
    }

    /**
     * Test is the connection is still up.
     * <p>
     * This will flush any cached commands or clears any cached responses. </p>
     *
     * @return
     *          {@code true} if the connection is healty.
     *
     * @since 1.0
     */
    public boolean isAlive ()
    {
        try {
            if (mFrame.hasPendingCommands())
                mFrame.flushPendingCommand();
            else if (mFrame.hasCachedReponse())
                mFrame.discardCommandBuffer();

            pingServer();
        }
        catch (Throwable e) {
            return false;
        }
        return true;
    }

    /**
     * Release any acquired resources associated with the opened channel.
     * <p>
     * Makes sure to free the communication channel if it was taken. Good
     * coding practices recommends not to rely on it.</p>
     *
     * @since 1.0
     */
    @Override
    protected void finalize() throws Throwable
    {
        close( true);
        super.finalize();
    }

    /**
     * Test server communication channel.
     * <p>
     * Used to check if the connection is healthy or signal the server to keep
     * this channel open.</p>
     * <p>
     * Is should be used to check if the communication channel still opened
     * after an exception was thrown during the interaction with the WHAIS
     * server. Depending on the severity of the exceptional condition the
     * server might decide to end the respective channel altogether.</p>
     *
     * @throws IOException
     *
     * @since 1.0
     * @see net.whais.Client.ConnException
     */
    public void pingServer() throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        mFrame.sendCommand( _c.CMD_PING_SERVER);
        final int cmdResult = mFrame.getCmdBuffer().getInt();
        mFrame.discardCommandBuffer();

        if (cmdResult != CmdResult.OK)
            throw new ConnException( cmdResult);
    }

    /**
     * Retrieves the names of globals values.
     * <p>
     * Given the {@link Connection} was initiated by an administrator, this
     * method its used to retrieve all the names of the global values defined
     * in the context of the connected database. </p>
     *
     * @return
     *            An array holding the names of the global names defined by
     *            the connected database. If no names are available this will
     *            return an array with {@code 0} elements.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #describeGlobal(String)
     * @since 1.0
     */
    public String[] retrieveGlobalNames() throws IOException
    {
        String[] result = Connection.emptyList;

        if (mUserId != ADMIN)
            throw new ConnException( CmdResult.OP_NOTPERMITED);

        else if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        ByteBuffer buffer = null;
        int glbIndex = 0, glbsCount = 0;
        do {
            if ((glbIndex == 0) || (buffer.position() >= mFrame.getLastPosition())) {
                mFrame.discardCommandBuffer();
                buffer = mFrame.getCmdBuffer();

                buffer.putInt( glbIndex);
                mFrame.markBufferPositionValid();
                mFrame.sendCommand( _c.CMD_LIST_GLOBALS);
                buffer = mFrame.getCmdBuffer();

                final int cmdResult = buffer.getInt();
                if (cmdResult != CmdResult.OK)
                    throw new ConnException( cmdResult);

                if (glbIndex == 0) {
                    glbsCount = buffer.getInt();
                    if (glbsCount != 0)
                        result = new String[glbsCount];
                    else
                        return emptyList;

                    buffer.position( buffer.position() - 4);
                }

                if ((buffer.getInt() != glbsCount) || (buffer.getInt() != glbIndex)) {
                    throw new ConnException( CmdResult.GENERAL_ERR, "Unexpected answer format received from server.");
                }
            }

            final int startOffset = buffer.position();
            while (buffer.get() != 0)
                ; // Just let the buffer's position advance.
            final int endOffset = buffer.position() - 1;

            assert startOffset < endOffset;

            result[glbIndex] = new String( buffer.array(),
                                           startOffset,
                                           endOffset - startOffset,
                                           StandardCharsets.UTF_8);
        } while (++glbIndex < glbsCount);

        return result;
    }

    /**
     * Gets the type of a global value.
     * <p>
     * Describes a global value defined in the namespace of connected
     * database. This {@link Connection} must be initiated by an administrator
     * to successfully complete this method call.</p>
     *
     * @param name
     *            The name of the global value.
     * @return
     *            An object representing the type of the specified value.
     *
     * @throws ConnException
     *
     * @since 1.0
     */
    public ValueType describeGlobal( String name) throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);
        else if (mUserId != ADMIN)
            throw new ConnException( CmdResult.OP_NOTPERMITED);
        else if ((name == null) || (name.length() == 0))
            throw new ConnException( CmdResult.INVALID_ARGS);

        return internalDescribeValue( name);
    }

    /**
     * Retrieves the names of the WHAIS procedures.
     * <p>
     * Retrieve the names of the WHAIS procedures defined in the namespace of
     * connected database. This {@link Connection} must be initiated by an
     * administrator to successfully complete this method call.</p>
     *
     * @return
     *            An array with the procedures names. If there are no
     *            procedures it will return an array with {@code 0}
     *            elements.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #describeProcedure(String)
     * @since 1.0
     */
    public String[] retrieveProceduresNames() throws IOException
    {
        String[] result = null;

        if (mUserId != ADMIN)
            throw new ConnException( CmdResult.OP_NOTPERMITED);
        else if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        ByteBuffer buffer = null;
        int procIndex = 0, procsCount = 0;
        do {
            if ((procIndex == 0)
                        || (buffer.position() >= mFrame.getLastPosition())) {
                mFrame.discardCommandBuffer();
                buffer = mFrame.getCmdBuffer();
                buffer.putInt( procIndex);
                mFrame.markBufferPositionValid();
                mFrame.sendCommand( _c.CMD_LIST_PROCEDURE);
                buffer = mFrame.getCmdBuffer();

                final int cmdResult = buffer.getInt();
                if (cmdResult != CmdResult.OK)
                    throw new ConnException( cmdResult);

                if (procIndex == 0) {
                    procsCount = buffer.getInt();
                    if (procsCount != 0)
                        result = new String[procsCount];
                    else
                        return emptyList;

                    buffer.position( buffer.position() - 4);
                }

                if ((buffer.getInt() != procsCount) || (buffer.getInt() != procIndex)) {
                    throw new ConnException( CmdResult.GENERAL_ERR, "Unexpected answer format received from server.");
                }
            }

            final int startOffset = buffer.position();
            while (buffer.get() != 0)
                ; // Just let the buffer's position advance.
            final int endOffset = buffer.position() - 1;

            assert startOffset < endOffset;

            result[procIndex] = new String( buffer.array(), startOffset, endOffset - startOffset,
                    StandardCharsets.UTF_8);
        } while (++procIndex < procsCount);

        return result;
    }

    /**
     * Describes a WHAIS procedure.
     * <p>
     * Once the name of a procedure defined in the context of the connected
     * database is known, it can be used with this method to find more
     * information about it( e.g like the type of the parameters it needs or
     * of the value it returns). This {@link Connection} must be initiated
     * by an administrator to successfully complete this method call.</p>
     *
     * @param name
     *            The name of the Whais procedure.
     * @return
     *            An object holding the procedure description.
     *
     * @throws ConnException
     * @throws IOException
     *
     * @see #retrieveProceduresNames()
     * @see ProcedureDescription
     * @since 1.0
     */
    public ProcedureDescription describeProcedure( String name) throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);
        else if (mUserId != ADMIN)
            throw new ConnException( CmdResult.OP_NOTPERMITED);
        else if ((name == null) || (name.length() == 0))
            throw new ConnException( CmdResult.INVALID_ARGS);

        mFrame.discardCommandBuffer();
        ByteBuffer b = mFrame.getCmdBuffer();
        final byte[] n = name.getBytes( StandardCharsets.UTF_8);
        if (mFrame.availableCmdSize() < 2 + 2 + n.length + 1) {
            throw new ConnException( CmdResult.LARGE_ARGS,
                                     "Procedure name is too long for the configured communication frame size.");
        }

        ValueType[] paramTypes = null;
        int param = 0, paramsCount = 0;
        do {
            if ((paramTypes == null)
                        || (b.position() >= mFrame.getLastPosition())) {
                mFrame.discardCommandBuffer();
                b = mFrame.getCmdBuffer().putShort( (short) param)
                                         .putShort( (short) 0)
                                         .put( n)
                                         .put( (byte) 0);
                mFrame.markBufferPositionValid();
                mFrame.sendCommand( _c.CMD_DESC_PROC_PARAM);
                b = mFrame.getCmdBuffer();
                final int cmdRsp = b.getInt();
                if (cmdRsp != CmdResult.OK)
                    throw new ConnException( cmdRsp);

                b.position( b.position() + n.length + 1);
                if (paramTypes == null) {
                    paramsCount = b.getShort();
                    paramsCount &= 0x0000FFFF;

                    if (paramsCount <= 0)
                        throw new ConnException( CmdResult.GENERAL_ERR, "Unexpected answer from server.");

                    paramTypes = new ValueType[paramsCount];
                    b.position( b.position() + 2);
                } else
                    b.position( b.position() + 4);
            }

            short ptype = b.getShort();
            if (ValueType.isTable( ptype)) {
                int fieldsCount = b.getShort();
                TableFieldType[] fields = null;

                if (fieldsCount > 0) {
                    fields = new TableFieldType[fieldsCount];

                    for (int i = 0; i < fieldsCount; ++i) {
                        int startOffset = b.position();
                        while (b.get() != 0)
                            ; // Let the buffer's position advance
                        int endOffset = b.position() - 1;

                        final short fieldType = b.getShort();
                        fields[i] = new TableFieldType( new String( b.array(),
                                                                    startOffset,
                                                                    endOffset - startOffset,
                                                                    StandardCharsets.UTF_8),
                                                        ValueType.create( fieldType));
                    }
                }

                if ((fields != null) && (fields.length > 0))
                    paramTypes[param] = ValueType.create( fields);
                else
                    paramTypes[param] = ValueType.create( ptype);
            } else
                paramTypes[param] = ValueType.create( ptype);

            assert paramTypes[param] != null;

        } while (++param < paramsCount);

        assert paramTypes.length > 0;

        return new ProcedureDescription( name, paramTypes);
    }

    /**
     * Describes the value from the top of the communication channel's stack.
     * <p>
     * This function is used to describe the result returned after an
     * execution of a WHAIS procedure.</p>
     * <p>
     * This is used when the user wants to avoid fetching large value contents
     * (e.g. like tables with an appreciable number of fields and/or with lots
     * of allocated rows). It provides the means to get information about
     * fields of table or to spot returned fields of tables, before retrieving
     * the values associated with their row cells.</p>
     *
     * @return
     *            An object holding the description of the value.
     *
     * @throws IOException
     *
     * @see #retrieveStackTop()
     * @see #retrieveStackTop(String, long)
     * @see #pushStackValue(Value)
     * @see #popStackValues(int)
     * @see #flushStackUpdates()
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public ValueType describeStackTop() throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        return internalDescribeValue( "");
    }

    /**
     * Add a <i>null</i> value on top of the communication channel's stack.
     * <p>
     * Used to instruct the WHAIS sever to put a <i>null</i> value on top of
     * this session execution stack. This is used mainly when the user does
     * not know all the content of a value that it needs to push on stack
     * (e.g. maybe the cell of a table with lots of rows and fields are need
     * to be retrieved from other resources).The user will use this method
     * followed by several use of and/or {@link #updateStackTopAddTableRows(int)}
     * {@link #updateStackTop(Value, String, long)}.</p>
     *
     * @param type
     *            An object holding the value type description.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #popStackValues(int)
     * @see #updateStackTop(Value)
     * @see #updateStackTop(Value, String, long)
     * @see #retrieveStackTop()
     * @see #retrieveStackTop(String, long)
     * @see #retrieveStackTopRowsCount()
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public void pushStackValue( ValueType type) throws IOException
    {
        if (!mFrame.hasPendingCommands())
            mFrame.discardCommandBuffer();
        else if (mFrame.getPendingCommand() != _c.CMD_UPDATE_STACK)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (type.isField())
            throw new ConnException( CmdResult.INVALID_ARGS, "Field values cannot be pushed on stack.");

        if (mFrame.availableCmdSize() < 3) {
            assert mFrame.hasPendingCommands();

            mFrame.flushPendingCommand();
            pushStackValue( type);
            return;
        }

        final ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());
        buffer.put( _c.CMD_UPDATE_FUNC_PUSH)
              .putShort( type.getTypeId());
        if (type.isTable() && (type.getFields().length > 0)) {
            buffer.putShort( (short) type.getFields().length);
            for (TableFieldType f : type.getFields()) {
                final byte[] utf8FieldName = f.getName().getBytes( StandardCharsets.UTF_8);
                final int spaceRequired = utf8FieldName.length + 1 + 2;

                if (buffer.position() + spaceRequired >= buffer.capacity()) {
                    if (!mFrame.hasPendingCommands()) {
                        throw new ConnException( CmdResult.LARGE_ARGS,
                                "The communication frame size does not  to allow the push of this table value.");
                    }

                    mFrame.flushPendingCommand();
                    pushStackValue( type);
                    return;
                }

                buffer.put( utf8FieldName)
                      .put( (byte) 0)
                      .putShort( f.getType().getTypeId());
            }
        }
        mFrame.markBufferPositionValid();
        mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
    }

    /**
     * Add a value on top of the communication channel stack.
     * <p>
     * This method it is simply a wrapper, calling
     * {@link #pushStackValue(ValueType)} followed by
     * {@link #updateStackTop(Value)} for better readability and ease of
     * use.</p>
     *
     * @param v
     *            Holds the value that needs to be put on stack.
     *
     * @throws IOException
     *
     * @see #popStackValues(int)
     * @see #updateStackTop(Value)
     * @see #updateStackTop(Value, String, long)
     * @see #retrieveStackTop()
     * @see #retrieveStackTop(String, long)
     * @see #retrieveStackTopRowsCount()
     * @see #executeProcedure(String)
     * @see #flushStackUpdates()
     *
     * @since 1.0
     */
    public void pushStackValue( Value v) throws IOException
    {
        pushStackValue( v.type());

        if ( ! v.isNull())
            updateStackTop( v);
    }

    /**
     * Remove values from the top of the communication channel stack.
     * <p>
     * Used to remove a specified number of values from the execution stack.
     * It is mostly used to clear the result of the last procedure execution,
     * before preparing the stack for another procedure execution.</p>
     *
     * @param count
     *            The number of values to remove. Use {@link #ALL} to remove
     *            all stack values.
     *
     * @throws IOException
     *
     * @see #popStackValues(int)
     * @see #updateStackTop(Value)
     * @see #updateStackTop(Value, String, long)
     * @see #retrieveStackTop()
     * @see #retrieveStackTop(String, long)
     * @see #retrieveStackTopRowsCount()
     * @see #executeProcedure(String)
     * @see #flushStackUpdates()
     *
     * @since 1.0
     */
    public void popStackValues( int count) throws IOException
    {
        if ( ! mFrame.hasPendingCommands())
            mFrame.discardCommandBuffer();
        else if (mFrame.getPendingCommand() != _c.CMD_UPDATE_STACK)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (mFrame.availableCmdSize() < 5) {
            assert mFrame.hasPendingCommands();

            mFrame.flushPendingCommand();
            popStackValues( count);

            return;
        }

        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        buffer.put( _c.CMD_UPDATE_FUNC_POP)
              .putInt( count);

        mFrame.markBufferPositionValid();
        mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
    }

    /**
     * Executes a WHAIS procedure.
     * <p>
     * The main way to retrieve a information from a WHAIS server is through
     * calling procedures. To provide the procedures with their required
     * parameters, the user has to put them prior on the communication channel
     * execution stack. First required parameter should be put first on the
     * stack, followed by the second one and so one. Once the procedure has
     * finished to execute all procedure's required parameters will be removed
     * from top of stack with the execution result put on top of stack.</p>
     * <p>
     * In case of uncommitted stack updates this will flush the pending stack
     * updates before sending the procedure execution request.</p>
     *
     * @param name
     *            The name of procedure to call. The procedure has to be
     *            defined in the namespace of the the connected database
     *            context or that is accessible by this context.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #pushStackValue(Value)
     * @see #pushStackValue(ValueType)
     * @see #popStackValues(int)
     * @see #updateStackTop(Value)
     * @see #updateStackTop(Value, String, long)
     * @see #retrieveStackTop()
     * @see #retrieveStackTop(String, long)
     * @see #retrieveStackTopRowsCount()
     * @see #flushStackUpdates()
     *
     * @since 1.0
     */
    public void executeProcedure( final String name) throws IOException
    {
        if (!mFrame.hasPendingCommands())
            mFrame.discardCommandBuffer();
        else if (mFrame.getPendingCommand() == _c.CMD_UPDATE_STACK)
            flushStackUpdates();
        else if (mFrame.getPendingCommand() != _c.CMD_INVALID)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        mFrame.discardCommandBuffer();
        final ByteBuffer b = mFrame.getCmdBuffer();
        final byte[] n = name.getBytes( StandardCharsets.UTF_8);
        if (mFrame.availableCmdSize() < n.length + 1) {
            throw new ConnException( CmdResult.LARGE_ARGS,
                                     "Procedure name is too long for the configured communication frame size.");
        }

        b.put( n)
         .put( (byte) 0);

        mFrame.markBufferPositionValid();
        mFrame.sendCommand( _c.CMD_EXEC_PROC);
    }

    /**
     * Adds rows to the stack top table.
     * <p>
     * Used in conjunction with {@link #pushStackValue(ValueType)} and
     * {@link #updateStackTop(Value, String, long)} to update table values
     * with cell values that are not immediately know by caller.</p>
     *
     * @param rowsCount
     *            The number of rows to increase the table with.
     *
     * @throws IOException
     *
     * @see #pushStackValue(ValueType)
     * @see #updateStackTop(Value, String, long)
     * @see #flushStackUpdates()
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public void updateStackTopAddTableRows( int rowsCount) throws IOException
    {
        assert rowsCount > 0;

        if (!mFrame.hasPendingCommands())
            mFrame.discardCommandBuffer();
        else if (mFrame.getPendingCommand() != _c.CMD_UPDATE_STACK)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (mFrame.availableCmdSize() < 5) {
            assert mFrame.hasPendingCommands();

            mFrame.flushPendingCommand();
            updateStackTopAddTableRows( rowsCount);
            return;
        }

        final ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        buffer.put( _c.CMD_UPDATE_FUNC_TBL_ROWS)
              .putInt( rowsCount);

        mFrame.markBufferPositionValid();
        mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
    }


    /**
     * Changes the stack top value content.
     * </p>
     * It sends an updated content for for the top of stack value. Its mostly
     * used after a call to @{code {@link #pushStackValue(ValueType)}} that
     * has ensured a null value of the same type was created on the stack
     * prior transmitting the actual value content.</p>
     * <p>
     * However there are few scenarios to use this effectively. It is worth
     * to look at alternatives as {@link #pushStackValue(Value)}, or its more
     * advanced version {@link #updateStackTop(Value, String, long)} for
     * better results.</p>
     * </p>
     * The value used to update the content has to be non null (as it is
     * supposed that null values are already created by default on stack) and
     * has to be properly defined (e.g. no undefined array, tables with no
     * fields, fields, etc.).</p>
     *
     * @param val
     *            The new value content.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #pushStackValue(ValueType)
     * @see #popStackValues(int)
     * @see #retrieveStackTop()
     * @see #flushStackUpdates()
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public void updateStackTop( Value val) throws IOException
    {
        if (!mFrame.hasPendingCommands())
            mFrame.discardCommandBuffer();
        else if (mFrame.getPendingCommand() != _c.CMD_UPDATE_STACK)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (val.isNull()) {
            throw new ConnException( CmdResult.INVALID_ARGS,
                                     "Cannot use a null value to update stack.");
        }

        if (val.type().equals( ValueType.textType()))
            updateStackTopText( val.toString(), 0);
        else if (val.type().isBasic())
            updateStackTopBasic( val);
        else if (val.type().isArray())
            updateStackTopArray( ((ArrayValue) val).toArray(), 0);
        else if (val.type().isTable()) {
            if (val.isNull())
                return;

            final TableValue table = (TableValue) val;
            final int rowsCount = table.getRowsCount();

            assert rowsCount > 0;

            updateStackTopAddTableRows( rowsCount);

            for (int row = 0; row < rowsCount; ++row) {
                if (table.isEmptyRow( row))
                    continue;

                for (TableFieldType fields : table.getFields()) {
                    final Value v = table.get( fields.getName(), row);
                    if ((v == null) || v.isNull())
                        continue;

                    updateStackTop( v, fields.getName(), row);
                }
            }
        } else
            throw new ConnException( CmdResult.INVALID_ARGS, "Unexpected value type to update the top of stack.");
    }

    /**
     * Updates the stack top value.
     * <p>
     * Used for cases when the user needs to update the cell values of a table
     * and either the information is not rapidly available. This allows the
     * update of table cell by individually selecting the associated field and
     * row (for basic or arrays types it might worth to consider the
     * {@link #pushStackValue(Value)} alternative) when the information of the
     * respective cell is available.</p>
     * <p>
     * The value has to be a non null value as it is supposed that rows values
     * are already null by default when the rows were added to the table.</p>
     * <p>
     * A call with the @{code fieldName} set to {@code null} (or set to
     * {@link #IGNORE_FIELD}) and the {@code row} set to {@link #IGNORE_ROW}
     * is similar to directly calling {@link #updateStackTop(Value)}.</p>
     *
     * @param val
     *            The new value content.
     * @param fieldName
     *            The cell's table field name to update (use
     *            {@link #IGNORE_FIELD} if does not apply).
     * @param row
     *            The cell's table row (use {@link #IGNORE_ROW} if does not
     *            apply).
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #pushStackValue(ValueType)
     * @see #pushStackValue(Value)
     * @see #popStackValues(int)
     * @see #updateStackTop(Value)
     * @see #updateStackTop(Value, String, long)
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public void updateStackTop( Value     val,
                                String    fieldName,
                                long      row) throws IOException
    {
        if (((fieldName == null)
                || (fieldName.equals( IGNORE_FIELD))) && (row == IGNORE_ROW)) {
            updateStackTop( val);
            return;
        }

        if ( ! mFrame.hasPendingCommands())
            mFrame.discardCommandBuffer();

        else if (mFrame.getPendingCommand() != _c.CMD_UPDATE_STACK)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (row < 0)
            throw new ConnException( CmdResult.INVALID_ROW);

        if (val.type().equals( ValueType.textType()))
            updateStackTopText( val.toString(), 0, fieldName, row);
        else if (val.type().isBasic())
            updateStackTopBasic( val, fieldName, row);
        else if (val.type().isArray())
            updateStackToArray( ((ArrayValue) val).toArray(), 0, fieldName, row);
        else
            throw new ConnException( CmdResult.INVALID_ARGS, "Unexpected value type to update the top of stack.");
    }

    /**
     * Commits stack updates requests.
     * <p>
     * In order to improve the efficiency when communicating with the WHAIS
     * server, all stack update requests (including pushes and pops) are
     * cached such way to use as fewer communication frames as possible. The
     * down side of this mechanism, possible error conditions are not signaled
     * (e.g. the corresponding exception thrown) after the exact faulty stack
     * update request but rather after the one that triggers the commit of the
     * cached change.</p>
     * <p>
     * The method allows to trigger the commit of cached stack updates
     * requests. It might help in situations when one needs to identify the
     * offending stack update request.</p>
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #pushStackValue(ValueType)
     * @see #pushStackValue(Value)
     * @see #popStackValues(int)
     * @see #retrieveStackTop()
     * @see #flushStackUpdates()
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public void flushStackUpdates() throws IOException
    {
        if ( ! mFrame.hasPendingCommands())
            return;

        if (mFrame.getPendingCommand() != _c.CMD_UPDATE_STACK)
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        mFrame.flushPendingCommand();

        final int cmdResult = mFrame.getCmdBuffer().getInt();
        mFrame.discardCommandBuffer();

        if (cmdResult != CmdResult.OK)
            throw new ConnException( cmdResult);
    }

    /**
     * Get the rows count of the top of stack value.
     * <p>
     * Used when one needs to retrieve the cell's values of a table (or a
     * field) result on a individual basis, to find the number of table
     * rows.</p>
     *
     * @return
     *            The number of table rows. If the value from the stack value
     *            is not a table nor a field then this will return negative
     *            value.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #describeStackTop()
     * @see #retrieveStackTop()
     * @see #retrieveStackTop(String, long)
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public long retrieveStackTopRowsCount() throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (mFrame.getCachedResponse() != _c.CMD_READ_STACK_RSP)
            refreshReadCache( IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, IGNORE_OFFSET);

        final ByteBuffer b = mFrame.getCmdBuffer();
        b.position( b.position() + 4);

        short type = b.getShort();
        if (!(ValueType.isTable( type) || ValueType.isField( type)))
            return -1;

        return b.getLong();
    }

    Value retrieveStackTop( final long row) throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (mFrame.getCachedResponse() != _c.CMD_READ_STACK_RSP) {
            refreshReadCache( IGNORE_FIELD, row, IGNORE_OFFSET, IGNORE_OFFSET);
        }

        ByteBuffer b = mFrame.getCmdBuffer();
        b.position( b.position() + 4);
        short type = b.getShort();

        assert (!ValueType.isTable( type));

        if (!ValueType.isField( type)) {
            if (row == IGNORE_ROW)
                return retrieveStackTop();

            throw new ConnException( CmdResult.INVALID_ROW);
        }

        if ((b = skipUntilCachedRow( row)) == null) {
            refreshReadCache( IGNORE_FIELD, row, IGNORE_OFFSET, IGNORE_OFFSET);

            if ((b = skipUntilCachedRow( row)) == null)
                throw new ConnException( CmdResult.GENERAL_ERR);
        }

        if (ValueType.isArray( type)) {
            long elementsCount = b.getLong();
            type = (short) ValueType.getBaseType( type);

            ArrayValue result = Value.createArray( ValueType.create( type | ValueType.ARRAY_MASK));

            if (elementsCount == 0)
                return result;

            long arrayOffset = b.getLong();
            if (arrayOffset != 0) {
                arrayOffset = 0;
                refreshReadCache( IGNORE_FIELD, row, 0, IGNORE_OFFSET);
                if ((b = skipUntilCachedRow( row)) == null)
                    throw new ConnException( CmdResult.GENERAL_ERR);

                if ((elementsCount != b.getLong()) || (arrayOffset != b.getLong())) {
                    throw new ConnException( CmdResult.GENERAL_ERR);
                }
            }

            while (arrayOffset < elementsCount) {
                if (b.position() >= mFrame.getLastPosition()) {
                    refreshReadCache( IGNORE_FIELD, row, arrayOffset, IGNORE_OFFSET);

                    if ((b = skipUntilCachedRow( row)) == null)
                        throw new ConnException( CmdResult.GENERAL_ERR);

                    if ((elementsCount != b.getLong()) || (arrayOffset != b.getLong())) {
                        throw new ConnException( CmdResult.GENERAL_ERR);
                    }
                }
                result.add( Value.createBasic( ValueType.create( type), b.array(), b.position()));

                while (b.get() != 0)
                    ; // Just let the buffer advance
                ++arrayOffset;
            }
            return result;
        } else if (ValueType.getBaseType( type) == ValueType.TEXT) {
            long charsCount = b.getLong();

            if (charsCount == 0)
                return Value.createBasic( ValueType.textType());

            long charOffset = b.getLong();
            String text = "";
            if (charOffset != 0) {
                charOffset = 0;
                refreshReadCache( IGNORE_FIELD, row, IGNORE_OFFSET, 0);
                if ((b = skipUntilCachedRow( row)) == null)
                    throw new ConnException( CmdResult.GENERAL_ERR);

                if ((charsCount != b.getLong()) || (charOffset != b.getLong())) {
                    throw new ConnException( CmdResult.GENERAL_ERR);
                }

            }

            while (charOffset < charsCount) {
                if (b.position() >= mFrame.getLastPosition()) {
                    refreshReadCache( IGNORE_FIELD, row, IGNORE_OFFSET, charOffset);
                    if ((b = skipUntilCachedRow( row)) == null)
                        throw new ConnException( CmdResult.GENERAL_ERR);

                    if ((charsCount != b.getLong()) || (charOffset != b.getLong())) {
                        throw new ConnException( CmdResult.GENERAL_ERR);
                    }
                }

                int startOffset = b.position();
                while (b.get() != 0)
                    ; // Just let the buffer position to advance
                int endOffset = b.position() - 1;

                String t = new String( b.array(), startOffset, endOffset - startOffset, StandardCharsets.UTF_8);
                charOffset += t.codePointCount( 0, t.length());
                text += t;
            }
            return Value.createBasic( ValueType.textType(), text);
        }

        return Value.createBasic( ValueType.create( ValueType.getBaseType( type)), b.array(), b.position());
    }

    /**
     * Gets content from the top of stack value.
     * <p>
     * Used mainly to get the cells of a table (or field) resulted after a
     * WHAIS procedure call. The cell's retrieval is made on a individual
     * basis (useful for large results). The caller should already know the
     * table fields by a prior use of {@link #describeStackTop()} and of
     * {@link #retrieveStackTopRowsCount()} for the rows count.</p>
     * <p>
     * In order to cause avoid unnecessary cache miss hits, it is advisable to
     * retrieve the table's cell in the ascending order of rows, and for every
     * row in the ascending order of fields array description resulted after
     * after the call to {@link #describeStackTop()}.</p>
     * <p>
     * Calling this method with the {@code field} set to {@code null}(or
     * {@link #IGNORE_FIELD}) and {@code row} set to {@link #IGNORE_ROW} is
     * equivalent to call directly {@link #retrieveStackTop()}</p>
     *
     *
     * @param field
     *            The table field name. Use {@link #IGNORE_FIELD} when is not
     *            applicable.
     * @param row
     *            The table row. Use {@link #IGNORE_ROW} when is not applicable.
     *
     * @return
     *            An object holding the value content. Should be noted that
     *            returned value should not belong to a resulted table nor a
     *            field (because table cell cannot be another table).
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #describeStackTop()
     * @see #retrieveStackTopRowsCount()
     * @see #retrieveStackTop()
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public Value retrieveStackTop( final String   field,
                                   final long     row) throws IOException
    {
        if (field == null || field.equals( IGNORE_FIELD))
            return (row != IGNORE_ROW) ? retrieveStackTop( row) : retrieveStackTop();

        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        if (mFrame.getCachedResponse() != _c.CMD_READ_STACK_RSP)
            refreshReadCache( field, row, IGNORE_OFFSET, IGNORE_OFFSET);

        ByteBuffer b = mFrame.getCmdBuffer();
        b.position( b.position() + 4);

        assert (field != null && !field.equals( IGNORE_FIELD));

        if (!ValueType.isTable( b.getShort()))
            throw new ConnException( CmdResult.INVALID_FIELD);

        if ((b = skipUntilFieldValue( field, row)) == null) {
            refreshReadCache( field, row, IGNORE_OFFSET, IGNORE_OFFSET);
            if ((b = skipUntilFieldValue( field, row)) == null)
                throw new ConnException( CmdResult.GENERAL_ERR);
        }

        ValueType type = ValueType.create( b.getShort());
        if (type.isArray()) {
            long elementsCount = b.getLong();
            ArrayValue result = Value.createArray( type);

            type = ValueType.create( type.getBaseType());

            if (elementsCount == 0)
                return result;

            long arrayOffset = b.getLong();
            if (arrayOffset != 0) {
                arrayOffset = 0;
                refreshReadCache( field, row, 0, IGNORE_OFFSET);
                if ((b = skipUntilFieldValue( field, row)) == null)
                    throw new ConnException( CmdResult.GENERAL_ERR);

                if ((type.getTypeId() != ValueType.getBaseType( b.getShort()))
                        || (elementsCount != b.getLong())
                        || (arrayOffset != b.getLong())) {
                    throw new ConnException( CmdResult.GENERAL_ERR);
                }
            }

            while (arrayOffset < elementsCount) {
                if (b.position() >= mFrame.getLastPosition()) {
                    refreshReadCache( field, row, arrayOffset, IGNORE_OFFSET);

                    if ((b = skipUntilFieldValue( field, row)) == null)
                        throw new ConnException( CmdResult.GENERAL_ERR);

                    if ((type.getTypeId() != ValueType.getBaseType( b.getShort()))
                            || (elementsCount != b.getLong())
                            || (arrayOffset != b.getLong())) {
                        throw new ConnException( CmdResult.GENERAL_ERR);
                    }
                }
                result.add( Value.createBasic( type, b.array(), b.position()));

                while (b.get() != 0)
                    ; // Just let the buffer advance

                ++arrayOffset;
            }
            return result;
        } else if (type.getBaseType() == ValueType.TEXT) {
            type = ValueType.textType();
            long charsCount = b.getLong();

            if (charsCount == 0)
                return Value.createBasic( type);

            long charOffset = b.getLong();
            String text = "";
            if (charOffset != 0) {
                charOffset = 0;
                refreshReadCache( field, row, IGNORE_OFFSET, 0);
                if ((b = skipUntilFieldValue( field, row)) == null)
                    throw new ConnException( CmdResult.GENERAL_ERR);

                if ((type.getTypeId() != b.getShort())
                        || (charsCount != b.getLong())
                        || (charOffset != b.getLong())) {
                    throw new ConnException( CmdResult.GENERAL_ERR);
                }
            }

            while (charOffset < charsCount) {
                if (b.position() >= mFrame.getLastPosition()) {
                    refreshReadCache( field, row, IGNORE_OFFSET, charOffset);
                    if ((b = skipUntilFieldValue( field, row)) == null)
                        throw new ConnException( CmdResult.GENERAL_ERR);

                    if ((type.getTypeId() != b.getShort())
                            || (charsCount != b.getLong())
                            || (charOffset != b.getLong())) {
                        throw new ConnException( CmdResult.GENERAL_ERR);
                    }
                }

                int startOffset = b.position();
                while (b.get() != 0)
                    ; // Just let the buffer position to advance
                int endOffset = b.position() - 1;

                String t = new String( b.array(), startOffset, endOffset - startOffset, StandardCharsets.UTF_8);
                charOffset += t.codePointCount( 0, t.length());
                text += t;
            }
            return Value.createBasic( type, text);
        }

        type = ValueType.create( type.getBaseType());
        return Value.createBasic( type, b.array(), b.position());
    }

    /**
     * Get the result of a WHAIS procedure execution.
     * <p>
     * The method should be called after a WHAIS procedure was executed in
     * order to retrieve the result of the execution from the top of stack. It
     * will retrieve all the result content (e.g. all text characters, array
     * values, all table and fields cells, etc). If the result is too large
     * once could consider other options like using
     * {@link #retrieveStackTop(String, long)}.</p>
     *
     * @return
     *            A value object holding the execution result.
     *
     * @throws IOException
     * @throws ConnException
     *
     * @see #describeStackTop()
     * @see #retrieveStackTopRowsCount()
     * @see #retrieveStackTop(String, long)
     * @see #executeProcedure(String)
     *
     * @since 1.0
     */
    public Value retrieveStackTop() throws IOException
    {
        if (mFrame.hasPendingCommands())
            throw new ConnException( CmdResult.INCOMPLETE_CMD);

        refreshReadCache( IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, IGNORE_OFFSET);
        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( buffer.position() + 4);

        ValueType type = ValueType.create( buffer.getShort());
        if (type.isTable()) {
            ValueType tableType = describeStackTop();
            final TableFieldType[] fields = tableType.getFields();

            assert fields.length >= 0;

            TableValue result = Value.createTable( fields);

            final long rowsCount = retrieveStackTopRowsCount();
            for (long r = 0; r < rowsCount; ++r) {
                for (TableFieldType f : fields) {
                    Value cellValue = retrieveStackTop( f.getName(), r);
                    result.put( cellValue, f.getName(), (int) r);
                }
            }

            return result;
        } else if (type.isField()) {
            FieldValue result = Value.createField( type);

            final long rowsCount = retrieveStackTopRowsCount();

            assert (rowsCount >= 0);

            for (long r = 0; r < rowsCount; ++r) {
                final Value v = retrieveStackTop( r);
                result.add( v);
            }

            return result;
        } else if (type.isArray()) {
            ValueType baseType = ValueType.create( type.getBaseType());
            ArrayValue result = Value.createArray( type);

            final long elementsCount = buffer.getLong();
            long currentOffset = buffer.getLong();

            while ((0 <= currentOffset) && (currentOffset < elementsCount)) {
                if (buffer.position() >= mFrame.getLastPosition()) {
                    refreshReadCache( IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, currentOffset);
                    buffer = mFrame.getCmdBuffer();
                    buffer.position( buffer.position() + 4 + 2);

                    if ((elementsCount != buffer.getLong())
                            || (currentOffset != buffer.getLong())) {
                        throw new ConnException( CmdResult.GENERAL_ERR, "Unexpected response frame format.");
                    }
                }

                final int startOffset = buffer.position();
                while (buffer.get() != 0)
                    ; // Just let the buffer's position advance.

                result.add( Value.createBasic( baseType, buffer.array(), startOffset));
                ++currentOffset;
            }

            return result;
        } else if (type.equals( ValueType.textType())) {
            final long charsCount = buffer.getLong();

            if (charsCount == 0)
                return Value.createBasic( ValueType.textType());

            long currentOffset = buffer.getLong();
            assert currentOffset == 0;

            String text = "";
            while (currentOffset < charsCount) {
                if (buffer.position() >= buffer.capacity()) {
                    refreshReadCache( IGNORE_FIELD, IGNORE_ROW, IGNORE_OFFSET, currentOffset);
                    buffer = mFrame.getCmdBuffer();
                    buffer.position( buffer.position() + 4);

                    if ((charsCount != buffer.getLong())
                            || (currentOffset != buffer.getLong())) {
                        throw new ConnException( CmdResult.GENERAL_ERR, "Unexpected response frame format.");
                    }
                }

                final int startOffset = buffer.position();
                while (buffer.get() != 0)
                    ; // Just let the buffer's position advance.
                final int endOffset = buffer.position() - 1;

                String t = new String( buffer.array(), startOffset, endOffset - startOffset, StandardCharsets.UTF_8);
                currentOffset += t.codePointCount( 0, t.length());
                text += t;

                assert currentOffset <= charsCount;
            }

            assert text.length() > 0;

            return Value.createBasic( ValueType.textType(), text);
        }

        assert type.isBasic();

        return Value.createBasic( type, buffer.array(), buffer.position());
    }

    private final void updateStackTopBasic( Value value) throws IOException
    {
        assert !value.type().equals( ValueType.textType());
        assert !(value.type().isArray() || value.type().isField() || value.type().isTable());

        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        final byte[] v = value.toString().getBytes( StandardCharsets.UTF_8);
        if (buffer.position() + 1 + 2 + v.length + 1 > buffer.capacity()) {
            assert mFrame.hasPendingCommands();

            mFrame.flushPendingCommand();

            buffer = mFrame.getCmdBuffer();
        }

        buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
              .putShort( value.type().getTypeId())
              .put( v)
              .put( (byte) 0);

        mFrame.markBufferPositionValid();
        mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
    }

    private final void updateStackTopBasic( Value       value,
                                            String      fieldName,
                                            long        row) throws IOException
    {
        assert !value.type().equals( ValueType.textType());
        assert !(value.type().isArray() || value.type().isField() || value.type().isTable());
        assert !value.isNull();

        byte[] f = fieldName.getBytes( StandardCharsets.UTF_8);
        byte[] v = value.toString().getBytes( StandardCharsets.UTF_8);

        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        final int spaceReq = 1 + 2 + f.length + 1 + 8 + v.length + 1;
        if (buffer.position() + spaceReq > buffer.capacity()) {
            if (mFrame.hasPendingCommands())
                mFrame.flushPendingCommand();

            buffer = mFrame.getCmdBuffer();

            if (buffer.position() + spaceReq > buffer.capacity())
                throw new ConnException( CmdResult.LARGE_ARGS, "Command to large to update the field value.");
        }

        buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
              .putShort( (short) (value.type().getTypeId() | ValueType.FIELD_MASK))
              .put( f)
              .put( (byte) 0)
              .putLong( row)
              .put( v)
              .put( (byte) 0);

        mFrame.markBufferPositionValid();
        mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
    }

    private final void updateStackTopArray( Value[]     values,
                                            long        arrayOffset) throws IOException
    {
        assert (values != null) && (values.length > 0);

        for (int i = 1; i < values.length; ++i) {
            if (!values[i].type().equals( values[i - 1].type())) {
                throw new ConnException( CmdResult.INVALID_ARGS,
                                         "The array's elements used to update the stack top are of "
                                         + "different types.");
            }
        }

        assert values[0].type().isBasic();

        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        int subCmdOffset = buffer.position(), count = 0;
        for (Value value : values) {
            assert !value.isNull();

            final byte[] v = value.toString().getBytes( StandardCharsets.UTF_8);
            final int spaceReq = v.length + ((count == 0) ? (1 + 2 + 2 + 8 + 1) : 1);

            if ((buffer.position() + spaceReq > buffer.capacity()) || (count >= 0xFFFF)) {
                assert mFrame.hasPendingCommands();

                mFrame.flushPendingCommand();

                buffer = mFrame.getCmdBuffer();
                subCmdOffset = buffer.position();
                count = 0;
            }

            if (count++ == 0) {
                buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
                      .putShort( (short) (value.type().getTypeId() | ValueType.ARRAY_MASK))
                      .putShort( (short) count) // Just reserve space
                      .putLong( arrayOffset);
            } else
                buffer.putShort( subCmdOffset + 3, (short) count);

            buffer.put( v)
                  .put( (byte) 0);
            ++arrayOffset;

            mFrame.markBufferPositionValid();
            mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
        }
    }

    private final void updateStackToArray( Value[]      values,
                                           long         arrayOffset,
                                           String       fieldName,
                                           long         row) throws IOException
    {
        assert (values != null) && (values.length > 0);

        for (int i = 1; i < values.length; ++i) {
            if (!values[i].type().equals( values[i - 1].type())) {
                throw new ConnException( CmdResult.INVALID_ARGS,
                                         "The array's elements used to update the stack top are of "
                                         + "different types.");
            }
        }

        assert values[0].type().isBasic();

        byte[] f = fieldName.getBytes( StandardCharsets.UTF_8);
        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        int subCmdOffset = buffer.position(), count = 0;
        for (Value value : values) {
            assert !value.isNull();

            final byte[] v = value.toString().getBytes( StandardCharsets.UTF_8);
            final int spaceReq = ((count == 0) ? (1 + 2 + f.length + 1 + 8 + 2 + 8) : 0) + v.length + 1;

            if ((buffer.position() + spaceReq > buffer.capacity()) || (count >= 0xFFFF)) {
                if (mFrame.hasPendingCommands()) {
                    mFrame.flushPendingCommand();

                    buffer = mFrame.getCmdBuffer();
                    subCmdOffset = buffer.position();

                    count = 0;
                }

                if (buffer.position() + spaceReq > buffer.capacity())
                    throw new ConnException( CmdResult.LARGE_ARGS, "Field name too long to allow a value update.");
            }

            if (count++ == 0) {
                buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
                      .putShort( (short) (value.type().getTypeId() | ValueType.ARRAY_MASK | ValueType.FIELD_MASK))
                      .put( f)
                      .put( (byte) 0)
                      .putLong( row)
                      .putShort( (short) count) // Just reserve space
                      .putLong( arrayOffset);
            } else {
                buffer.putShort( subCmdOffset + 1 + 2 + f.length + 1 + 8, (short) count);
            }

            buffer.put( v)
                  .put( (byte) 0);
            ++arrayOffset;

            mFrame.markBufferPositionValid();
            mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
        }
    }

    private final void updateStackTopText( String       value,
                                           long         textOffset) throws IOException
    {
        final byte[] text = value.getBytes( StandardCharsets.UTF_8);

        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        if (buffer.position() + 1 + 2 + 8 + 4 + 1 > buffer.capacity()) {
            // + 4 -> max Unicode code units count in utf8
            // + 1 -> the ending 0
            assert mFrame.hasPendingCommands();
            mFrame.flushPendingCommand();

            buffer = mFrame.getCmdBuffer();
        }

        buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
              .putShort( (short) ValueType.TEXT)
              .putLong( textOffset);

        int textCount = 0;
        while (textCount < text.length) {
            final int cuCount = utf8CUCount( text[textCount]);
            if (buffer.position() + cuCount + 1 > buffer.capacity()) {
                assert textCount > 0;
                assert mFrame.hasPendingCommands();

                buffer.put( (byte) 0);
                mFrame.markBufferPositionValid();
                mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);

                mFrame.flushPendingCommand();
                mFrame.discardCommandBuffer();

                buffer = mFrame.getCmdBuffer();

                buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
                      .putShort( (short) ValueType.TEXT)
                      .putLong( textOffset);
            } else {
                buffer.put( text, textCount, cuCount);
                ++textOffset;
                textCount += cuCount;
            }
        }

        assert textCount == text.length;

        buffer.put( (byte) 0);
        mFrame.markBufferPositionValid();
        mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
    }

    private final void updateStackTopText( String       value,
                                           long         textOffset,
                                           String       fieldName, long row) throws IOException
    {
        final byte[] text = value.getBytes( StandardCharsets.UTF_8);
        final byte[] f = fieldName.getBytes( StandardCharsets.UTF_8);

        final int minSpace = 1 + 2 + f.length + 1 + 8 + 8 + 4 + 1;

        ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.position( mFrame.getLastPosition());

        if (buffer.position() + minSpace > buffer.capacity()) {
            // + 4 -> max Unicode code units count in utf8
            // + 1 -> the ending 0
            assert mFrame.hasPendingCommands();
            mFrame.flushPendingCommand();

            buffer = mFrame.getCmdBuffer();

            if (buffer.position() + minSpace > buffer.capacity())
                throw new ConnException( CmdResult.LARGE_ARGS, "Field name too long to allow a value update.");
        }

        buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
              .putShort( (short) (ValueType.TEXT & ValueType.FIELD_MASK))
              .put( f)
              .put( (byte) 0)
              .putLong( row)
              .putLong( textOffset);

        int textCount = 0;
        while (textCount < text.length) {
            final int cuCount = utf8CUCount( text[textCount]);
            if (cuCount == 0)
                throw new ConnException( CmdResult.GENERAL_ERR);

            if (buffer.position() + minSpace + cuCount + 1 > buffer.capacity()) {
                assert textCount > 0;
                assert mFrame.hasPendingCommands();

                mFrame.flushPendingCommand();

                buffer = mFrame.getCmdBuffer();

                buffer.put( _c.CMD_UPDATE_FUNC_CHTOP)
                      .putShort( (short) (ValueType.TEXT & ValueType.FIELD_MASK))
                      .put( f)
                      .put( (byte) 0)
                      .putLong( row)
                      .putLong( textOffset);
            }

            buffer.put( text, textCount, cuCount);
            buffer.put( (byte) 0);
            ++textOffset;
            textCount += cuCount;

            mFrame.markBufferPositionValid();
            mFrame.setPendingCommand( _c.CMD_UPDATE_STACK);
        }

        assert textCount == text.length;
    }

    private final void refreshReadCache( String    field,
                                         long      rowHint,
                                         long      arrayOffsetHint,
                                         long      textOffsetHint) throws IOException
    {
        assert !mFrame.hasPendingCommands();

        final int reqSize = ((field == null) ? 0 : field.length()) + 1 + 8 + 8 + 8;
        if (reqSize > mFrame.maxCmdSize())
            throw new ConnException( CmdResult.LARGE_ARGS);

        mFrame.discardCommandBuffer();

        final ByteBuffer buffer = mFrame.getCmdBuffer();
        buffer.put( field.getBytes( StandardCharsets.UTF_8))
              .put( (byte) 0)
              .putLong( rowHint)
              .putLong( arrayOffsetHint)
              .putLong( textOffsetHint);

        mFrame.markBufferPositionValid();
        mFrame.sendCommand( _c.CMD_READ_STACK);
        final int reponse = mFrame.getCmdBuffer().getInt();
        if (reponse != CmdResult.OK)
            throw new ConnException( reponse);
    }

    private final ByteBuffer skipUntilCachedRow( long row) throws ConnException
    {
        assert mFrame.getCachedResponse() == _c.CMD_READ_STACK_RSP;

        final ByteBuffer b = mFrame.getCmdBuffer();
        b.position( b.position() + 4);

        final short type = b.getShort();

        if (row >= b.getLong())
            throw new ConnException( CmdResult.INVALID_ROW);

        long currentRow = b.getLong();
        if (currentRow > row)
            return null;

        if (ValueType.isTable( type)) {
            int fieldsCount = b.getShort();
            fieldsCount &= 0xFFFF; // Sign correction

            while (currentRow++ < row) {
                if (b.position() >= mFrame.getLastPosition())
                    return null;

                for (int i = 0; i < fieldsCount; ++i) {
                    if (!skipCachedFieldValue( b, mFrame.getLastPosition())) {
                        return null;
                    }
                }
            }
        } else if (ValueType.isField( type) && ValueType.isArray( type)) {
            while (currentRow++ < row) {
                if (b.position() >= mFrame.getLastPosition())
                    return null;

                final long elemsCount = b.getLong();
                if (elemsCount == 0)
                    break;

                long offset = b.getLong();
                while (offset++ < elemsCount) {
                    if (b.position() >= mFrame.getLastPosition())
                        return null;

                    while (b.get() != 0)
                        ; // Just let the buffer position to advance.
                }
            }
        } else if (ValueType.isField( type) && (ValueType.getBaseType( type) == ValueType.TEXT)) {
            while (currentRow++ < row) {
                if (b.position() >= mFrame.getLastPosition())
                    return null;

                final long charsCount = b.getLong();
                if (charsCount == 0)
                    break;

                long offset = b.getLong();

                assert (offset < charsCount);

                while (b.get() != 0)
                    ; // Just let the buffer's position to advance.
            }
        } else {
            while (currentRow++ < row) {
                if (b.position() >= mFrame.getLastPosition())
                    return null;

                while (b.get() != 0)
                    ; // Just let the buffer's position to advance.
            }
        }

        if (b.position() >= mFrame.getLastPosition())
            return null;

        return b;
    }

    private final ByteBuffer skipUntilFieldValue( String   field,
                                                  long     row) throws ConnException
    {
        ByteBuffer b = skipUntilCachedRow( row);

        if (b == null)
            return null;

        if ( ! skipUntilCachedField( field, b, mFrame.getLastPosition()))
            return null;

        while (b.get() != 0)
            ; // Ignore the field name

        return b;
    }

    private final ValueType internalDescribeValue( String name) throws IOException
    {
        assert name != null;

        ByteBuffer buffer = null;
        TableFieldType[] fields = null;
        int field = 0, fieldsCount = 0;
        short type = 0;
        do {
            if ((field == 0) || (buffer.position() >= mFrame.getLastPosition())) {
                mFrame.discardCommandBuffer();
                buffer = mFrame.getCmdBuffer();

                buffer.putShort( (short) field) // Field hint
                      .putShort( (short) 0) // Reserved
                      .put( name.getBytes( StandardCharsets.UTF_8))
                      .put( (byte) 0);
                mFrame.markBufferPositionValid();
                mFrame.sendCommand( _c.CMD_GLOBAL_DESC);
                buffer = mFrame.getCmdBuffer();

                int cmdResult = buffer.getInt();
                if (cmdResult != CmdResult.OK)
                    throw new ConnException( cmdResult);

                while (buffer.get() != 0)
                    ; // Ignore the global variable name

                if (field == 0) {
                    type = buffer.getShort();

                    if (!ValueType.isTable( type))
                        return ValueType.create( type);

                    fieldsCount = buffer.getShort();
                    fieldsCount &= 0x0000FFFF; // Sign correction

                    if (fieldsCount == 0)
                        return ValueType.create( type);

                    fields = new TableFieldType[fieldsCount];
                    buffer.position( buffer.position() - (2 + 2));
                }

                if ((type != buffer.getShort())
                        || (fieldsCount != (buffer.getShort() & 0x0000FFFF))
                        || (field != (buffer.getShort() & 0x0000FFFF))) {
                    throw new ConnException( CmdResult.GENERAL_ERR, "Unexpected answer format received from server.");
                }
            }

            final int startOffset = buffer.position();
            while (buffer.get() != 0)
                ; // Just let the buffer's position advance.
            final int endOffset = buffer.position() - 1;

            assert startOffset < endOffset;

            fields[field] = new TableFieldType( new String( buffer.array(),
                                                            startOffset,
                                                            endOffset - startOffset,
                                                            StandardCharsets.UTF_8),
                                                ValueType.create( buffer.getShort()));
        } while (++field < fieldsCount);

        mFrame.discardCommandBuffer();
        if ((fields != null) && (fields.length > 0))
            return ValueType.create( fields);

        return ValueType.create( type);
    }

    private static final boolean skipCachedFieldValue( ByteBuffer b,
                                                       final int  toPosition) throws ConnException
    {
        if (b.position() >= toPosition)
            return false;

        while (b.get() != 0)
            ; // Just let the buffer position advance

        final short type = b.getShort();

        if (ValueType.isArray( type)) {
            final long elemsCount = b.getLong();
            if (elemsCount > 0) {
                long offset = b.getLong();

                assert offset < elemsCount;

                while (offset < elemsCount) {
                    if (b.position() >= toPosition)
                        return false;

                    while (b.get() != 0)
                        ; // Just let the buffer's position advance;

                    ++offset;
                }
            }
        } else if (type == ValueType.TEXT) {
            final long charsCount = b.getLong();
            if (charsCount > 0) {
                b.position( b.position() + 8);
                while (b.get() != 0)
                    ; // Just let the buffer advance
            }
        } else if (ValueType.isBasic( type)) {
            while (b.get() != 0)
                ; // Just let the buffer advance
        } else
            throw new ConnException( CmdResult.GENERAL_ERR);

        return true;
    }

    private static final boolean skipUntilCachedField( String       f,
                                                       ByteBuffer   b,
                                                       final int    toPosition) throws ConnException
    {
        assert f.length() > 0;

        byte[] fb = f.getBytes( StandardCharsets.UTF_8);

        do {
            if (b.position() >= toPosition)
                return false;

            int fieldStart = b.position(), offset = 0;
            while (offset < fb.length) {
                if (b.get() != fb[offset])
                    break;

                ++offset;
            }

            if ((offset == fb.length) && (b.get() == 0)) {
                b.position( fieldStart);
                return true;
            } else
                b.position( fieldStart);

        } while (skipCachedFieldValue( b, toPosition));

        return false;
    }

    private static final int utf8CUCount( final byte firstCodeUnit)
    {
        short unit = firstCodeUnit;
        unit &= 0x00FF; // Sign correction.

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

    /**
     * Used to connect as a database administrator.
     */
    public static final byte ADMIN = 0;

    /**
     * Used to connect as a regular user.
     */
    public static final byte USER = 1;

    /**
     * Used to clear all stack values.
     */
    public static final int ALL = -1;

    /**
     * Used when a row index is not applicable.
     */
    public static final long IGNORE_ROW = -1;

    /**
     * Used when a field name is not applicable.
     */
    public static final String IGNORE_FIELD = "";

    private static final long     IGNORE_OFFSET = -1;
    private static final String[] emptyList     = new String[0];

    private CommunicationFrame    mFrame;
    private byte                  mUserId;
}