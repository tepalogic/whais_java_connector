
import java.io.IOException;

import test.whais.client.CommnandLine;
import net.whais.Client.Connection;
import net.whais.Client.ProcedureParameters;


public class ConnectorTest {
	public static void main (String[] args) throws IOException {
	    CommnandLine cmdLine = new CommnandLine (args);
	    
        System.out.print ("Program started.\n");
        
        Connection c = new Connection (cmdLine.getHostname (),
                                       cmdLine.getPort (),
                                       cmdLine.getDatabase (),
                                       cmdLine.getPassword (),
                                       (byte) cmdLine.getUserId ());

        System.out.print ("Connection successful. Sending ping command ...\n");
        c.pingServer();

        System.out.print ("Sending second ping command ...\n");
        c.pingServer();
        
        String[] glbsNames = c.retrieveGlobalNames ();
        if ((glbsNames == null) || glbsNames.length == 0)
            System.out.println ("There are no global values defined.");
        else
        {
            System.out.println ("There are " + glbsNames.length + " global value(s) defined: ");
            for (String s : glbsNames)
            {
                System.out.println ('\t' + s + ' ' + c.describeGlobal (s).typeAsString ());
            }
        }
        
        
        String[] procsNames = c.retrieveProceduresNames ();
        if ((procsNames == null) || procsNames.length == 0)
            System.out.println ("There are no procedures defined.");
        else
        {
            System.out.println ("There are " + procsNames.length + " procedure(s) defined: ");
            int procIndex = 0;
            for (String s : procsNames)
            {
                System.out.println ("[" + procIndex++ + "]\t" + s);
                ProcedureParameters p = c.describeProcedure (s);
                
                System.out.println("\t\tReturn Type: " + p.describeReturnValue ().typeAsString ());
                for (int i = 1; i <= p.count (); ++ i)
                    System.out.println("\t\tParam " + i +": " + p.describeParameter (i).typeAsString ());              
            }
        }

        System.out.print ("Closing connection ...\n");
        c.close ();

        System.out.print ("The End!\n");
	}
}
