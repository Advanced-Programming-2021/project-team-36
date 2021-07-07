package YuGiOh.archive.view.gui.event;

import YuGiOh.model.card.action.Action;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class GameActionEvent extends Event {
    public static final EventType<GameActionEvent> MY_TYPE = new EventType<>("game action event");

    @Getter
    private Action action;

    public GameActionEvent(Action action){
        super(MY_TYPE);
        this.action = action;
    }
}
