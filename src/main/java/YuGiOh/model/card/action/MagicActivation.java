package YuGiOh.model.card.action;

import lombok.Getter;
import YuGiOh.model.card.Magic;

public class MagicActivation extends Event {
    @Getter
    private final Magic magic;

    public MagicActivation(Magic magic){
        this.magic = magic;
    }

    @Override
    public int getSpeed() {
        return magic.getSpeed();
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to activate effect of %s?", magic.getName());
    }
}
