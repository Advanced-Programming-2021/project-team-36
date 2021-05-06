package model.card;

import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.State;

public class Monster extends Card {
    // this should be abstract too. todo
    protected int attackDamage;
    protected int defenseRate;
    protected MonsterAttribute attribute;
    protected MonsterType monsterType;
    protected MonsterCardType monsterCardType;
    protected State state;
    protected int level;

    protected Monster(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price);
        this.attackDamage = attackDamage;
        this.defenseRate = defenseRate;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.monsterCardType = monsterCardType;
        this.state = null;
        this.level = level;
    }
    public Monster(Monster monster){
        super(monster.name, monster.description, monster.price);
        this.attackDamage = monster.attackDamage;
        this.defenseRate = monster.defenseRate;
        this.attribute = monster.attribute;
        this.monsterType = monster.monsterType;
        this.monsterCardType = monster.monsterCardType;
        this.state = monster.state;
        this.level = monster.level;
    }
    public Integer getAttackDamage() {
        return attackDamage;
    }

    public Integer getDefenseRate() {
        return defenseRate;
    }

    public Integer getLevel() {
        return level;
    }

    public boolean canSummonNormally() {
        return true;
    }
}
