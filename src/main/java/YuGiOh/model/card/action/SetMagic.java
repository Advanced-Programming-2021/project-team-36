package YuGiOh.model.card.action;

import lombok.Getter;
import YuGiOh.model.card.Magic;

public class SetMagic extends Event {
    @Getter
    Magic magic;
    public SetMagic(Magic magic){
        this.magic = magic;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to set %s?", magic.getName());
    }
}
