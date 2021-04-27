package model.card;

public abstract class Card {
    protected String name;
    protected String description;
    protected Integer level;
    protected Integer speed;

    public String getName() {
        return name;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getSpeed() {
        return speed;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() { // TODO
        return "Card{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", level=" + level +
                ", speed=" + speed +
                '}';
    }
}
