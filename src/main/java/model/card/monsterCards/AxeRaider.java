package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class AxeRaider extends Monster {
    public AxeRaider() {
        name = "Axe Raider";
        level = 4;
        attribute = MonsterAttribute.EARTH;
        monsterType = MonsterType.WARRIOR;
        monsterCardType = MonsterCardType.NORMAL;
        attackDamage = 1700;
        defenseRate = 1150;
        description = "An axe-wielding monster of tremendous strength and agility.";
        price = 3100;
    }
}
