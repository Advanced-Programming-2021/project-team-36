package model.card;

import lombok.Getter;
import lombok.Setter;
import model.enums.Icon;
import model.enums.MagicState;
import model.enums.Status;

public class Magic extends Card {
    @Getter
    protected Icon icon;
    protected Status status;
    @Getter @Setter
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

    public boolean hasEffectOnMonsterSummon(Monster monster){
        return false;
    }
    public Effect onMonsterSummon(Monster monster){
        return ()->{};
    }

    public MagicState getState() {
        return magicState;
    }
}
