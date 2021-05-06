package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class Fireyarou extends Monster {
    public Fireyarou() {
        name = "Fireyarou";
        level = 4;
        attribute = MonsterAttribute.FIRE;
        monsterType = MonsterType.PYRO;
        monsterCardType = MonsterCardType.NORMAL;
        attackDamage = 1300;
        defenseRate = 1000;
        description = "A malevolent creature wrapped in flames that attacks enemies with intense fire.";
        price = 2500;
    }
}
