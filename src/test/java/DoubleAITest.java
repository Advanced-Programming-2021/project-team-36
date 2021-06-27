import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.Duel;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.Player;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import org.junit.Test;
import utils.IntegrationTestBase;

import java.util.Random;

public class DoubleAITest extends IntegrationTestBase {
    Random random = new Random(183981);

    @Test
    public void multipleTest() throws ModelException {
        int numberOfTests = 1;
        for (int i = 0; i < numberOfTests; i ++) {
            battle();
        }
    }

    public void battle() throws ModelException {
        Player players[] = new Player[2];
        players[0] = new AIPlayer();
        players[1] = new AIPlayer();

        Card allCards[] = YuGiOh.model.card.Utils.getAllCards();
        int numberOfCards = 20;
        for (int i = 0; i < 2; i ++) {
            User user = players[i].getUser();
            user.deleteDeck(user.getDecks().get(0));
            user.addDeck(new Deck("someDeck"));
            for (int j = 0; j < numberOfCards; j ++)
                user.addCard(allCards[random.nextInt(allCards.length)]);
        }
        DuelMenuController duelMenuController = new DuelMenuController(new Duel(players[0], players[1], 1));
        duelMenuController.control();
    }
}
