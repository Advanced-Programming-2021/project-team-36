import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.Game;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.Player;
import org.junit.Test;
import utils.IntegrationTestBase;

public class DoubleAITest extends IntegrationTestBase {
    @Test
    public void battle() throws ModelException {
        Player p1 = new AIPlayer();
        Player p2 = new AIPlayer();
        DuelMenuController duelMenuController = new DuelMenuController(new Game(p1, p2, 1));
        duelMenuController.control();
    }
}
