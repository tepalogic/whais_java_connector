import java.io.IOException;
import java.util.Arrays;

import net.whais.Client.CmdResult;
import net.whais.Client.ConnException;
import net.whais.Client.Connection;
import net.whais.Client.TableValue;
import net.whais.Client.Value;
import test.net.whais.Client.CommnandLine;


public class TestSyncStatements
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

        final CommnandLine cmdLine = new CommnandLine (customArgs);
        Connection c = new Connection (cmdLine.getHostname (),
                                       cmdLine.getPort (),
                                       cmdLine.getDatabase (),
                                       cmdLine.getPassword (),
                                       (byte) cmdLine.getUserId (),
                                       cmdLine.getMaxFrameSize ());

        boolean testResult = true;
        final TestSyncStatements test = new TestSyncStatements ();

        c.executeProcedure ("SyncStatementsTestSetup");
        c.pingServer();
        Thread[] t = new Thread[THREADS_COUNT];

        for (int i = 0; i < t.length; ++i) {
            final int alias_i = i;
            t[i] = new Thread( new Runnable() {
                @Override public void run () {
                    try {
                        test.testWorkerTask (cmdLine, alias_i);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            t[i].start ();
        }

        for (int i = 0; i < t.length; ++i) {
            try {
                t[i].join ();
            } catch (InterruptedException e) {
                System.out.println ("Exception signaled while joining thread " + i);
                testResult = false;
            }
        }

        c.pingServer ();

        testResult &= test.testTableContent (c);

        c.pingServer ();
        c.close ();

        if ( ! testResult) {
            System.out.println ("TEST RESULT: FAIL");
            System.exit (1);
        }

        System.out.println ("TEST RESULT: PASS");
    }

    boolean testWorkerTask (CommnandLine cmdLine, int threadId) throws IOException
    {
        System.out.println ("Starting thread " + threadId + " ... ");
        Connection c = new Connection (cmdLine.getHostname (),
                cmdLine.getPort (),
                cmdLine.getDatabase (),
                cmdLine.getPassword (),
                (byte) cmdLine.getUserId (),
                cmdLine.getMaxFrameSize ());
        for (int i = 0; i < THREAD_REQUESTS; ++i) {
            c.popStackValues( Connection.ALL);
            try
            {
                c.executeProcedure ("HandleSyncTestRequest");
            } catch (ConnException e) {
                if (e.getCode() != CmdResult.PROC_RUNTIME_ERR)
                    throw e;
            }
        }

        c.pingServer();
        c.close ();

        System.out.println ("Ending thread " + threadId + '!');
        return false;
    }

    boolean testTableContent (Connection c) throws IOException
    {
        c.executeProcedure ("GetSyncTestRequestsCount");
        Value v = c.retrieveStackTop ();

        final int requestsExpected = THREADS_COUNT * THREAD_REQUESTS;
        if (v.isNull()
            || (Integer.parseInt (v.toString()) != requestsExpected)) {

            System.out.println ("The test executed '" + v + "' times rather than '" + requestsExpected + '\'');
            return false;
        }
        final int requestsCount = Integer.parseInt (v.toString());

        c.executeProcedure ("GetSyncTestEntryCount");
        v = c.retrieveStackTop ();
        if (v.isNull()) {
            System.out.println ("No actual sync updates were made! I don't believe it!");
            return false;
        }

        final int tableRows = Integer.parseInt (v.toString ());
        if (tableRows >= requestsCount) {
            System.out.println ("There aren't less table rows than actual requests count "
                                + '(' + tableRows + " vs. " + requestsCount + "). Hard to believe!");
            return false;
        }

        System.out.println ("Testing table content ... ");
        c.executeProcedure ("GetSyncTestTable");
        v = c.retrieveStackTop ();
        if (v.isNull()) {
            System.out.println ("The resulted test table is null.");
            return false;
        }

        TableValue t = (TableValue)v;
        if (t.getRowsCount () != tableRows) {
            System.out.println ("The resulted test table has " + t.getRowsCount() + "rather than " + tableRows + "rows.");
            return false;
        }

        for (int r = 0; r < tableRows; ++r) {
            final Value id = t.get( "entryId", r);
            final Value rndValue = t.get( "rndValue", r);

            if (r != Integer.parseInt (id.toString())) {
                System.out.println ("At row " + r + "unexpected value for entry id " + id);
                return false;
            }

            if (Integer.parseInt (rndValue.toString()) > r ) {
                System.out.println ("At row " + r + " unexpected value for random value " + rndValue);
                return false;
            }
        }



        return true;
    }


    public final static int THREADS_COUNT   = 30;
    public final static int THREAD_REQUESTS = 100;
}
