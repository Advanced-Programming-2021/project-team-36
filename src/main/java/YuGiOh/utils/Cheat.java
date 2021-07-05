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
import YuGiOh.model.enums.Constants;

import java.util.Random;

public class Cheat {
    private static final Random rnd = new Random();

    private static void buyRandomCardsForUser(User user, int count){
        user.increaseBalance(Constants.InfMoney.val);
        try {
            Card[] allCards = Utils.getAllCards();
            while (user.getCards().size() < count)
                user.buy(allCards[rnd.nextInt(allCards.length)]);
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
                int index = rnd.nextInt(user.getCards().size());
                Card card = user.getCards().get(index);
                if (deck.getCardFrequency(card) >= user.getCardFrequency(card))
                    continue;
                if (deck.getCardFrequency(card) >= 3)
                    continue;
                if (deck.getMainDeck().isFull())
                    continue;
                deck.getMainDeck().addCard(card);
            }
            while (deck.getSideDeck().getCards().size() < 7) {
                int index = rnd.nextInt(user.getCards().size());
                Card card = user.getCards().get(index);
                if (deck.getCardFrequency(card) >= user.getCardFrequency(card))
                    continue;
                if (deck.getCardFrequency(card) >= 3)
                    continue;
                if (deck.getSideDeck().isFull())
                    continue;
                deck.getSideDeck().addCard(card);
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
    }
}
