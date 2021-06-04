package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

public class SkullGuardian extends Monster {
    public SkullGuardian(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }
}
