import YuGiOh.controller.GameController;
import initialize.Sample;
import YuGiOh.model.enums.Phase;
import org.junit.Test;

public class RitualSummonTest extends Sample {
    @Test
    public void test(){
        initializeUser("shayan", "1234", "shayan.p");
        String username = "abolfazl";
        String password = "1234pass";
        String nickname = "atreus";
        run(String.format("user create -u %s -p %s -n %s", username, password, nickname));
        run(String.format("user login -u %s -p %s", username, password));
        run("menu enter shop");
        run("shop buy CommandKnight");
        run("shop buy CommandKnight");
        run("shop buy SkullGuardian"); // Ritual Monster
        run("shop buy SkullGuardian"); // Ritual Monster
        run("shop buy AdvancedRitualArt"); // Ritual Spell
        run("shop buy AdvancedRitualArt"); // Ritual Spell
        run("menu exit");
        run("menu enter deck");
        run("deck create deck1");
        run("deck add-card --deck deck1 --card CommandKnight");
        run("deck add-card --deck deck1 --card CommandKnight");
        run("deck add-card --deck deck1 --card SkullGuardian");
        run("deck add-card --deck deck1 --card SkullGuardian");
        run("deck add-card --deck deck1 --card AdvancedRitualArt");
        run("deck add-card --deck deck1 --card AdvancedRitualArt");
        run("deck set-active deck1");
        run("menu exit");
        run("duel --new --ai --round 1");
        if (GameController.getInstance().getGame().getPhase().equals(Phase.STANDBY_PHASE))
            run("next phase");
        for (int i = 1; i <= 5; i++)
            if (GameController.getInstance().getGame().getCurrentPlayer().getBoard().getCardsOnHand().get(i - 1).getName().equalsIgnoreCase("AdvancedRitualArt"))
                run(String.format("select --hand %d", i));
        run("activate effect");
        run("help");
//        for (int i = 1; i <= 5; i++)
//            if (GameController.getInstance().getGame().getCurrentPlayer().getBoard().getCardsOnHand().get(i - 1).getName().equalsIgnoreCase("SkullGuardian")) {
//                run(String.format("select --hand %d", i));
//                break;
//            }
//        run("help");
        checkNoInvalidCommandsInBuffer();
    }
}
