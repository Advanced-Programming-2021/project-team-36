package model.card.action;

import lombok.Getter;
import model.card.Monster;

public class MonsterAttackEvent extends AttackEvent {
    @Getter
    private final Monster defender;

    public MonsterAttackEvent(Monster attacker, Monster defender){
        super(attacker);
        this.defender = defender;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to attack %s with %s?", defender.getName(), getAttacker().getName());
    }
}
