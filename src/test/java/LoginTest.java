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

    @Test
    public void existingUsernameOrNickname() {
        run("user create -u boz -p 93133 -n goosale");
        checkEqualExact("user created successfully!\n");
        run("user create -u boz -p 19139 -n aidjkd");
        checkEqualExact("user with username boz already exists\n");
        run("user create -u khar -p 9139 -n goosale");
        checkEqualExact("user with nickname goosale already exists\n");
    }

    @Test
    public void nonExistingUser() {
        run("user login -u bozghale -p 93133");
        checkEqualExact("Username and password didn’t match!\n");
        run("menu enter login");
        checkEqualExact("can't navigate to your current menu!\n");
        run("menu enter Main");
        checkEqualExact("please login first\n");
    }

    @Test
    public void cheateLogin() {
        run("user cheat -u ajdkda -p 10083 -n18393");
        checkEqualExact("user logged in successfully!\n");
        run("exit");
    }
}
