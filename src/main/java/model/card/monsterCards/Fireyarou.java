package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class Fireyarou extends Monster {
    public Fireyarou() {
        super("Fireyarou",
                "A malevolent creature wrapped in flames that attacks enemies with intense fire.",
                2500,
                1300,
                1000,
                MonsterAttribute.FIRE,
                MonsterType.PYRO,
                MonsterCardType.NORMAL,
                4
        );
    }
}
