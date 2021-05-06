package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class HornImp extends Monster {
    public HornImp() {
        name = "Horn Imp";
        level = 4;
        attribute = MonsterAttribute.DARK;
        monsterType = MonsterType.FIEND;
        monsterCardType = MonsterCardType.NORMAL;
        attackDamage = 1300;
        defenseRate = 1000;
        description = "A small fiend that dwells in the dark, its single horn makes it a formidable opponent.";
        price = 2500;
    }
}
