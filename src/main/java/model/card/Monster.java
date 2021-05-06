package model.card;

import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.MonsterState;

public class Monster extends Card {
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
        this.monsterCardType = monsterCardType;
        this.monsterState = null;
        this.level = level;
    }

    public Monster(Monster monster){
        super(monster.name, monster.description, monster.price);
        this.attackDamage = monster.attackDamage;
        this.defenseRate = monster.defenseRate;
        this.attribute = monster.attribute;
        this.monsterType = monster.monsterType;
        this.monsterCardType = monster.monsterCardType;
        this.monsterState = monster.monsterState;
        this.level = monster.level;
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
