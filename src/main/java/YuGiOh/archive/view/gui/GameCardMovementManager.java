package YuGiOh.archive.view.gui;

import YuGiOh.controller.MainGameThread;
import YuGiOh.archive.view.gui.component.CardFrame;
import YuGiOh.archive.view.gui.component.GameField;
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
        DoubleBinding xChange = x.add(cardFrame.getCenterXProperty().negate());
        DoubleBinding yChange = y.add(cardFrame.getCenterYProperty().negate());
        boolean bigEnough = (Math.abs(xChange.get()) + Math.abs(yChange.get())) > 5;
        MainGameThread.OneWayTicket ticket = new MainGameThread.OneWayTicket();
        Runnable runnable = ()->{
            cardFrame.layoutXProperty().unbind();
            cardFrame.layoutYProperty().unbind();
            TranslateTransition tt = new TranslateTransition(animationDuration, cardFrame);
            tt.setFromX(0);
            tt.setFromY(0);
            tt.toXProperty().bind(xChange);
            tt.toYProperty().bind(yChange);
            tt.setOnFinished(e -> {
                cardFrame.moveByBindingCoordinates(x, y);
                cardFrame.setTranslateX(0);
                cardFrame.setTranslateY(0);
                ticket.free();
            });
            tt.play();
        };

        if(blocking & animationDuration.greaterThan(Duration.millis(10)) & bigEnough)
            ticket.runOneWay(runnable);
        else if (bigEnough)
            Platform.runLater(runnable);
        else
            cardFrame.moveByBindingCoordinates(x, y);
    }
}
