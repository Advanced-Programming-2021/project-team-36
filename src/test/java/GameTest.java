import controller.GameController;
import controller.menu.DeckMenuController;
import controller.menu.LoginMenuController;
import controller.menu.MainMenuController;
import model.enums.Phase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class GameTest extends Shayan {
    @Test
    public void test() {
        initializeUser("abolfazl", "1234", "atreus");
        initializeUser("shayan", "1234", "shayan.p");
        startNewDuel("abolfazl", "1234", "shayan");
    }

    void startNewDuel(String username, String password, String opponentUsername) {
        run(String.format("user login -u %s -p %s", username, password));
        runUntilNoInput();
        checkCurrentMenu(MainMenuController.class);
        run(String.format("duel --new --second_player %s --round 1", opponentUsername));
        run("select --hand 1");
        run("summon");
        run("show board");
        run("next phase");
        run("next phase");
        Assertions.assertEquals(GameController.getInstance().getGame().getPhase(), Phase.MAIN_PHASE1);
        run("show hand");
        run("select --hand 1");
        run("summon");
        run("show board");
        run("card show --selected");
        run("next phase");
        Assertions.assertEquals(GameController.getInstance().getGame().getPhase(), Phase.BATTLE_PHASE);
        checkNoInvalidCommandsInBuffer();
    }

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
