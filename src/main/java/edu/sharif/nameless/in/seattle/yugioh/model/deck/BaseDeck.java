package edu.sharif.nameless.in.seattle.yugioh.model.deck;

import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;

import java.io.Serializable;
import java.util.*;

public class BaseDeck implements Cloneable, Serializable {
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

    public void removeCard(Card toBeRemoved) {
        Card tmp = null;
        for(Card card : cards){
            if(card.getName().equals(toBeRemoved.getName()))
                tmp = card;
        }
        cards.remove(tmp);
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

    public BaseDeck readyForBattle(Player player){
        for(Card card: cards){
            card.readyForBattle(player);
        }
        return this;
    }
}
