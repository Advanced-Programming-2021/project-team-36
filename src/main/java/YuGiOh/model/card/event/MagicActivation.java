package YuGiOh.model.card.event;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import lombok.Getter;

public class MagicActivation extends Event {
    @Getter
    private final Card card;

    public MagicActivation(Magic magic){
        this.card = magic;
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
