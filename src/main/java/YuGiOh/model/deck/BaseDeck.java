package YuGiOh.model.deck;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.*;

public class BaseDeck implements Cloneable, Serializable {
    protected ObservableList<Card> cards;

    {
        cards = FXCollections.observableArrayList();
    }

    public ObservableList<Card> getCards() {
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

    public void removeCard(Card toBeRemoved) {
        Card tmp = null;
        for (Card card : cards) {
            if (card.getName().equals(toBeRemoved.getName()))
                tmp = card;
        }
        cards.remove(tmp);
    }

    public boolean hasCard(Card card) {
        for (Card card1 : cards)
            if (card1.getName().equals(card.getName()))
                return true;
        return false;
    }

    public int getNumberOfCards() {
        return cards.size();
    }

    public int getCardFrequency(Card card) {
        int count = 0;
        for (Card c : cards)
            if (c.getName().equals(card.getName()))
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
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        baseDeck.cards = FXCollections.observableArrayList();
        for (Card card : cards) {
            baseDeck.cards.add(card.clone());
        }
        return baseDeck;
    }

    public BaseDeck readyForBattle(Player player) {
        for (Card card : cards) {
            card.readyForBattle(player);
        }
        return this;
    }
}
