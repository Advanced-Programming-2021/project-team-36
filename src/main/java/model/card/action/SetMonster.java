package model.card.action;

import lombok.Getter;
import model.card.Monster;
import model.enums.SummonType;

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
}
