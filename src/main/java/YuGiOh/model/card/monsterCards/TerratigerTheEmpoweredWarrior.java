package YuGiOh.model.card.monsterCards;

import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;

public class TerratigerTheEmpoweredWarrior extends Monster {
    public TerratigerTheEmpoweredWarrior(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    // todo pending special summon
}
