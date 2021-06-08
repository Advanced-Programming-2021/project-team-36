package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

public class Marshmaloon extends Monster {
    public Marshmaloon(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    // todo. is it correct? your player may get damage even if this card does not get destroyed!

    @Override
    public void tryToSendToGraveYardOfMe(){
        // it is really nothing!
        // this monster cannot be killed!
    }

    @Override
    public Effect onBeingAttackedByMonster(Monster attacker){
        return ()->{
            if(!isFacedUp())
                GameController.getInstance().decreaseLifePoint(
                        GameController.getInstance().getGame().getOtherPlayer(this.owner),
                        1000
                );
            changeFromHiddenToOccupiedIfCanEffect().run();
            damageStep(attacker);
        };
    }
}
