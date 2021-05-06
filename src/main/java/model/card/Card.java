package model.card;

import java.util.ArrayList;

public abstract class Card {
    protected String name;
    protected String description;
    protected Integer price;

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
