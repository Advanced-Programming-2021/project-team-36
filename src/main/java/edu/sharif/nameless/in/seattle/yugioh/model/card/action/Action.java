package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import lombok.Getter;

public class Action {
    @Getter
    private final Event event;
    @Getter
    private final Effect effect;

    public Action(Event event, Effect effect){
        this.event = event;
        this.effect = effect;
    }
}
