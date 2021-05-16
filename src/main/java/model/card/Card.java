package model.card;

import model.Player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import utils.ClassFinder;

public abstract class Card implements Comparable<Card>, Cloneable{
    protected String name;
    protected String description;
    protected int price;
    public Player owner;
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public Card readyForBattle(Player owner){
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
            return cloned;
        } catch (CloneNotSupportedException e){
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