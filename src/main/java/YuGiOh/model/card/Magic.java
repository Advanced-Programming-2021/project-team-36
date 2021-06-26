package YuGiOh.model.card;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MagicState;
import YuGiOh.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

abstract public class Magic extends Card {
    @Getter
    protected Icon icon;
    protected Status status;
    @Getter @Setter
    protected Monster equippedMonster;
    @Setter
    protected MagicState magicState;

    public Magic(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price);
        this.icon = icon;
        this.status = status;
        this.magicState = null;
    }

    public final Effect activateEffect() {
        return () -> {
            getEffect().run();
            if (icon.equals(Icon.QUICKPLAY) || icon.equals(Icon.COUNTER) || icon.equals(Icon.NORMAL)) {
                moveCardToGraveYard();
            }
        };
    }


    abstract protected Effect getEffect();

    abstract public boolean canActivateEffect();

    @Override
    public boolean hasEffect() {
        return true;
    }

    @Override
    public boolean isFacedUp(){
        return magicState.equals(MagicState.OCCUPIED);
    }

    @Override
    public Magic clone() {
        Magic cloned = (Magic) super.clone();
        cloned.icon = icon;
        cloned.status = status;
        cloned.magicState = null;
        return cloned;
    }

    public int affectionOnAttackingMonster(Monster monster) {
        return 0;
    }

    public int affectionOnDefensiveMonster(Monster monster) {
        return 0;
    }

    protected Stack<Action> getChain() {
        return GameController.getInstance().getGame().getChain();
    }

    public boolean isActivated() {
        return GameController.getInstance().getGame().getAllCardsOnBoard().contains(this) && isFacedUp();
    }

    public MagicState getState() {
        return magicState;
    }

    public void onMovingToGraveYard() {
    }

    public void onDestroyMyMonster() {
    }

    public boolean letMagicActivate(Magic magic){
        return true;
    }
}
