package edu.sharif.nameless.in.seattle.yugioh.model.card.magicCards.traps;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.AttackEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Trap;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Status;

public class MagicCylinder extends Trap {

    public MagicCylinder(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        return ()->{
            GameController.getInstance().decreaseLifePoint(
                    GameController.getInstance().getGame().getOtherPlayer(this.owner),
                    ((AttackEvent) getChain().peek().getEvent()).getAttacker().getAttackDamage()
            );
            getChain().pop();
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
