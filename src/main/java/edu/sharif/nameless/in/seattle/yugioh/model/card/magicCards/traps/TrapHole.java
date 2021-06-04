package edu.sharif.nameless.in.seattle.yugioh.model.card.magicCards.traps;

import edu.sharif.nameless.in.seattle.yugioh.model.card.action.SummonEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.SummonType;
import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Trap;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Status;

public class TrapHole extends Trap {
    public TrapHole(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        Monster monster = ((SummonEvent) getChain().peek().getEvent()).getMonster();
        return ()->{
            GameController.getInstance().getPlayerControllerByPlayer(monster.owner).moveCardToGraveYard(monster);
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
