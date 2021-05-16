package model.card.monsterCards;

import controller.GameController;
import controller.events.GameOver;
import model.Game;
import model.card.Effect;
import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.ZoneType;

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
            } catch (GameOver ignored){
            }
            if(GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD)) {
                attacker.tryToSendToGraveYardOfMe();
            }
            attacker.owner.setLifePoint(myLifePoint);
            attacker.owner.setLifePoint(opponentLifePoint);
        };
    }
}
