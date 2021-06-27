package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

public class RingOfDefense extends Spell {

    public RingOfDefense(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            Action action = getChain().pop();
            int myLifePoint = this.owner.getLifePoint();
            try {
                action.runEffect();
            } catch (RoundOverExceptionEvent ignored) {
            }
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully.", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
            this.owner.setLifePoint(myLifePoint - this.owner.getLifePoint());
        };
    }

    @Override
    public boolean canActivateEffect() {
        return !getChain().isEmpty();
    }
}