package YuGiOh.view.gui.transition;

import YuGiOh.view.gui.component.CardFrame;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

import java.util.Random;

public class CardRotateTransition extends ReversibleTransition {
    private final CardFrame cardFrame;

    public CardRotateTransition(CardFrame cardFrame, BooleanBinding animationStateProperty) {
        super(animationStateProperty);
        this.cardFrame = cardFrame;
    }

    {
        setCycleDuration(Duration.millis(600));
    }

    @Override
    protected void interpolate(double frac) {
        cardFrame.getForceImageFaceUp().set(frac >= 0.5);
        cardFrame.compressImage((frac - 0.5) * (frac - 0.5) * 4);
    }
}
