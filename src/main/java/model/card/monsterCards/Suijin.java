package model.card.monsterCards;

import controller.menu.DuelMenuController;
import model.card.action.Effect;
import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import view.DuelMenuView;

public class Suijin extends Monster {
    boolean stillHasPower = true;

    public Suijin(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public Effect onBeingAttackedByMonster(Monster attacker) {
        // todo It's just a sample. correct it.
        return () -> {
            changeFromHiddenToOccupiedIfCanEffect().run();
            int _attackDamage = attacker.getAttackDamage();
            if(stillHasPower) {
                boolean confirm = ((DuelMenuView) DuelMenuController.getInstance().getView()).askUser(
                        "Do you want to activate Suijin's effect?", "yes", "no");
                if (confirm) {
                    stillHasPower = false;
                    attacker.setAttackDamage(0);
                }
            }
            damageStep(attacker);
            attacker.setAttackDamage(_attackDamage);
        };
    }

    @Override
    public Monster clone() {
        Suijin cloned = (Suijin) super.clone();
        cloned.stillHasPower = true;
        return cloned;
    }

    @Override
    public int getSpeed(){
        return 2;
    }
}