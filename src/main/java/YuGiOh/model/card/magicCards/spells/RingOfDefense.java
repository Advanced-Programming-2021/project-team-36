package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.card.magicCards.traps.MagicCylinder;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class RingOfDefense extends Spell {

    public RingOfDefense(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            Action action = getChain().pop();
            int myLifePoint = this.getOwner().getLifePoint();
            action.runEffect();
            GameController.getInstance().increaseLifePoint(this.getOwner(),myLifePoint - this.getOwner().getLifePoint());
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully.", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if(getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if(!(action.getEvent() instanceof MagicActivation))
            return false;
        MagicActivation event = (MagicActivation) action.getEvent();
        return event.getMagic() instanceof MagicCylinder;
    }
}