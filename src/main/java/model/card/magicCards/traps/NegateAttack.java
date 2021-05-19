package model.card.magicCards.traps;

import controller.GameController;
import model.card.Trap;
import model.card.action.Action;
import model.card.action.AttackEvent;
import model.card.action.Effect;
import model.enums.Icon;
import model.enums.Phase;
import model.enums.Status;

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