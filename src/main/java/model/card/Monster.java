package model.card;

import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.MonsterState;

public class Monster extends Card{
    // this should be abstract too. todo
    protected int attackDamage;
    protected int defenseRate;
    protected MonsterAttribute attribute;
    protected MonsterType monsterType;
    protected MonsterCardType monsterCardType;
    protected MonsterState monsterState = null;
    protected int level;

    protected Monster(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price);
        this.attackDamage = attackDamage;
        this.defenseRate = defenseRate;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.level = level;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getDefenseRate() {
        return defenseRate;
    }

    public int getLevel() {
        return level;
    }

    public MonsterState getState() {
        return monsterState;
    }

    public boolean canSummonNormally() {
        return true;
    }
}
