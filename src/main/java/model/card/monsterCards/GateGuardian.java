package model.card.monsterCards;

import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class GateGuardian extends Monster {
    public GateGuardian(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    // todo tell Shayan what special summon means and then he will implement this
}
