package edu.sharif.nameless.in.seattle.yugioh.view.gui.event;

import edu.sharif.nameless.in.seattle.yugioh.view.gui.CardFrame;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import lombok.Getter;

public class DropCard extends Event {
    public static final EventType<DropCard> MY_TYPE = new EventType<>("drop card event");

    @Getter
    private final Bounds bounds;
    @Getter
    private final CardFrame cardFrame;

    public DropCard(CardFrame cardFrame, Bounds bounds) {
        super(MY_TYPE);
        this.cardFrame = cardFrame;
        this.bounds = bounds;
    }
}
