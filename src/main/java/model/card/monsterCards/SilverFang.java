package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class SilverFang extends Monster {
    public SilverFang() {
        name = "Silver Fang";
        level = 3;
        attribute = MonsterAttribute.EARTH;
        monsterType = MonsterType.BEAST;
        monsterCardType = MonsterCardType.NORMAL;
        attackDamage = 1200;
        defenseRate = 800;
        description = "A snow wolf that's beautiful to the eye, but absolutely vicious in battle.";
        price = 1700;
    }
}
