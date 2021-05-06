package model.card;

import model.enums.Icon;
import model.enums.MagicState;
import model.enums.Status;

public class Magic extends Card {
    protected Icon icon;
    protected Status status;
    protected MagicState magicState;
    protected Magic(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price);
        this.icon = icon;
        this.status = status;
        this.magicState = null;
    }

    @Override
    public Magic clone() {
        Magic cloned = (Magic) super.clone();
        cloned.icon = icon;
        cloned.status = status;
        cloned.magicState = magicState;
        return cloned;
    }

    public MagicState getState() {
        return magicState;
    }
}
