package YuGiOh.view.gui;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.view.gui.event.ClickOnCard;
import YuGiOh.view.gui.event.DropCard;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;
import lombok.Getter;

public class CardFrame extends ImageView {
    private final BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    private final Card card;
    private double mouseDifX, mouseDifY;
//    private final DoubleProperty xCoordinateProperty = new SimpleDoubleProperty(), yCoordinateProperty = new SimpleDoubleProperty();

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
            mouseDifX = getX() - e.getSceneX();
            mouseDifY = getY() - e.getSceneY();
            setCursor(Cursor.MOVE);
        });
        setOnMouseReleased(e->{
            Bounds bounds = getBoundsInParent();
            setCursor(Cursor.HAND);
            moveByTranslateValue(0, 0);
            fireEvent(new DropCard(this, bounds));
        });
        setOnMouseDragged(e->{
            moveByTranslateValue(e.getSceneX() + mouseDifX, e.getSceneY() + mouseDifY);
        });
    }


    public void lockThisThread(){
        synchronized (this){
            try { wait(); } catch (InterruptedException e){}
        }
    }
    public void unlockThisThread(){
        synchronized (this){
            notify();
        }
    }

    public void moveByBindingCoordinates(DoubleBinding x, DoubleBinding y, boolean visible, Runnable afterAnimation){
        synchronized (this) {
            setVisible(visible);
            layoutXProperty().unbind();
            layoutYProperty().unbind();
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.0001), this);
            tt.setFromX(0);
            tt.setFromY(0);
            tt.toXProperty().bind(x.add(layoutXProperty().negate()));
            tt.toYProperty().bind(y.add(layoutYProperty().negate()));
            tt.setOnFinished(e->{
                layoutXProperty().bind(x);
                layoutYProperty().bind(y);
                setTranslateX(0);
                setTranslateY(0);
                setVisible(true);
                afterAnimation.run();
            });
            tt.play();
        }
    }
    public void moveByTranslateValue(double x, double y){
        setTranslateX(x);
        setTranslateY(y);
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
