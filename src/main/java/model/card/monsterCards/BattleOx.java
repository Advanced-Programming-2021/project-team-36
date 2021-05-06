package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class BattleOx extends Monster {
    public BattleOx() {
        super("BattleOx",
                "A monster with tremendous power, it destroys enemies with a swing of its axe.",
                2900,
                1700,
                1000,
                MonsterAttribute.EARTH,
                MonsterType.BEASTWARRIOR,
                MonsterCardType.NORMAL,
                4
        );
    }
}
