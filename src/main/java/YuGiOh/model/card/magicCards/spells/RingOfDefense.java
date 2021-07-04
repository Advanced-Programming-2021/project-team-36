package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.card.magicCards.traps.MagicCylinder;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;

public class RingOfDefense extends Spell {

    public RingOfDefense(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            Action action = getChain().pop();
            int myLifePoint = this.getOwner().getLifePoint();
            try {
                action.runEffect();
            } catch (RoundOverExceptionEvent ignored) {
            }
            GameController.getInstance().increaseLifePoint(this.getOwner(),myLifePoint - this.getOwner().getLifePoint());
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully.", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return !getChain().isEmpty() && getChain().peek().getEvent() instanceof MagicActivation &&
                ((MagicActivation) getChain().peek().getEvent()).getCard() instanceof MagicCylinder;
    }
}