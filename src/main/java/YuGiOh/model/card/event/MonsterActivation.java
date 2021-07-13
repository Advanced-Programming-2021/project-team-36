package YuGiOh.model.card.event;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import lombok.Getter;

public class MonsterActivation extends Event {
    @Getter
    private final Monster monster;

    public MonsterActivation(Monster monster){
        this.monster = monster;
    }

    @Override
    public int getSpeed() {
        return monster.getSpeed();
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to activate effect of %s?", monster.getName());
    }

    @Override
    public String getDescription(){
        return "activate magic";
    }
}
