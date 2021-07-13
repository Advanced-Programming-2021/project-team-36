package YuGiOh.view.game;

import YuGiOh.view.game.component.CardFrame;
import YuGiOh.view.game.component.GameField;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GameCardMovementManager {
    private final GameField gameField;
    public GameCardMovementManager(GameField gameField) {
        this.gameField = gameField;
    }
    private HashMap<CardFrame, CompletableFuture<Void>> cardsAnimating = new HashMap<>();

    public synchronized CompletableFuture<Void> animateCardMoving(CardFrame cardFrame, RatioLocation ratioLocation, Duration animationDuration){
        return animateCardMoving(
                cardFrame,
                gameField.widthProperty().multiply(ratioLocation.xRatio),
                gameField.heightProperty().multiply(ratioLocation.yRatio),
                animationDuration
        );
    }

    private synchronized CompletableFuture<Void> animateCardMoving(CardFrame cardFrame, DoubleBinding x, DoubleBinding y, Duration animationDuration) {
        if(!cardsAnimating.containsKey(cardFrame) || cardsAnimating.get(cardFrame).isDone()) {
            cardsAnimating.put(cardFrame, CompletableFuture.completedFuture(null));
        }
        return cardsAnimating.get(cardFrame).thenCompose(res-> {
            CompletableFuture<Void> ret = new CompletableFuture<>();
            DoubleBinding xChange = x.add(cardFrame.getCenterXProperty().negate());
            DoubleBinding yChange = y.add(cardFrame.getCenterYProperty().negate());
            boolean bigEnough = (Math.abs(xChange.get()) + Math.abs(yChange.get())) > 5;

            if (bigEnough) {
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
                    ret.complete(null);
                });
                tt.play();
            } else {
                cardFrame.moveByBindingCoordinates(x, y);
                ret.complete(null);
            }
            return ret;
        });
    }
}
