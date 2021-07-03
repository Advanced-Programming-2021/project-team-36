package YuGiOh.view.gui.Transitions;

import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

abstract public class ReversibleTransition extends Transition {
    private final SimpleBooleanProperty isActive = new SimpleBooleanProperty(true);
    private final NumberBinding direction;

    public ReversibleTransition(SimpleBooleanProperty animationStateProperty) {
        super();
        this.direction = Bindings.when(animationStateProperty.and(isActive))
                .then(1)
                .otherwise(-1);
        rateProperty().bind(direction);
        animationStateProperty.addListener((InvalidationListener) o-> refresh());
    }

    public void refresh() {
        int playDirection = 0;
        if(getCurrentTime().equals(Duration.ZERO))
            playDirection = 1;
        if(getCurrentTime().equals(getTotalDuration()))
            playDirection = -1;
        if(playDirection == (int) direction.getValue())
            play();
    }
    public void start() {
        refresh();
    }
    public void activate() {
        isActive.set(true);
    }
    public void deactivate() {
        isActive.set(false);
    }
}