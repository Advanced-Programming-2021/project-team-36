package controller.menu;

import utils.CustomPrinter;
import utils.RoutingException;
import controller.LogicException;
import controller.ProgramController;
import lombok.Getter;
import model.User;
import model.card.Card;
import model.deck.Deck;
import view.*;

import java.util.Arrays;


public class DeckMenuController extends BaseMenuController {
    @Getter
    public static DeckMenuController instance;
    private final User user;

    public DeckMenuController(User user){
        this.view = new DeckMenuView();
        this.user = user;
        instance = this;
    }

    public void createDeck(String deckName) throws LogicException {
        if (user.getDeckByName(deckName) != null)
            throw new LogicException(String.format("deck with name %s already exists", deckName));
        user.addDeck(new Deck(deckName));
        CustomPrinter.println("deck created successfully!");
    }

    public void deleteDeck(Deck deck) {
        user.deleteDeck(deck);
        CustomPrinter.println("deck deleted successfully");
    }

    public void setActiveDeck(Deck deck) {
        user.setActiveDeck(deck);
        CustomPrinter.println("deck activated successfully");
    }

    public void addCardToDeck(Card card, Deck deck, boolean side) throws LogicException{
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
        CustomPrinter.println("card added to deck successfully");
    }

    public void removeCardFromDeck(Card card, Deck deck, boolean side) throws LogicException {
        if (!side) {
            if (deck.getMainDeck().getCardFrequency(card) == 0)
                throw new LogicException(String.format("card with name %s does not exists in main deck", card.getName()));
            deck.getMainDeck().removeCard(card);
        } else {
            if (deck.getSideDeck().getCardFrequency(card) == 0)
                throw new LogicException(String.format("card with name %s does not exists in side deck", card.getName()));
            deck.getSideDeck().removeCard(card);
        }
        CustomPrinter.println("card removed from deck successfully");
    }

    public void showDeck(Deck deck, boolean side) {
        CustomPrinter.println(deck.info(side));
    }

    public void showAllDecks() {
        CustomPrinter.println("Decks:");
        CustomPrinter.println("Active deck:");
        Deck activeDeck = user.getActiveDeck();
        if (activeDeck != null)
            CustomPrinter.println(activeDeck);
        CustomPrinter.println("Other decks:");
        Arrays.stream(user.getDecks().toArray()).sorted().filter(e -> e != activeDeck).forEach(CustomPrinter::println);
    }

    public void showAllCards() {
        Arrays.stream(user.getCards().toArray()).sorted().forEach(CustomPrinter::println);
    }

    public Deck deckParser(String deckName) throws ParserException {
        Deck deck = user.getDeckByName(deckName);
        if (deck == null)
            throw new ParserException(String.format("deck with name %s does not exists", deckName));
        return deck;
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(this.getClass()))
            throw new RoutingException("can't navigate to your current menu!");
        if (menu.equals(LoginMenuController.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenuController.class))
            return MainMenuController.getInstance();
        throw new RoutingException("menu navigation is not possible");
    }

}
