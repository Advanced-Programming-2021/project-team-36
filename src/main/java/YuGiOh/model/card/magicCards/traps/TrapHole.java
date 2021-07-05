package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.SummonType;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.utils.CustomPrinter;

public class TrapHole extends Trap {
    public TrapHole(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        Monster monster = ((SummonEvent) getChain().peek().getEvent()).getMonster();
        return ()->{
            GameController.getInstance().moveCardToGraveYard(monster);
            GameController.getInstance().moveCardToGraveYard(this);
            getChain().pop(); // do not summon
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if(getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if(action.getEvent() instanceof SummonEvent){
            SummonEvent event = (SummonEvent) action.getEvent();
            SummonType summonType = event.getSummonType();
            if(summonType.equals(SummonType.NORMAL) || summonType.equals(SummonType.FLIP))
                return event.getMonster().getAttackDamage() >= 1000;
        }
        return false;
    }
}
