import controller.GameController;
import controller.menu.MainMenuController;
import initialize.Sample;
import model.enums.Phase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import initialize.Sample.*;

public class GameTest extends Sample {
    @Test
    public void test() {
        run(String.format("debug --automatic_save on"));
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
        run("menu exit");
        run("menu exit");
        run("menu exit");
    }

}
