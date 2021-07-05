import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menu.MainMenuController;
import YuGiOh.model.enums.AIMode;
import org.junit.Test;
import utils.IntegrationTestBase;

public class MenuTest extends IntegrationTestBase {

    @Test
    public void test() {
        String username = "shayan", password = "1234", nickname = "Shayan.P";
        run(String.format("user create -u %s -p %s -n %s", username, password, nickname));
        run(String.format("user login -u %s -p %s", username, password));
        run("menu exit");
        run(String.format("user login -u %s -p %s", username, password));
        run("menu enter ajhdja");
        run("menu enter login");
        run("menu enter main");
        run("menu enter scoreboard");
        run("menu exit");
        run("menu enter scoreboard");
        run("menu enter login");
        run("menu enter scoreboard");
        run("menu enter scorebo13d");
        run("menu enter main");
        try {
            MainMenuController.getInstance().startDuelAIWithAI(1, AIMode.AGGRESSIVE, AIMode.NORMAL);
        } catch (Exception exception) {
        }


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

