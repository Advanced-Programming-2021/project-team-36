package YuGiOh.archive.view.gui.transition;

import YuGiOh.archive.view.gui.component.CardFrame;
import javafx.beans.binding.BooleanBinding;
import javafx.util.Duration;

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
