package edu.sharif.nameless.in.seattle.yugioh.model.card;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MagicState;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Status;

import java.util.Stack;

abstract public class Magic extends Card {
    @Getter
    protected Icon icon;
    protected Status status;
    protected SimpleObjectProperty<MagicState> magicStateProperty;
    public Magic(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price);
        this.icon = icon;
        this.status = status;
        this.magicStateProperty = new SimpleObjectProperty<>(null);
    }

    abstract public Effect activateEffect();
    abstract public boolean canActivateEffect();

    @Override
    public BooleanBinding facedUpProperty() {
        return Bindings.when(magicStateProperty.isEqualTo(MagicState.OCCUPIED)).then(true).otherwise(false);
    }

    @Override
    public Magic clone() {
        Magic cloned = (Magic) super.clone();
        cloned.icon = icon;
        cloned.status = status;
        cloned.magicStateProperty = new SimpleObjectProperty<>(null);
        return cloned;
    }

    public MagicState getMagicState() {
        return magicStateProperty.get();
    }

    public void setMagicState(MagicState magicState) {
        this.magicStateProperty.set(magicState);
    }

    protected Stack<Action> getChain(){
        return GameController.getInstance().getGame().getChain();
    }

    public MagicState getState() {
        return magicStateProperty.getValue();
    }
}
