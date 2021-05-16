package initialize;

import controller.menu.DeckMenuController;
import controller.menu.LoginMenuController;
import utils.IntegrationTestBase;

public class Sample extends IntegrationTestBase {
    public void initializeUser(String username, String password, String nickname) {
        run(String.format("user create -u %s -p %s -n %s", username, password, nickname));
        run(String.format("user login -u %s -p %s", username, password));
        run("menu enter shop");
        run("shop show --all");
        for (int i = 1; i <= 3; i++) {
            run("shop buy AxeRaider");
            run("shop buy BattleOx");
            run("shop buy Fireyarou");
            run("shop buy HornImp");
            run("shop buy SilverFang");
        }
        run("menu exit");
        run("menu enter deck");
        String deckName = username + "Deck";
        run(String.format("deck create %s", deckName));
        for (int i = 1; i <= 3; i++) {
            run(String.format("deck add-card --card AxeRaider --deck %s", deckName));
            run(String.format("deck add-card --card BattleOx --deck %s", deckName));
            run(String.format("deck add-card --card Fireyarou --deck %s", deckName));
            run(String.format("deck add-card --card HornImp --deck %s", deckName));
            run(String.format("deck add-card --card SilverFang --deck %s", deckName));
        }
        run(String.format("deck set-active %s", deckName));
        checkCurrentMenu(DeckMenuController.class);
        checkNoInvalidCommandsInBuffer();
        run("menu exit");
        run("user logout");
        checkCurrentMenu(LoginMenuController.class);
        checkNoInvalidCommandsInBuffer();
    }

}
