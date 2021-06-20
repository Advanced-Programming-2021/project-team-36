package YuGiOh.utils;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.controller.menu.ShopMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.model.deck.Deck;

import java.util.Random;

public class Cheat {
    public static void buildSuperUser(User user){
        Random rnd = new Random();
        try {
            ShopMenuController shopController = new ShopMenuController(user);
            Card[] allCards = Utils.getAllCards();
            while (user.getCards().size() < 100) {
                shopController.buyCard(allCards[rnd.nextInt(allCards.length)]);
            }
            for (Card card : allCards)
                if (card.getName().equalsIgnoreCase("AdvancedRitualArt") || card.getName().equalsIgnoreCase("SkullGuardian")) {
                    shopController.buyCard(card);
                    shopController.buyCard(card);
                    shopController.buyCard(card);
                }
        } catch (ModelException ignored) {
        }
        for (int i = 0; i < 1; i++) {
            String deckName = user.getNickname() + i;
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

        CustomPrinter.getLastBuffer();
        // this will clear the buffer
    }
}
