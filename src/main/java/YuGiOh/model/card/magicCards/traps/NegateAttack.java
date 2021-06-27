package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Phase;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.AttackEvent;
import YuGiOh.utils.CustomPrinter;

public class NegateAttack extends Trap {
    public NegateAttack(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return ()->{
            getChain().pop();
            GameController.getInstance().goNextPhase();
            GameController.getInstance().getPlayerControllerByPlayer(this.owner).moveCardToGraveYard(this);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
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
