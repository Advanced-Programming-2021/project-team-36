package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Monster;

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
