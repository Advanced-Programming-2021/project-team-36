import controller.menu.DuelMenuController;
import model.Game;
import model.ModelException;
import model.Player.AIPlayer;
import model.Player.Player;
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
