import controller.GameController;
import org.junit.Test;
import utils.IntegrationTestBase;

public class CardNullTest extends IntegrationTestBase {
    @Test
    public void test(){
        run("user login -u abolfazl -p 1234");
        run("duel --new --second_player shayan --round 1");

        System.out.println(GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard());
//        run("show hand");
//        System.out.println(GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getCardsOnHand().size());
    }
}
