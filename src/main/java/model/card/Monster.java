package model.card;

import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.State;

public class Monster extends Card{
    protected Integer attackDamage;
    protected Integer defenseRate;
    protected MonsterAttribute attribute;
    protected String monsterType;
    protected MonsterCardType cardType;
    protected State state;
    protected Integer level;

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
