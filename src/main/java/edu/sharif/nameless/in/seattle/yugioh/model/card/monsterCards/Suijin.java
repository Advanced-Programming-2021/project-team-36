package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

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
            if (stillHasPower) {
                boolean confirm = GameController.getInstance().getPlayerControllerByPlayer(this.owner).askRespondToQuestion(
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
    public int getSpeed() {
        return 2;
    }
}