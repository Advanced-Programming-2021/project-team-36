package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.event.AttackEvent;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Trap;

import java.util.concurrent.CompletableFuture;

public class MagicCylinder extends Trap {

    public MagicCylinder(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            if (!canActivateEffect())
                throw new Error("This must never happen if we validate before run.");
            AttackEvent event = (AttackEvent) getChain().pop().getEvent();
            Monster attacker = event.getAttacker();
            GameController.getInstance().decreaseLifePoint(
                    GameController.getInstance().getGame().getOtherPlayer(this.getOwner()),
                    attacker.getAttackDamage(),
                    true
            );
            attacker.setAllowAttack(false);
            GameController.getInstance().moveCardToGraveYard(this);
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if (action.getEvent() instanceof AttackEvent) {
            AttackEvent event = (AttackEvent) action.getEvent();
            return event.getAttacker().getOwner().equals(GameController.getInstance().getGame().getOtherPlayer(this.getOwner()));
        }
        return false;
    }
}
