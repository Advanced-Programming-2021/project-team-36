package YuGiOh.view.gui;

import YuGiOh.controller.MainGameThread;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class PileOfCardManager extends StackPane {
    private final List<CardFrame> cardFrames;
    private final Direction openButtonPosition;
    private final DoubleBinding closeX, closeY, openX, openY, startX, startY, cardWidthProperty, cardHeightProperty;
    private final Text counterText;

    private boolean open = false;

    // todo we don't use open button position now

    public PileOfCardManager(Direction openButtonPosition, DoubleBinding startX, DoubleBinding startY, DoubleBinding closeX, DoubleBinding closeY, DoubleBinding openX, DoubleBinding openY, DoubleBinding cardWidthProperty, DoubleBinding cardHeightProperty) {
        this.openButtonPosition = openButtonPosition;
        this.startX = startX;
        this.startY = startY;
        this.closeX = closeX;
        this.closeY = closeY;
        this.openX = openX;
        this.openY = openY;
        this.cardWidthProperty = cardWidthProperty;
        this.cardHeightProperty = cardHeightProperty;

        cardFrames = new ArrayList<>();

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
        close();
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
            if(cardFrames.size() >= 1) {
                setVisible(true);
                counterText.setText(String.valueOf(cardFrames.size()));
            } else {
                setVisible(false);
            }
        });
    }
}
