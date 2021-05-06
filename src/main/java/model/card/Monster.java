package model.card;

import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.State;

public class Monster extends Card{
    // this should be abstract too. todo
    protected int attackDamage;
    protected int defenseRate;
    protected MonsterAttribute attribute;
    protected MonsterType monsterType;
    protected MonsterCardType monsterCardType;
    protected State state;
    protected int level;

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
