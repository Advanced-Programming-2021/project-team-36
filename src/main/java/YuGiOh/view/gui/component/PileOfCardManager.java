package YuGiOh.view.gui.component;

import YuGiOh.controller.MainGameThread;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.view.gui.Direction;
import YuGiOh.view.gui.GameCardFrameManager;
import YuGiOh.view.gui.component.CardFrame;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PileOfCardManager extends StackPane {
    private final Direction openButtonPosition;
    private final DoubleBinding closeX, closeY, openX, openY, startX, startY;
    private final Text counterText;
    private final ZoneType zoneType;
    private final Player owner;
    private final GameCardFrameManager manager;

    private boolean open = false;

    // todo we don't use open button position now

    public PileOfCardManager(GameCardFrameManager manager, ZoneType zoneType, Player owner, Direction openButtonPosition, DoubleBinding startX, DoubleBinding startY, DoubleBinding closeX, DoubleBinding closeY, DoubleBinding openX, DoubleBinding openY) {
        this.openButtonPosition = openButtonPosition;
        this.startX = startX;
        this.startY = startY;
        this.closeX = closeX;
        this.closeY = closeY;
        this.openX = openX;
        this.openY = openY;
        this.manager = manager;
        this.zoneType = zoneType;
        this.owner = owner;

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

        refresh();
    }

    private List<CardFrame> getCardFrames() {
        return manager.getCardsByZoneAndPlayer(zoneType, owner);
    }

    public void open() {
        if(!open)
            open = true;
        getCardFrames().forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(true));
        refresh();
    }
    public void close() {
        if(open)
            open = false;
        getCardFrames().forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(false));
        refresh();
    }
    public void toggle() {
        if(open)
            close();
        else
            open();
    }
    private void refresh() {
        MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui(()-> {
            DoubleBinding lastX = open ? openX : closeX;
            DoubleBinding lastY = open ? openY : closeY;
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
