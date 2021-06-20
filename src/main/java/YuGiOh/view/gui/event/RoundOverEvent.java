package YuGiOh.view.gui.event;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class RoundOverEvent extends Event {
    public static final EventType<RoundOverEvent> MY_TYPE = new EventType<>("round over event");

    @Getter
    RoundOverExceptionEvent exceptionEvent;

    public RoundOverEvent(RoundOverExceptionEvent exceptionEvent){
        super(MY_TYPE);
        this.exceptionEvent = exceptionEvent;
    }
}
