package model.deck;

import model.card.Card;
import model.card.Magic;
import model.card.Monster;

import java.util.*;

public class BaseDeck implements Cloneable {
    protected ArrayList<Card> cards;

    {
        cards = new ArrayList<>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

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

    public int getNumberOfCards() {
        return cards.size();
    }

    public int getCardFrequency(Card card) {
        int count = 0;
        for (Card c : cards)
            if (c.getName().equalsIgnoreCase(card.getName()))
                count++;
        return count;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Monsters:\n");
        Arrays.stream(cards.toArray()).filter(card -> card instanceof Monster).forEach(card -> stringBuilder.append(card.toString()).append("\n"));
        stringBuilder.append("Spell and Traps:\n");
        Arrays.stream(cards.toArray()).filter(card -> card instanceof Magic).forEach(card -> stringBuilder.append(card.toString()).append("\n"));
        return stringBuilder.toString();
    }

    @Override
    public BaseDeck clone() {
        BaseDeck baseDeck;
        try {
            baseDeck = (BaseDeck) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
        baseDeck.cards = new ArrayList<>();
        for(Card card : cards){
            baseDeck.cards.add(card.clone());
        }
        return baseDeck;
    }
}
