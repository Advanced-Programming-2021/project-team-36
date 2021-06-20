package YuGiOh.view.gui.event;

import YuGiOh.view.gui.CardFrame;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class ClickOnCard extends Event {
    public static final EventType<ClickOnCard> MY_TYPE = new EventType<ClickOnCard>("click on card");

    @Getter
    private final CardFrame cardFrame;

    public ClickOnCard(CardFrame cardFrame) {
        super(MY_TYPE);
        this.cardFrame = cardFrame;
    }
}
