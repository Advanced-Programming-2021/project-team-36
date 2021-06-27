package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;

public class Suijin extends Monster {
    boolean stillHasPower = true;

    public Suijin(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public void specialEffectWhenBeingAttacked(Monster attacker) {
        int _attackDamage = attacker.getAttackDamage();
        if (stillHasPower) {
            boolean confirm = GameController.getInstance().getPlayerControllerByPlayer(this.owner).askRespondToQuestion(
                    "Do you want to activate Suijin's effect?", "yes", "no");
            if (confirm) {
                stillHasPower = false;
                attacker.setAttackDamage(0);
                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this.asEffect(), Color.Gray);
            }
        }
        damageStep(attacker);
        attacker.setAttackDamage(_attackDamage);
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