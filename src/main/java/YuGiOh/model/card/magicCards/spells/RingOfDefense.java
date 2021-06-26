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
            CustomPrinter.println("Ring of Defense won't let you inflict any damage to me. Ha ha ha", Color.Purple);
            int myLifePoint = this.owner.getLifePoint();
            try {
                action.runEffect();
            } catch (RoundOverExceptionEvent ignored) {
            }
            CustomPrinter.println("Ring of Defense heal life point now.", Color.Purple);
            this.owner.setLifePoint(myLifePoint - this.owner.getLifePoint());
        };
    }

    @Override
    public boolean canActivateEffect() {
        return !getChain().isEmpty();
    }
}