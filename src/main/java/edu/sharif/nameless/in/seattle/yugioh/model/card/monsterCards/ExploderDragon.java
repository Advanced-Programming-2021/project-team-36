package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.ZoneType;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

public class ExploderDragon extends Monster {
    public ExploderDragon(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public Effect onBeingAttackedByMonster(Monster attacker){
        return ()->{
            int myLifePoint = this.owner.getLifePoint();
            int opponentLifePoint = attacker.owner.getLifePoint();
            try{
                super.onBeingAttackedByMonster(attacker).run();
            } catch (RoundOverEvent ignored){
            }
            if(GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD)) {
                attacker.tryToSendToGraveYardOfMe();
            }
            this.owner.setLifePoint(myLifePoint);
            attacker.owner.setLifePoint(opponentLifePoint);
        };
    }
}
