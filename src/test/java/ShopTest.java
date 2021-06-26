import org.junit.Test;
import utils.IntegrationTestBase;

public class ShopTest extends IntegrationTestBase {
    @Test
    public void test() {
        String username = "shayan", password = "1234", nickname = "Shayan.P";

        run(String.format("user create -u %s -p %s -n %s", username, password, nickname));
        run(String.format("user login -u %s -p %s", username, password));
        run("cheat increase --balance 18931jhad");
        run("cheat increase --balance 1893189");
        run("menu enter shop");
        run("shop show --all");
        run("shop buy AxeRaider");
        run("shop buy AxeRaider");
        run("shop buy AxeRaider");

//        for (int i = 1; i <= 3; i++) {
//            run("shop buy BattleOx");
//            run("shop buy Fireyarou");
//            run("shop buy HornImp");
//            run("shop buy SilverFang");
//        }
//        run("menu exit");
//        run("menu enter deck");
//        String deckName = username + "Deck";
//        run(String.format("deck create %s", deckName));
//        run(String.format("deck add-card --card AxeRaider --deck %s", deckName));
    }
}

