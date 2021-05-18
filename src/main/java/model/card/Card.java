package model.card;

import lombok.Getter;
import lombok.Setter;
import model.Player.Player;

import java.io.Serializable;

public abstract class Card implements Comparable<Card>, Cloneable, Serializable {
    protected String name;
    protected String description;
    protected int price;
    public Player owner;

    @Getter
    boolean isInBattle;

    {
        owner = null;
        isInBattle = false;
    }

    protected Card(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    abstract public int getSpeed();

    abstract public boolean isFacedUp();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public Card readyForBattle(Player owner) {
        isInBattle = true;
        this.owner = owner;
        return this;
    }

    @Override
    public Card clone() {
        try {
            Card cloned = (Card) super.clone();
            cloned.name = name;
            cloned.description = description;
            cloned.price = price;
            cloned.isInBattle = false;
            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        // TODO
    }

    @Override
    public String toString() {
        return name + ":" + description;
    }

    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}