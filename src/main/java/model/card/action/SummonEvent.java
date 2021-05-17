package model.card.action;

import lombok.Getter;
import model.card.Monster;
import model.enums.SummonType;

public class SummonEvent extends Event {
    @Getter
    Monster monster;
    @Getter
    SummonType summonType;
    public SummonEvent(Monster monster, SummonType summonType){
        this.monster = monster;
        this.summonType = summonType;
    }
}
