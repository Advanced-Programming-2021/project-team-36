package YuGiOh.model.card.action;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import lombok.Getter;

public class MagicActivation extends Event {
    @Getter
    private final Card card;

    public MagicActivation(Card card){
        this.card = card;
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
