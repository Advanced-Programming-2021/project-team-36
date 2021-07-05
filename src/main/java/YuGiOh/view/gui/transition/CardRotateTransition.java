package YuGiOh.view.gui.transition;

import YuGiOh.view.gui.component.CardFrame;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Duration;

public class CardRotateTransition extends ReversibleTransition {
    private final CardFrame cardFrame;
    private final DoubleBinding originalWidth;

    public CardRotateTransition(CardFrame cardFrame, SimpleBooleanProperty animationStateProperty, DoubleBinding originalWidth) {
        super(animationStateProperty);
        this.cardFrame = cardFrame;
        this.originalWidth = originalWidth;
    }

    {
        setCycleDuration(Duration.millis(600));
    }

    @Override
    protected void interpolate(double frac) {
        cardFrame.getForceImageFaceUp().set(frac >= 0.5);
        DoubleBinding newWidthProperty = originalWidth.multiply((frac - 0.5) * (frac - 0.5) * 4);
        cardFrame.bindImageWidth(newWidthProperty);
        cardFrame.setImageTranslateX((originalWidth.get() - newWidthProperty.get()) / 2);
    }
}
