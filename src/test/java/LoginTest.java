import org.junit.Test;
import utils.IntegrationTestBase;

public class LoginTest extends IntegrationTestBase {
    @Test
    public void invalidCommand() {
        run("salam pesaram khoobi?");
        checkEqualExact("invalid command!\n");
        checkEqualIgnoreNewLine("invalid command!");
    }

    @Test
    public void wrongPassword(){
        run("user create -u shayan -p=1234 -n utils.IntegrationTestBase.P");
        run("user create --password 12345 -u abolfazl --nickname Abolof");
        run("user login --username shayan -p 123");
        checkNoInvalidCommandsInBuffer();
    }
}
