package YuGiOh.model.card;

import YuGiOh.controller.LogicException;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.action.Effect;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.io.Serializable;

public abstract class Card implements Comparable<Card>, Cloneable, Serializable {
    protected String name;
    protected String description;
    protected int price;
    private SimpleObjectProperty<Player> ownerProperty;

    @Getter
    boolean isInBattle;

    {
        ownerProperty = new SimpleObjectProperty<>(null);
        isInBattle = false;
    }

    public Card(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    abstract public int getSpeed();

    abstract public BooleanBinding facedUpProperty();

    public boolean isFacedUp(){
        return facedUpProperty().get();
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

    public Card readyForBattle(Player owner) {
        isInBattle = true;
        setOwner(owner);
        return this;
    }

    public Card outOfBattle() {
        isInBattle = false;
        return this;
    }

    public boolean isActivated() {
        return false;
    }

    abstract public Effect activateEffect() throws LogicException;
    abstract public boolean canActivateEffect();
    abstract public boolean hasEffect();

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

    public void moveCardToGraveYard(){
        getOwner().moveCardToGraveYard(this);
    }

    public void startOfNewTurn() {
    }

    protected void setOwner(Player player) {
        ownerProperty.set(player);
    }
    public Player getOwner() {
        return ownerProperty.get();
    }
    public SimpleObjectProperty<Player> ownerProperty() {
        return ownerProperty;
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