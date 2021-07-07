package YuGiOh.archive.view.gui.component;

import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.archive.view.gui.GameCardFrameManager;
import YuGiOh.archive.view.gui.RatioLocation;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class PileOfCardManager extends StackPane {
    private final DoubleBinding startX, startY, openX, openY;
    private final Text counterText;
    private final ZoneType zoneType;
    private final Player owner;
    private final GameCardFrameManager manager;
    private final GameField gameField;

    private boolean isOpen = false;

    public PileOfCardManager(GameField gameField, GameCardFrameManager manager, ZoneType zoneType, Player owner, RatioLocation start, RatioLocation open) {
        this.openX = gameField.widthProperty().multiply(open.xRatio);
        this.openY = gameField.heightProperty().multiply(open.yRatio);
        this.startX = gameField.widthProperty().multiply(start.xRatio);
        this.startY = gameField.heightProperty().multiply(start.yRatio);

        this.manager = manager;
        this.zoneType = zoneType;
        this.owner = owner;
        this.gameField = gameField;

        layoutXProperty().bind(startX);
        layoutYProperty().bind(startY);

        setOnMouseClicked(e-> toggle());
        setShape(new Circle(15));
        setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setVisible(false);

        counterText = new Text();
        counterText.setFill(Color.WHEAT);
        counterText.setFont(Font.font(20));

        getChildren().add(counterText);

        close(true);
    }

    private List<CardFrame> getCardFrames() {
        return manager.getCardsByZoneAndPlayer(zoneType, owner);
    }

    public void open(boolean force) {
        if(!isOpen | force) {
            isOpen = true;
            getCardFrames().forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(true));
            List<CardFrame> cardFrames = getCardFrames();
            for (int i = 0; i < cardFrames.size(); i++) {
                cardFrames.get(i).moveByBindingCoordinates(
                        startX.add(openX.add(startX.negate()).divide(cardFrames.size()).multiply(i)),
                        startY.add(openY.add(startY.negate()).divide(cardFrames.size()).multiply(i))
                );
            }
            Platform.runLater(()->{
                for (CardFrame cardFrame : cardFrames)
                    cardFrame.toFront();
            });
        }
        refreshNumber();
    }
    public void close(boolean force) {
        if(isOpen || force) {
            isOpen = false;
            List<CardFrame> cardFrames = getCardFrames();
            cardFrames.forEach(cardFrame -> {
                cardFrame.getForceFlipCardAnimation().set(false);
                cardFrame.moveByBindingCoordinates(startX, startY);
            });
        }
        refreshNumber();
    }
    public void toggle() {
        if(isOpen)
            close(false);
        else
            open(false);
    }
    private void refreshNumber() {
        Platform.runLater(()->{
            toFront();
            setViewOrder(-3);
            int number = getCardFrames().size();
            if(number >= 1) {
                setVisible(true);
                counterText.setText(String.valueOf(number));
            } else {
                setVisible(false);
            }
        });
    }
}
