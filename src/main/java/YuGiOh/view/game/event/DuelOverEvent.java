package YuGiOh.view.game.event;

import javafx.event.Event;
import javafx.event.EventType;

public class DuelOverEvent extends Event {
    public static final EventType<DuelOverEvent> MY_TYPE = new EventType<>("duel over event");

    public DuelOverEvent(){
        super(MY_TYPE);
    }
}
