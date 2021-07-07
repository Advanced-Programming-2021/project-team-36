package YuGiOh.model.card;

import YuGiOh.controller.LogicException;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.action.Effect;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public abstract class Card implements Comparable<Card>, Cloneable, Serializable {
    @Getter @Setter
    protected String name;
    @Getter @Setter
    protected String description;
    @Getter @Setter
    protected int price;

    private SimpleObjectProperty<Player> ownerProperty;

    {
        ownerProperty = new SimpleObjectProperty<>(null);
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

    public Card readyForBattle(Player owner) {
        setOwner(owner);
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
            cloned.ownerProperty = new SimpleObjectProperty<>(null);
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

    public void preprocessForEffect() {
    }

    abstract public void onMovingToGraveyard();

    public void setOwner(Player player) {
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