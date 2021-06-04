package edu.sharif.nameless.in.seattle.yugioh.model.card;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
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
    @Getter @Setter
    protected MagicState magicState;
    public Magic(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price);
        this.icon = icon;
        this.status = status;
        this.magicState = null;
    }

    abstract public Effect activateEffect();
    abstract public boolean canActivateEffect();

    @Override
    public boolean isFacedUp() {
        return magicState.equals(MagicState.OCCUPIED);
    }

    @Override
    public Magic clone() {
        Magic cloned = (Magic) super.clone();
        cloned.icon = icon;
        cloned.status = status;
        cloned.magicState = magicState;
        return cloned;
    }

    protected Stack<Action> getChain(){
        return GameController.getInstance().getGame().getChain();
    }

    public MagicState getState() {
        return magicState;
    }
}
