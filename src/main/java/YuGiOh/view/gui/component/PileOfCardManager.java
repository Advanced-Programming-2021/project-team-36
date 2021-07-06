package YuGiOh.view.gui.component;

import YuGiOh.controller.MainGameThread;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.view.gui.Direction;
import YuGiOh.view.gui.GameCardFrameManager;
import YuGiOh.view.gui.RatioLocation;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class PileOfCardManager extends StackPane {
    private final RatioLocation close, open, start;
    private final Text counterText;
    private final ZoneType zoneType;
    private final Player owner;
    private final GameCardFrameManager manager;
    private final GameField gameField;

    private boolean isOpen = false;

    public PileOfCardManager(GameField gameField, GameCardFrameManager manager, ZoneType zoneType, Player owner, RatioLocation start, RatioLocation close, RatioLocation open) {
        this.start = start;
        this.close = close;
        this.open = open;
        this.manager = manager;
        this.zoneType = zoneType;
        this.owner = owner;
        this.gameField = gameField;

        layoutXProperty().bind(gameField.widthProperty().multiply(start.xRatio));
        layoutYProperty().bind(gameField.heightProperty().multiply(start.yRatio));

        setOnMouseClicked(e-> toggle());
        setShape(new Circle(15));
        setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        setVisible(false);

        counterText = new Text();
        counterText.setFill(Color.WHEAT);
        counterText.setFont(Font.font(20));

        getChildren().add(counterText);

        refresh();
    }

    private List<CardFrame> getCardFrames() {
        return manager.getCardsByZoneAndPlayer(zoneType, owner);
    }

    public void open() {
        if(!isOpen)
            isOpen = true;
        getCardFrames().forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(true));
        refresh();
    }
    public void close() {
        if(isOpen)
            isOpen = false;
        getCardFrames().forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(false));
        refresh();
    }
    public void toggle() {
        if(isOpen)
            close();
        else
            open();
    }
    private void refresh() {
        MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui(()-> {
            DoubleBinding lastX = gameField.widthProperty().multiply(isOpen ? open.xRatio : close.xRatio);
            DoubleBinding lastY = gameField.heightProperty().multiply(isOpen ? open.yRatio : close.yRatio);
            DoubleBinding startX = gameField.widthProperty().multiply(start.xRatio);
            DoubleBinding startY = gameField.heightProperty().multiply(start.yRatio);

            List<CardFrame> cardFrames = getCardFrames();
            for (int i = 0; i < cardFrames.size(); i++) {
                cardFrames.get(i).moveByBindingCoordinates(
                        startX.add(lastX.add(startX.negate()).divide(cardFrames.size()).multiply(i)),
                        startY.add(lastY.add(startY.negate()).divide(cardFrames.size()).multiply(i))
                );
                cardFrames.get(i).toFront();
            }
            setViewOrder(-3);
            if(cardFrames.size() >= 1) {
                setVisible(true);
                counterText.setText(String.valueOf(cardFrames.size()));
            } else {
                setVisible(false);
            }
        });
    }
}
