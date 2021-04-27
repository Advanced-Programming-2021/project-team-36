package model.deck;

import model.deck.MainDeck;
import model.deck.SideDeck;

public class Deck {
    private String name;
    private MainDeck mainDeck;
    private SideDeck sideDeck;

    Deck(String name) {
        this.name = name;
        mainDeck = new MainDeck();
        sideDeck = new SideDeck();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainDeck getMainDeck() {
        return mainDeck;
    }

    public SideDeck getSideDeck() {
        return sideDeck;
    }
}
