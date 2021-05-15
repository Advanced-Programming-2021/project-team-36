import controller.GameController;
import controller.ProgramController;
import controller.menu.DeckMenuController;
import controller.menu.DuelMenuController;
import controller.menu.LoginMenuController;
import controller.menu.MainMenuController;
import model.enums.Phase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import Utils.Debugger;

public class GameTest extends Shayan {
    @Test
    public void test() {
        initializeUser("abolfazl", "1234", "atreus");
        initializeUser("shayan", "1234", "shayan.p");
        startNewDuel("abolfazl", "1234", "shayan");
    }

    void startNewDuel(String username, String password, String opponentUsername) {
        inject(String.format("user login -u %s -p %s", username, password));
        runUntilNoInput();
        checkCurrentMenu(MainMenuController.class);
        inject(String.format("duel --new --second_player %s --round 1", opponentUsername));
        inject("select --hand 1");
        inject("summon");
        inject("show board");
        inject("next phase");
        inject("next phase");
        Assertions.assertEquals(GameController.getInstance().getGame().getPhase(), Phase.MAIN_PHASE1);
        inject("show hand");
        inject("select --hand 1");
        inject("summon");
        inject("show board");
        inject("card show --selected");
        inject("next phase");
        Assertions.assertEquals(GameController.getInstance().getGame().getPhase(), Phase.BATTLE_PHASE);
        checkNoInvalidCommandsInBuffer();
    }

    public void initializeUser(String username, String password, String nickname) {
        inject(String.format("user create -u %s -p %s -n %s", username, password, nickname));
        inject(String.format("user login -u %s -p %s", username, password));
        inject("menu enter shop");
        for (int i = 1; i <= 3; i++) {
            inject("shop buy AxeRaider");
            inject("shop buy BattleOx");
            inject("shop buy Fireyarou");
            inject("shop buy HornImp");
            inject("shop buy SilverFang");
        }
        inject("menu exit");
        inject("menu enter deck");
        String deckName = username + "Deck";
        inject(String.format("deck create %s", deckName));
        for (int i = 1; i <= 3; i++) {
            inject(String.format("deck add-card --card AxeRaider --deck %s", deckName));
            inject(String.format("deck add-card --card BattleOx --deck %s", deckName));
            inject(String.format("deck add-card --card Fireyarou --deck %s", deckName));
            inject(String.format("deck add-card --card HornImp --deck %s", deckName));
            inject(String.format("deck add-card --card SilverFang --deck %s", deckName));
        }
        inject(String.format("deck set-active %s", deckName));
        checkCurrentMenu(DeckMenuController.class);
        checkNoInvalidCommandsInBuffer();
        inject("menu exit");
        inject("user logout");
        checkCurrentMenu(LoginMenuController.class);
        checkNoInvalidCommandsInBuffer();
    }
}
