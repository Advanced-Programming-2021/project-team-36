package YuGiOh.archive.view.gui;

import YuGiOh.model.card.event.Event;
import YuGiOh.archive.view.gui.component.GameField;
import YuGiOh.archive.view.gui.event.GameActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import lombok.Getter;

public class GuiReporter {
    @Getter
    private static GuiReporter instance;
    private final GameField gameField;

    public GuiReporter(GameField gameField){
        instance = this;
        this.gameField = gameField;
    }
    public void report(javafx.event.Event event){
        gameField.fireEvent(event);
    }
    public <T extends Event> void addGameEventHandler(GameEventHandler<T> handler){
        gameField.addEventHandler(GameActionEvent.MY_TYPE, e->{
            T event;
            try {
                event = (T) e.getAction().getEvent();
                handler.handle(event);
            } catch (ClassCastException ignored){
            }
        });
    }
    public final <T extends javafx.event.Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        gameField.addEventHandler(eventType, eventHandler);
    }

    public interface GameEventHandler <T extends Event>{
        void handle(T gameEvent);
    }
}
