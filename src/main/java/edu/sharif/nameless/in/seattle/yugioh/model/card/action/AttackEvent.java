package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import lombok.Getter;

public abstract class AttackEvent extends Event{
    @Getter
    private final Monster attacker;

    public AttackEvent(Monster attacker){
        this.attacker = attacker;
    }

    @Override
    public int getSpeed() {
        return 1;
    }
}
