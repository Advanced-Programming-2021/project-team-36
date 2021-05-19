package model.card.magicCards.traps;

import controller.GameController;
import model.card.Monster;
import model.card.Trap;
import model.card.action.Action;
import model.card.action.Effect;
import model.card.action.SummonEvent;
import model.enums.Icon;
import model.enums.Status;
import model.enums.SummonType;

public class TrapHole extends Trap {
    public TrapHole(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        Monster monster = ((SummonEvent) getChain().peek().getEvent()).getMonster();
        return ()->{
            GameController.getInstance().moveCardToGraveYard(monster);
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
