import YuGiOh.controller.GameController;
import YuGiOh.controller.menu.MainMenuController;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.controller.menu.MainMenuController;
import YuGiOh.controller.menu.ScoreboardMenuController;
import initialize.Sample;
import YuGiOh.model.enums.Phase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class GameTest extends Sample {
    @Test
    public void test() {
        run(String.format("debug --automatic_save on"));
        initializeUser("_abolfazl", "1234", "_atreus");
        initializeUser("_shayan", "1234", "_shayan.p");
        startNewDuel("_abolfazl", "1234", "_shayan");
    }

    void startNewDuel(String username, String password, String opponentUsername) {
        run(String.format("user login -u %s -p %s", username, password));
        runUntilNoInput();
        checkCurrentMenu(MainMenuController.class);
        run(String.format("duel --new --second_player %s --round 1", opponentUsername));
        run("select --hand 1");
        clearBuffer();
        run("summon");
//        run("show board");
//        run("next phase");
//        Assertions.assertEquals(GameController.getInstance().getGame().getPhase(), Phase.MAIN_PHASE1);
//        run("show hand");
//        run("select --hand 1");
//        run("summon");
//        checkNoInvalidCommandsInBuffer();
//        run("show board");
//        run("card show --selected");
//        run("show graveyard");
//        run("next phase");
//        Assertions.assertEquals(GameController.getInstance().getGame().getPhase(), Phase.BATTLE_PHASE);
//        checkNoInvalidCommandsInBuffer();
//        run("surrender");
//        run("menu exit");
    }

    @Test
    public void testCheatModes() {
        initializeUser("abolfazl", "1234", "atreus");
        initializeUser("shayan", "1234", "shayan.p");
        run(String.format("user login -u %s -p %s", "abolfazl", "1234"));
        runUntilNoInput();
        run("debug --mode on");
        run("debug --mode off");
        run("debug --mode off");
        run("debug --capture on");
        run("adjiadjdajh");
        run("debug --capture off");
        run("debug --capture off");
        run("debug --automatic_save on");
        run("debug --automatic_save off");
        run("debug --automatic_save off");
        run("debug import test --file ajhdajh --count 18309");
        checkCurrentMenu(MainMenuController.class);
        run("cheat increase --balance 1391931");
        run("menu enter deck");
        checkCurrentMenu(DeckMenuController.class);
        run("deck create salam");
        run("deck show --deck salam");
        run("deck show --deck salam --side 0");
        run("deck delete salam");
        run("deck show --all");
        run("deck show --cards");
        run("menu enter deck");
        run("menu enter havij");
        run("menu exit");
        run(String.format("duel --new --second_player %s --round 1", "shayan"));
        run("select --hand 1");
        run("card show --selected");
        run("summon");
        run("show board");
        run("menu enter ajadj");
        run("cheat increase -lp 138ahda");
        run("cheat increase -lp 9");
        run("cheat ultimate cheat");
        checkCurrentMenu(MainMenuController.class);
        run("menu enter scoreboard");
        checkCurrentMenu(ScoreboardMenuController.class);
        run("scoreboard show");
    }

}
