package YuGiOh.archive.view.gui.transition;

import YuGiOh.archive.view.gui.component.CardFrame;
import javafx.beans.binding.BooleanBinding;
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
