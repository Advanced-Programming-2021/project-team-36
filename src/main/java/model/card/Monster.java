package model.card;

import controller.GameController;
import controller.menu.DuelMenuController;
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

    @Override
    public Monster clone() {
        Monster cloned = (Monster) super.clone();
        cloned.attackDamage = attackDamage;
        cloned.defenseRate = defenseRate;
        cloned.attribute = attribute;
        cloned.monsterType = monsterType;
        cloned.monsterCardType = monsterCardType;
        cloned.monsterState = monsterState;
        cloned.level = level;
        return cloned;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getDefenseRate() {
        return defenseRate;
    }

    public void setMonsterState(MonsterState monsterState) {
        this.monsterState = monsterState;
    }

    public int getLevel() {
        return level;
    }

    public MonsterState getState() {
        return monsterState;
    }

    public boolean canSummonNormally() {
        // todo why is it always true?
        return true;
    }

    public void onBeingAttackedByMonster(Monster attacker){
        // todo are the responses ok? maybe we have to swap your and mine?
        // todo remove this System.outs!
        if (monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED)) {
            if (attacker.getAttackDamage() > this.getAttackDamage()) {
                int difference = attacker.getAttackDamage() - this.getAttackDamage();
                System.out.println(String.format("your opponent’s monster is destroyed and your opponent receives %d battle damage", difference));
                GameController.getInstance().moveCardToGraveYard(this);
                GameController.getInstance().decreaseLifePoint(this.owner, difference);
            }
            else if (attacker.getAttackDamage() == this.getAttackDamage()) {
                System.out.println("both you and your opponent monster cards are destroyed and no one receives damage");
                GameController.getInstance().moveCardToGraveYard(attacker);
                GameController.getInstance().moveCardToGraveYard(this);
            }
            else {
                int difference = this.getAttackDamage() - attacker.getAttackDamage();
                System.out.println(String.format("Your monster card is destroyed and you received %s battle damage", difference));
                GameController.getInstance().moveCardToGraveYard(attacker);
                GameController.getInstance().decreaseLifePoint(attacker.owner, difference);
            }
        }
        else if(monsterState.equals(MonsterState.DEFENSIVE_OCCUPIED)){
            // todo complete this
        }
        else if(monsterState.equals(MonsterState.DEFENSIVE_HIDDEN)){

        }
    }
}
