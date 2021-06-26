package YuGiOh.model.card.action;

import YuGiOh.model.enums.SummonType;
import lombok.Getter;
import YuGiOh.model.card.Monster;

public class SummonEvent extends Event {
    @Getter
    Monster monster;
    @Getter
    SummonType summonType;
    public SummonEvent(Monster monster, SummonType summonType){
        this.monster = monster;
        this.summonType = summonType;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to summon %s?", monster.getName());
    }

    @Override
    public String getDescription(){
        return "summon monster";
    }
}
