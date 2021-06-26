package YuGiOh.utils;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.controller.menu.ShopMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.model.deck.Deck;

import java.util.Random;

public class Cheat {
    private static final Random rnd = new Random();

    private static void buyRandomCardsForUser(User user, int count){
        try {
            ShopMenuController shopController = new ShopMenuController(user);
            Card[] allCards = Utils.getAllCards();
            while (user.getCards().size() < count) {
                shopController.buyCard(allCards[rnd.nextInt(allCards.length)]);
            }
        } catch (ModelException ignored) {
        }
    }

    private static void buildRandomDecksForUser(User user){
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
    }

    public static void buildSuperUserWithManyOfThisCards(User user, int countOfInjection, String... cardNames){
        buildSuperUser(user);

        user.getActiveDeck().getMainDeck().getCards().removeAll(user.getActiveDeck().getMainDeck().getCards().subList(0, countOfInjection));
        for(String cardName : cardNames){
            for(int i = 0; i < countOfInjection/cardNames.length; i++) {
                user.addCard(Utils.getCard(cardName));
                user.getActiveDeck().getMainDeck().addCard(Utils.getCard(cardName));
            }
        }
    }

    public static void buildSuperUser(User user){
        buyRandomCardsForUser(user, 100);
        buildRandomDecksForUser(user);
        CustomPrinter.getLastBuffer();
    }
}
