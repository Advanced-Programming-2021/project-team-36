package controller;

import model.User;
import model.card.Card;
import model.deck.Deck;
import view.Context;

import java.util.Arrays;

public class DeckMenuController {
    public static void createDeck(Context context, String deckName) throws LogicException {
        User user = context.getUser();
        if (user.getDeckByName(deckName) != null)
            throw new LogicException(String.format("deck with name %s already exists", deckName));
        user.addDeck(new Deck(deckName));
        System.out.println("deck created successfully!");
    }

    public static void deleteDeck(Context context, Deck deck) {
        User user = context.getUser();
        user.deleteDeck(deck);
        System.out.println("deck deleted successfully");
    }

    public static void setActiveDeck(Context context, Deck deck) {
        User user = context.getUser();
        user.setActiveDeck(deck);
        System.out.println("deck activated successfully");
    }

    public static void addCardToDeck(Context context, Card card, Deck deck, boolean side) throws LogicException{
        User user = context.getUser();
        if (deck.getCardFrequency(card) >= user.getCardFrequency(card))
            throw new LogicException(String.format("you do not have enough %s card", deck.getName()));
        if (deck.getCardFrequency(card) == 3)
            throw new LogicException(String.format("there are already three cards with name %s in deck %s", card.getName(), deck.getName()));
        if (!side) {
            if (deck.getMainDeck().isFull())
                throw new LogicException("main deck is full");
            deck.getMainDeck().addCard(card);
        } else {
            if (deck.getSideDeck().isFull())
                throw new LogicException("side deck is full");
            deck.getSideDeck().addCard(card);
        }
        System.out.println("card added to deck successfully");
    }

    public static void removeCardFromDeck(Context context, Card card, Deck deck, boolean side) throws LogicException {
        User user = context.getUser();
        if (!side) {
            if (deck.getMainDeck().getCardFrequency(card) == 0)
                throw new LogicException(String.format("card with name %s does not exists in main deck", card.getName()));
            deck.getMainDeck().removeCard(card);
        } else {
            if (deck.getSideDeck().getCardFrequency(card) == 0)
                throw new LogicException(String.format("card with name %s does not exists in side deck", card.getName()));
            deck.getSideDeck().removeCard(card);
        }
        System.out.println("card removed from deck successfully");
    }

    public static void showDeck(Context context, Deck deck, boolean side) {
        User user = context.getUser();
        System.out.println(deck.info(side));
    }

    public static void showAllDecks(Context context) {
        System.out.println("Decks:");
        System.out.println("Active deck:");
        User user = context.getUser();
        Deck activeDeck = user.getActiveDeck();
        if (activeDeck != null)
            System.out.println(activeDeck);
        System.out.println("Other decks:");
        Arrays.stream(user.getDecks().toArray()).sorted().filter(e -> e != activeDeck).forEach(System.out::println);
    }

    public static void showAllCards(Context context) {
        User user = context.getUser();
        Arrays.stream(user.getCards().toArray()).sorted().forEach(System.out::println);
    }
}
