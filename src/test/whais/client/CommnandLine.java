package test.whais.client;

public class CommnandLine
{
    public CommnandLine (String[] args)
    {
        try 
        {
            for (int i = 0; i < args.length; ++i)
            {
                if (args[i].equals(ARG_ROOT))
                    userId = 0;
                
                else if (args[i].equals(ARG_DATABASE))
                    this.database = args[++i];
                
                else if (args[i].equals (ARG_PASSWORD))
                    this.password = args[++i];
                
                else if (args[i].equals (ARG_PORT))
                    this.port = args[++i];
                
                else if (args[i].equals(ARG_HOST_NAME))
                    this.hostname = args[++i];
                
                else
                {
                    System.out.println ("What should I do with '" + args[i] + '?');  
                    printUsage ();
                }
            }
        } catch (Throwable e) {
            printUsage ();
        }

        if (password == null)
            password = (userId == 0) ? "root_test_password" : "test_password";

        System.out.println ( "Hostname: " + hostname);
        System.out.println ( "Port: " + port);
        System.out.println ( "Database: " + database);
        System.out.println ( "User: " + userId);
        System.out.println ( "Password: " + password);
    }
    
    public String getDatabase ()
    {
        return this.database;
    }

    public String getPassword ()
    {
        return this.password;
    }

    public String getHostname ()
    {
        return this.hostname;
    }

    public String getPort ()
    {
        return this.port;
    }

    public int getUserId ()
    {
        return this.userId;
    }
    
    private void printUsage ()
    {
        System.out.println ("(JavaTestClass) " +
                            ARG_HOST_NAME      + " hostname\n "       +
                            ARG_PORT           + " port\n "           +
                            ARG_DATABASE       + " databasename\n "   +
                            ARG_ROOT           + '\n'                 +
                            ARG_PASSWORD       + " password");
        System.exit (0);
    }
    
    
    private String      database  = "test_list_db";
    private String      password  = null;
    private String      hostname  = "localhost";
    private String      port      = "1761";
    private int         userId    = 1;
    
    private static final String ARG_ROOT              = "--root";
    private static final String ARG_HOST_NAME         = "-h";
    private static final String ARG_PORT              = "-p";
    private static final String ARG_DATABASE          = "-d";
    private static final String ARG_PASSWORD          = "-k"; 
}
