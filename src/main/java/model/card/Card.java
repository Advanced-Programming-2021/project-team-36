package model.card;

import java.util.ArrayList;

public abstract class Card {
    protected final String name;
    protected final String description;
    protected final int price;

    protected Card(String name, String description, int price){
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

    public static ArrayList<Card> getAllCardsLexicographically() {
        // TODO
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return name + ":" + description;
    }
}
