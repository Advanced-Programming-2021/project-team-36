package YuGiOh.model.card.event;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import lombok.Getter;

public class MonsterActivation extends Event {
    @Getter
    private final Card card;

    public MonsterActivation(Monster monster){
        this.card = monster;
    }

    @Override
    public int getSpeed() {
        return card.getSpeed();
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to activate effect of %s?", card.getName());
    }

    @Override
    public String getDescription(){
        return "activate magic";
    }
}
