import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.Duel;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.Player;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import YuGiOh.model.enums.AIMode;
import initialize.Sample;
import org.junit.Test;

import java.util.Random;

public class ScoreBoardTest extends Sample {
    @Test
    public void showScoreBoard() {
        initializeUser("shayan", "1234", "Shayan.P");
        run("user login -u shayan -p 1234");
        run("menu enter ScoreBoard");
        run("scoreboard show");
    }

    public void battle() throws ModelException {
        Random random = new Random();
        Player players[] = new Player[2];
        players[0] = new AIPlayer(AIMode.NORMAL);
        players[1] = new AIPlayer(AIMode.AGGRESSIVE);

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
