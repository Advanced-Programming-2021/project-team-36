package YuGiOh.model.card;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MagicState;
import YuGiOh.model.enums.Status;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

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

    public Effect activateEffect(){
        return ()->{
            getEffect().run();
            moveCardToGraveYard();
        };
    }

    abstract protected Effect getEffect();
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
