package test.net.whais.Client;

public class CommnandLine
{
    public CommnandLine (String[] args)
    {
        try
        {
            for (int i = 0; i < args.length; ++i)
            {
                if (args[i].equals(ARG_ROOT))
                    this.userId = 0;

                else if (args[i].equals(ARG_DATABASE))
                    this.database = args[++i];

                else if (args[i].equals (ARG_PASSWORD))
                    this.password = args[++i];

                else if (args[i].equals (ARG_PORT))
                    this.port = args[++i];

                else if (args[i].equals(ARG_HOST_NAME))
                    this.hostname = args[++i];

                else if (args[i].equals(ARG_FRAME_SIZE))
                    this.frameSize = Integer.parseInt (args[++i]);

                else
                {
                    System.out.println ("What should I do with '" + args[i] + '?');
                    this.printUsage ();
                }
            }
        } catch (Throwable e) {
            this.printUsage ();
        }

        if (this.password == null)
            this.password = (this.userId == 0) ? "root_test_password" : "test_password";

        System.out.println ( "Hostname:     " + this.hostname);
        System.out.println ( "Port:         " + this.port);
        System.out.println ( "Database:     " + this.database);
        System.out.println ( "User:         " + this.userId);
        System.out.println ( "Password:     " + this.password);
        System.out.println ( "maxFrameSize: " + this.frameSize);
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

    public int getMaxFrameSize ()
    {
        return this.frameSize;
    }

    private void printUsage ()
    {
        System.out.println ("(JavaTestClass) " + '\n'                 +
                            ARG_HOST_NAME      + "\thostname\n"       +
                            ARG_PORT           + "\tport\n"           +
                            ARG_FRAME_SIZE     + "\tsize\n"           +
                            ARG_DATABASE       + "\tdatabase\n"       +
                            ARG_ROOT           + '\n'                 +
                            ARG_PASSWORD       + " password");
        System.exit (0);
    }


    private String      database  = "";
    private String      password  = null;
    private String      hostname  = "localhost";
    private String      port      = "1761";
    private int         userId    = 1;
    private int         frameSize = Integer.MAX_VALUE;

    private static final String ARG_ROOT              = "--root";
    private static final String ARG_HOST_NAME         = "-h";
    private static final String ARG_PORT              = "-p";
    private static final String ARG_DATABASE          = "-d";
    private static final String ARG_PASSWORD          = "-k";
    private static final String ARG_FRAME_SIZE        = "--fs";
}
