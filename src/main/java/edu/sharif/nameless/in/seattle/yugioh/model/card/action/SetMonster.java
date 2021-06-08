package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;

public class SetMonster extends Event {
    @Getter
    Monster monster;
    public SetMonster(Monster monster){
        this.monster = monster;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to set %s?", monster.getName());
    }

    @Override
    public String getDescription(){
        return "set monster";
    }
}
