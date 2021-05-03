package model.card;

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

    @Override
    public String toString() {
        return name + ":" + description;
    }
}
