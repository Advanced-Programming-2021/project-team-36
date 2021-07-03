package YuGiOh.view.gui;

import YuGiOh.controller.MainGameThread;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class PileOfCardManager extends Button {
    private final List<CardFrame> cardFrames;
    private final Direction openButtonPosition;
    private final DoubleBinding closeX, closeY, openX, openY, startX, startY;

    private boolean open = false;

    public PileOfCardManager(Direction openButtonPosition, DoubleBinding startX, DoubleBinding startY, DoubleBinding closeX, DoubleBinding closeY, DoubleBinding openX, DoubleBinding openY) {
        this.openButtonPosition = openButtonPosition;
        this.startX = startX;
        this.startY = startY;
        this.closeX = closeX;
        this.closeY = closeY;
        this.openX = openX;
        this.openY = openY;
        cardFrames = new ArrayList<>();

        setOnMouseClicked(e-> toggle());
        setShape(new Circle(10, Color.BLUE));
        layoutXProperty().bind(startX);
        layoutYProperty().bind(startY);
    }

    public void open() {
        if(!open)
            open = true;
        cardFrames.forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(true));
        refresh();
    }
    public void close() {
        if(open)
            open = false;
        cardFrames.forEach(cardFrame -> cardFrame.getForceFlipCardAnimation().set(false));
        refresh();
    }
    public void toggle() {
        if(open)
            close();
        else
            open();
    }
    public void setCardFrames(List<CardFrame> cardFrames) {
        close();
        this.cardFrames.clear();
        this.cardFrames.addAll(cardFrames);
    }
    private void refresh() {
        MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui(()-> {
            DoubleBinding lastX = open ? openX : closeX;
            DoubleBinding lastY = open ? openY : closeY;
            for (int i = 0; i < cardFrames.size(); i++) {
                cardFrames.get(i).moveByBindingCoordinates(
                        startX.add(lastX.add(startX.negate()).divide(cardFrames.size()).multiply(i)),
                        startY.add(lastY.add(startY.negate()).divide(cardFrames.size()).multiply(i))
                );
                cardFrames.get(i).toFront();
            }
            toFront();
        });
    }
}
