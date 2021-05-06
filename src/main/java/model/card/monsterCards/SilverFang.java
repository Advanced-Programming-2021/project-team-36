package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class SilverFang extends Monster {
    public SilverFang() {
        super("SilverFang",
                "A small fiend that dwells in the dark, its single horn makes it a formidable opponent.",
                1700,
                1200,
                800,
                MonsterAttribute.EARTH,
                MonsterType.BEAST,
                MonsterCardType.NORMAL,
                3
        );
    }
}
