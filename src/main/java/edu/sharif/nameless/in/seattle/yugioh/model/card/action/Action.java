package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import lombok.Getter;

public class Action {
    @Getter
    private final Event event;
    private final Effect effect;

    public Action(Event event, Effect effect){
        this.event = event;
        this.effect = effect;
    }

    public void runEffect() throws ResistToChooseCard {
        try {
            effect.run();
        } catch (LogicException e){
            // this must never happen :))
            e.printStackTrace();
        }
    }
}
