package YuGiOh.view.gui.Transitions;

import YuGiOh.view.gui.CardFrame;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

public class JumpingAnimation extends ReversibleTransition {
    private final CardFrame cardFrame;

    {
        setCycleDuration(Duration.millis(300));
    }

    public JumpingAnimation(CardFrame cardFrame, SimpleBooleanProperty animationStateProperty) {
        super(animationStateProperty);
        this.cardFrame = cardFrame;
    }

    @Override
    protected void interpolate(double frac) {
        cardFrame.setTranslateY(-frac * cardFrame.getHeight() / 3);
    }
}
