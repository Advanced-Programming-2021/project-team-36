package controller;

import model.Player;
import model.User;
import model.card.Card;
import model.deck.Deck;

public class DeckMenu extends BaseMenu {
    public static void createDeck(String deckName) {
        User user = User.getCurrentUser();
        if (user.getDeckByName(deckName) != null) {
            System.out.println("deck with name " + deckName + " already exists");
        }
        User.getCurrentUser().addDeck(new Deck(deckName));
        System.out.println("deck created successfully!");
    }
    public static void deleteDeck(String deckName) {
        User user = User.getCurrentUser();
        Deck deck = user.getDeckByName(deckName);
        if (deck == null) {
            System.out.println("deck with name " + deckName + " does not exists");
            return;
        }
        user.deleteDeck(deck);
        System.out.println("deck deleted successfully");
    }
    public static void setActiveDeck(String deckName) {
        User user = User.getCurrentUser();
        Deck deck = user.getDeckByName(deckName);
        if (deck == null) {
            System.out.println("deck with name " + deckName + " does not exists");
            return;
        }
        user.setActiveDeck(deck);
        System.out.println("deck activated successfully");
    }
    public static void addCardToDeck(String cardName, String deckName, boolean side) {
        Card card; // TODO : Card must be initialized to something. Should also check whether the card exists or not.
        // Response in case : "card with name " + cardName + " does not exist"
        User user = User.getCurrentUser();
        Deck deck = user.getDeckByName(deckName);
        if (deck == null) {
            System.out.println("deck with name " + deckName + " does not exists");
            return;
        }
        if (!side) {
            // TODO : Should check whether the mainDeck is full.
            // Response in case : "main deck is full"
            deck.getMainDeck().addCard(card);
        }
        else {
            // TODO : Should check whether the sideDeck is full.
            // Response in case : "side deck is full"
            deck.getSideDeck().addCard(card);
        }
        System.out.println("card added to deck successfully");
    }
    public static void removeCardFromDeck(String cardName, String deckName, boolean side) {
        Card card; // TODO : Card must be initialized to something. Should also check whether the card exists or not.
        // Response in case : "card with name " + cardName + " does not exist"
        User user = User.getCurrentUser();
        Deck deck = user.getDeckByName(deckName);
        if (deck == null) {
            System.out.println("deck with name " + deckName + " does not exists");
            return;
        }
        if (!side) {
            // TODO : Should check whether the card exists in the mainDeck.
            // Response in case : "card with name " + cardName + " does not exists in main deck"
            deck.getMainDeck().removeCard(card);
        }
        else {
            // TODO : Should check whether the card exists in the sideDeck.
            // Response in case : "card with name " + cardName + " does not exists in side deck"
            deck.getSideDeck().removeCard(card);
        }
        System.out.println("card removed from deck successfully"); // Doc mistakenly says "form"
    }
    public static void showDeck(String deckName, boolean side) {
        User user = User.getCurrentUser();
        Deck deck = user.getDeckByName(deckName);
        System.out.println(deck.info(side));
    }
    public static void showAllDecks() {
        System.out.println("Decks:");
        System.out.println("Active deck:");
        User user = User.getCurrentUser();
        Deck activeDeck = user.getActiveDeck();
        if (activeDeck != null)
            System.out.println(activeDeck);
        System.out.println("Other decks:");
        for (Deck deck : User.getCurrentUser().getDecksLexicographically())
            if (deck != activeDeck)
                System.out.println(deck);
    }
    public static void showAllCards() {
        User user = User.getCurrentUser();
        for (Card card : user.getCardsLexicographically())
            System.out.println(card);
    }
    protected static void showCurrentMenu() {

    }
    protected static void navigateToMenu(String menu) {

    }
    protected static void exit() {

    }
    public static void programControl() {

    }
}
