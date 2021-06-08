package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import lombok.Getter;

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

    @Override
    public String getDescription(){
        return "activate magic";
    }
}
