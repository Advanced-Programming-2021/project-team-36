package YuGiOh.view.gui.transition;

import YuGiOh.view.gui.component.CardFrame;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

public class JumpingAnimation extends ReversibleTransition {
    private final CardFrame cardFrame;

    {
        setCycleDuration(Duration.millis(300));
    }

    public JumpingAnimation(CardFrame cardFrame, BooleanBinding animationStateProperty) {
        super(animationStateProperty);
        this.cardFrame = cardFrame;
    }

    @Override
    protected void interpolate(double frac) {
        cardFrame.setTranslateY(-frac * cardFrame.getHeight() / 3);
    }
}