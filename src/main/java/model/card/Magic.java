package model.card;

import model.enums.MagicState;

public class Magic extends Card {
    protected MagicState magicState = null;
    protected Magic(String name, String description, int price) {
        super(name, description, price);
    }

    public MagicState getMagicState() {
        return magicState;
    }
}
