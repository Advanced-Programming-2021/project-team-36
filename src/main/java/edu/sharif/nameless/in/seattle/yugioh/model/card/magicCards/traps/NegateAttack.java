package edu.sharif.nameless.in.seattle.yugioh.model.card.magicCards.traps;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Trap;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Phase;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Status;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.AttackEvent;

public class NegateAttack extends Trap {
    public NegateAttack(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        return ()->{
            getChain().pop();
            // todo how to end battle phase when there is still action in chain?
            assert GameController.getInstance().getGame().getPhase().equals(Phase.BATTLE_PHASE);
            // todo remove this assert?
            GameController.getInstance().goNextPhase();
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
