package controller;

import model.User;
import model.card.Card;
import model.deck.Deck;
import view.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeckMenuController {
    public static void createDeck(Context context, String deckName) throws LogicException {
        User user = context.getUser();
        if (user.getDeckByName(deckName) != null)
            throw new LogicException(String.format("deck with name %s already exists", deckName));
        user.addDeck(new Deck(deckName));
        System.out.println("deck created successfully!");
    }

    public static void deleteDeck(Context context, Deck deck) throws LogicException {
        User user = context.getUser();
        user.deleteDeck(deck);
        System.out.println("deck deleted successfully");
    }

    public static void setActiveDeck(Context context, Deck deck) throws LogicException {
        User user = context.getUser();
        user.setActiveDeck(deck);
        System.out.println("deck activated successfully");
    }

    public static void addCardToDeck(Context context, Card card, Deck deck, boolean side) throws LogicException {
        User user = context.getUser();
        if (!side) {
            // TODO : Should check whether the mainDeck is full.
            // Response in case : "main deck is full"
            deck.getMainDeck().addCard(card);
        } else {
            // TODO : Should check whether the sideDeck is full.
            // Response in case : "side deck is full"
            deck.getSideDeck().addCard(card);
        }
        System.out.println("card added to deck successfully");
    }

    public static void removeCardFromDeck(Context context, Card card, Deck deck, boolean side) throws LogicException {
        User user = context.getUser();
        if (!side) {
            // TODO : Should check whether the card exists in the mainDeck.
            // Response in case : "card with name " + cardName + " does not exists in main deck"
            deck.getMainDeck().removeCard(card);
        } else {
            // TODO : Should check whether the card exists in the sideDeck.
            // Response in case : "card with name " + cardName + " does not exists in side deck"
            deck.getSideDeck().removeCard(card);
        }
        System.out.println("card removed from deck successfully"); // Doc mistakenly says "form"
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
