package model.card.action;

import lombok.Getter;
import model.card.Monster;

public abstract class AttackEvent extends Event{
    @Getter
    private final Monster attacker;

    public AttackEvent(Monster attacker){
        this.attacker = attacker;
    }
}
