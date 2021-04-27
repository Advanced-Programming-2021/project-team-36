package model.deck;

import model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BaseDeck {
    protected ArrayList<Card> cards;

    public Card getTopCard() {
        if (!cards.isEmpty())
            return cards.get(cards.size() - 1);
        return null;
    }

    public void shuffleCards() {
        Collections.shuffle(cards, new Random(System.nanoTime()));
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.removeIf(card1 -> card1.equals(card));
    }

    @Override
    public String toString() { // TODO
        return "BaseDeck{" +
                "cards=" + cards +
                '}';
    }
}
