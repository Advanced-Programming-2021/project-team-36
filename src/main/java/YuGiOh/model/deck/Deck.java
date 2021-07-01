package YuGiOh.model.deck;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;

import java.io.Serializable;

public class Deck implements Comparable<Deck>, Cloneable, Serializable {
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

    @Override
    public String toString() {
        return String.format("%s: main deck %d, side deck %d, %s",
                name, mainDeck.cards.size(), sideDeck.cards.size(),
                (mainDeck.isValid() && sideDeck.isValid()) ? "valid" : "invalid");
    }

    @Override
    public Deck clone(){
        Deck deck;
        try {
            deck = (Deck) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
        deck.name = new String(name);
        deck.mainDeck = mainDeck.clone();
        deck.sideDeck = sideDeck.clone();
        return deck;
    }

    public Deck readyForBattle(Player player){
        mainDeck.readyForBattle(player);
        sideDeck.readyForBattle(player);
        return this;
    }
}
