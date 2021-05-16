package model.card.monsterCards;

import controller.GameController;
import model.card.Effect;
import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.ZoneType;

public class YomiShip extends Monster {
    public YomiShip(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public Effect onBeingAttackedByMonster(Monster attacker) {
        return ()-> {
            super.onBeingAttackedByMonster(attacker).run();
            if(GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD))
                attacker.tryToSendToGraveYardOfMe();
        };
    }
}
