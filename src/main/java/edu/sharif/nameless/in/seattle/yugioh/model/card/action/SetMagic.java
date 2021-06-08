package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import lombok.Getter;

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

    @Override
    public String getDescription(){
        return "set magic";
    }
}
