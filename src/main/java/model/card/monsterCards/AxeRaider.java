package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class AxeRaider extends Monster {
    public AxeRaider() {
        super("AxeRaider",
                "An axe-wielding monster of tremendous strength and agility.",
                3100,
                1700,
                1150,
                MonsterAttribute.EARTH,
                MonsterType.WARRIOR,
                MonsterCardType.NORMAL,
                4
        );
    }
}
