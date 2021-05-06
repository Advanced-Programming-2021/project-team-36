package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class HornImp extends Monster {
    public HornImp() {
        super("HornImp",
                "A small fiend that dwells in the dark, its single horn makes it a formidable opponent.",
                2500,
                1300,
                1000,
                MonsterAttribute.DARK,
                MonsterType.FIEND,
                MonsterCardType.NORMAL,
                4
        );
    }
}
