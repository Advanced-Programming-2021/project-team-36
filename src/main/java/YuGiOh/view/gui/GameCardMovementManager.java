package YuGiOh.view.gui;

import YuGiOh.controller.MainGameThread;
import YuGiOh.view.gui.component.CardFrame;
import YuGiOh.view.gui.component.GameField;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.util.Duration;

public class GameCardMovementManager {
    private final GameField gameField;
    public GameCardMovementManager(GameField gameField) {
        this.gameField = gameField;
    }

    public void animateCardMoving(CardFrame cardFrame, RatioLocation ratioLocation, Duration animationDuration, boolean blocking){
        animateCardMoving(
                cardFrame,
                gameField.widthProperty().multiply(ratioLocation.xRatio),
                gameField.heightProperty().multiply(ratioLocation.yRatio),
                animationDuration,
                blocking
        );
    }

    private void animateCardMoving(CardFrame cardFrame, DoubleBinding x, DoubleBinding y, Duration animationDuration, boolean blocking) {
        Runnable runnable = ()-> {
            cardFrame.layoutXProperty().unbind();
            cardFrame.layoutYProperty().unbind();
            TranslateTransition tt = new TranslateTransition(animationDuration, cardFrame);
            tt.setFromX(0);
            tt.setFromY(0);
            tt.toXProperty().bind(x.add(cardFrame.getCenterXProperty().negate()));
            tt.toYProperty().bind(y.add(cardFrame.getCenterYProperty().negate()));
            tt.setOnFinished(e -> {
                cardFrame.moveByBindingCoordinates(x, y);
                cardFrame.setTranslateX(0);
                cardFrame.setTranslateY(0);
                if(blocking)
                    MainGameThread.getInstance().unlockTheThreadIfMain();
            });
            tt.play();
        };
        if(blocking)
            MainGameThread.getInstance().onlyBlockRunningThreadThenDoInGui(runnable);
        else
            Platform.runLater(runnable);
    }
}
