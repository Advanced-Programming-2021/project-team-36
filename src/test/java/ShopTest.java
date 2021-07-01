import YuGiOh.controller.ProgramController;
import org.junit.Test;
import utils.IntegrationTestBase;

public class ShopTest extends IntegrationTestBase {
    @Test
    public void test() {
        String username = "shayan", password = "1234", nickname = "Shayan.P";

        run(String.format("user create -u %s -p %s -n %s", username, password, nickname));
        run(String.format("user login -u %s -p %s", username, password));
        run("cheat increase --balance 18931jhad");
        checkEqualExact("invalid number\n");
        run("cheat increase --balance 1893189");
        run("menu enter shop");
        run("shop show --all");
        run("shop buy AxeRaider");
        run("shop buy AxeRaider");
        run("shop buy AxeRaider");
    }
}

