package model.Player;

import controller.LogicException;
import controller.menu.DeckMenuController;
import controller.menu.ShopMenuController;
import model.card.Card;
import model.card.Utils;
import utils.CustomPrinter;
import model.ModelException;
import model.User;
import model.card.monsterCards.*;
import model.deck.Deck;
import view.Parser;
import view.ParserException;

import java.util.Random;
import java.util.RandomAccess;

public class AIPlayer extends Player {
    public AIPlayer() throws ModelException {
        super(getAIUser());
    }
    private static User getAIUser() {
        User user = User.getUserByUsername("artificial_intelligence");
        if (user != null)
            return user;
        Random rnd = new Random();
        int aiId = rnd.nextInt(9) + 1;
        user = new User("artificial_intelligence" + aiId, "Mr.AI" + aiId, "thisIsAStrongPassword");
        try {
            ShopMenuController shopController = new ShopMenuController(user);
            Card[] allCards = Utils.getAllCards();
            while (user.getCards().size() < 200) {
                shopController.buyCard(allCards[rnd.nextInt(allCards.length)]);
            }
        } catch (ModelException ignored) {
        }

        for (int i = 0; i < 8; i++) {
            String deckName = "Mr.AIDeck" + i;
            Deck deck = new Deck(deckName);
            user.addDeck(deck);
            DeckMenuController deckController = new DeckMenuController(user);
            while (deck.getMainDeck().getCards().size() < 40) {
                try {
                    int index = rnd.nextInt(user.getCards().size());
                    deckController.addCardToDeck(user.getCards().get(index), deck, false);
                } catch (LogicException ignored) {
                }
            }
            while (deck.getSideDeck().getCards().size() < 7) {
                try {
                    int index = rnd.nextInt(user.getCards().size());
                    deckController.addCardToDeck(user.getCards().get(index), deck, true);
                } catch (LogicException ignored) {
                }
            }
        }

        int index = rnd.nextInt(user.getDecks().size());
        user.setActiveDeck(user.getDecks().get(index));

        return user;
    }
}
