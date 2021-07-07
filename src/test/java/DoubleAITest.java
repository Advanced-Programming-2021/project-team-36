import YuGiOh.controller.ProgramController;
import YuGiOh.controller.menus.DuelMenuController;
import YuGiOh.archive.menu.MainMenuController;
import YuGiOh.model.Duel;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.Player;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import YuGiOh.model.enums.AIMode;
import org.junit.Test;
import utils.IntegrationTestBase;

import java.util.Random;

public class DoubleAITest extends IntegrationTestBase {
    Random random = new Random(183981);

    @Test
    public void multipleTest() throws ModelException {
        new ProgramController();
        new MainMenuController(new User("salam", "this", "test"));
        int numberOfTests = 10;
        for (int i = 0; i < numberOfTests; i ++) {
            battle();
        }
    }

    public void battle() throws ModelException {
        Player players[] = new Player[2];
        players[0] = new AIPlayer(random.nextInt(2) == 0 ? AIMode.NORMAL : AIMode.AGGRESSIVE);
        players[1] = new AIPlayer(random.nextInt(2) == 0 ? AIMode.NORMAL : AIMode.AGGRESSIVE);

        Card allCards[] = YuGiOh.model.card.Utils.getAllCards();
        int numberOfCards = 20;
        for (int i = 0; i < 2; i ++) {
            User user = players[i].getUser();
            user.deleteDeck(user.getDecks().get(0));
            user.addDeck(new Deck("someDeck"));
            for (int j = 0; j < numberOfCards; j ++)
                user.addCard(allCards[random.nextInt(allCards.length)]);
        }
        DuelMenuController duelMenuController = new DuelMenuController(new Duel(players[0], players[1], 3));
        duelMenuController.control();
    }
}
