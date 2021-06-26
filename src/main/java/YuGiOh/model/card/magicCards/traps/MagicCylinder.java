package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.card.action.Event;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.AttackEvent;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Trap;

public class MagicCylinder extends Trap {

    public MagicCylinder(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return ()->{
            if(!canActivateEffect())
                throw new LogicException("You can't activate this effect");
            AttackEvent event = (AttackEvent) getChain().pop().getEvent();
            GameController.getInstance().decreaseLifePoint(
                    GameController.getInstance().getGame().getOtherPlayer(this.owner),
                    event.getAttacker().getAttackDamage()
            );
            event.getAttacker().setAllowAttack(false);
            GameController.getInstance().getPlayerControllerByPlayer(this.owner).moveCardToGraveYard(this);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if(getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if(action.getEvent() instanceof AttackEvent){
            AttackEvent event = (AttackEvent) action.getEvent();
            return event.getAttacker().owner.equals(GameController.getInstance().getGame().getOtherPlayer(this.owner));
        }
        return false;
    }
}
