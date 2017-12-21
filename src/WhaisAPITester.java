import java.io.IOException;

import net.whais.Client.Connection;

public interface WhaisAPITester
{
    boolean DoTest(final Connection c) throws IOException;
};
