package YuGiOh.view.gui.event;

import YuGiOh.view.gui.component.CardFrame;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import lombok.Getter;

public class DropCardEvent extends Event {
    public static final EventType<DropCardEvent> MY_TYPE = new EventType<>("drop card event");

    @Getter
    private final Bounds bounds;
    @Getter
    private final CardFrame cardFrame;

    public DropCardEvent(CardFrame cardFrame, Bounds bounds) {
        super(MY_TYPE);
        this.cardFrame = cardFrame;
        this.bounds = bounds;
    }
}
