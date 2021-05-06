package model.deck;

import model.card.Card;
import model.deck.MainDeck;
import model.deck.SideDeck;

import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Deck implements Comparable<Deck> {
    private String name;
    private MainDeck mainDeck;
    private SideDeck sideDeck;

    public Deck(String name) {
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

    public int getCardFrequency(Card card) {
        return getMainDeck().getCardFrequency(card) + getSideDeck().getCardFrequency(card);
    }

    public String info(boolean side) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Deck: ").append(name).append("\n");
        if (side){
            stringBuilder.append("Side deck:").append("\n");
            stringBuilder.append(sideDeck.toString());
        }
        else{
            stringBuilder.append("Main deck:").append("\n");
            stringBuilder.append(mainDeck.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Deck other) {
        return this.getName().compareTo(other.getName());
    }
}
