package edu.sharif.nameless.in.seattle.yugioh.view.gui;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.event.ClickOnCard;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.event.DropCard;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

public class CardFrame extends ImageView {
    private final BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    private final Card card;
    private double mouseDifX, mouseDifY;

    CardFrame(Card card, DoubleBinding widthProperty, DoubleBinding heightProperty){
        super();

        this.card = card;

        // todo implement this in hand observable better
        SimpleBooleanProperty inHandObservable = new SimpleBooleanProperty(true);
        card.owner.getBoard().getCardsOnHand().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                inHandObservable.set(card.owner.getBoard().getCardsOnHand().contains(card));
            }
        });

        imageProperty().bind(Bindings.when(card.facedUpProperty().or(inHandObservable)).then(getFacedUpImage()).otherwise(getHiddenImage()));
        fitWidthProperty().bind(widthProperty);
        fitHeightProperty().bind(heightProperty);

        if(card instanceof Monster)
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));

        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new Bloom())
                        .otherwise(new DropShadow())
        );

        setOnMouseClicked(e->{
            // todo we haven't handled clicking on dead cards
            fireEvent(new ClickOnCard(this));
        });

        setOnMousePressed(e->{
            mouseDifX = getLayoutX() - e.getSceneX();
            mouseDifY = getLayoutY() - e.getSceneY();
            setCursor(Cursor.MOVE);
        });
        setOnMouseReleased(e->{
            Bounds bounds = getBoundsInParent();
            setCursor(Cursor.HAND);
            moveByLayoutValue(0, 0);
            fireEvent(new DropCard(this, bounds));
        });
        setOnMouseDragged(e->{
            moveByLayoutValue(e.getSceneX() + mouseDifX, e.getSceneY() + mouseDifY);
        });
    }

    public void bindCoordinates(DoubleBinding x, DoubleBinding y){
        xProperty().bind(x);
        yProperty().bind(y);
    }
    public void moveByLayoutValue(double x, double y){
        setLayoutX(x);
        setLayoutY(y);
    }

    Image getHiddenImage(){
        return new Image(Utils.getAsset("Cards/hidden.png").toURI().toString());
    }
    Image getFacedUpImage(){
        return Utils.getCardImage(card);
    }
    public void select(){
        isSelected.set(true);
    }
    public void deselect(){
        isSelected.set(false);
    }
}
