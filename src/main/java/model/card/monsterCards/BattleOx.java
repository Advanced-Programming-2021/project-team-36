package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class BattleOx extends Monster {
    public BattleOx() {
        name = "Battle OX";
        level = 4;
        attribute = MonsterAttribute.EARTH;
        monsterType = MonsterType.BEASTWARRIOR;
        monsterCardType = MonsterCardType.NORMAL;
        attackDamage = 1700;
        defenseRate = 1000;
        description = "A monster with tremendous power, it destroys enemies with a swing of its axe.";
        price = 2900;
    }
}
