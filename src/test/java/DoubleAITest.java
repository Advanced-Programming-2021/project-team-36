import edu.sharif.nameless.in.seattle.yugioh.controller.menu.DuelMenuController;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.AIPlayer;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import org.junit.Test;
import utils.IntegrationTestBase;

public class  DoubleAITest extends IntegrationTestBase {
    @Test
    public void battle() throws ModelException {
        Player p1 = new AIPlayer();
        Player p2 = new AIPlayer();
        DuelMenuController duelMenuController = new DuelMenuController(new Game(p1, p2, 1));
        duelMenuController.control();
    }
}
