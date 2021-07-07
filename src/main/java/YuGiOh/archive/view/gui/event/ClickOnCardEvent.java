package YuGiOh.archive.view.gui.event;

import YuGiOh.archive.view.gui.component.CardFrame;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class ClickOnCardEvent extends Event {
    public static final EventType<ClickOnCardEvent> MY_TYPE = new EventType<>("click on card");

    @Getter
    private final CardFrame cardFrame;

    public ClickOnCardEvent(CardFrame cardFrame) {
        super(MY_TYPE);
        this.cardFrame = cardFrame;
    }
}
