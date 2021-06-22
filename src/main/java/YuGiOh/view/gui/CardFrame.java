package YuGiOh.view.gui;

import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.view.gui.event.ClickOnCard;
import YuGiOh.view.gui.event.DropCard;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;

import java.util.ArrayList;

public class CardFrame extends Pane {
    private final BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    private final Card card;
    private double mouseDifX, mouseDifY;
    private SimpleBooleanProperty inHandObservable;
    private DoubleBinding widthProperty, heightProperty;
    private ImageView imageView = new ImageView();
    private Image faceUpImage, faceDownImage;
    private GameField gameRoot;

    public Image getImage(){
        return imageView.getImage();
    }

    private final Animation flipCardAnimation = new Transition() {
        {
            setCycleDuration(Duration.millis(600));
        }

        @Override
        protected void interpolate(double frac) {
            bindImage(frac >= 0.5);
            DoubleBinding newWidthProperty = widthProperty.multiply((frac - 0.5) * (frac - 0.5) * 4);
            bindImageWidth(widthProperty.multiply((frac - 0.5) * (frac - 0.5) * 4));
            imageView.setTranslateX((widthProperty.get() - newWidthProperty.get()) / 2);
        }
    };

    private void bindImageWidth(DoubleBinding binding){
        imageView.fitWidthProperty().unbind();
        imageView.fitWidthProperty().bind(binding);
    }
    private void bindImageHeight(DoubleBinding binding){
        imageView.fitHeightProperty().unbind();
        imageView.fitHeightProperty().bind(binding);
    }
    private void bindImage(boolean reverse){
        imageView.imageProperty().unbind();
        if(reverse)
            imageView.imageProperty().bind(Bindings.when(card.facedUpProperty().or(inHandObservable)).then(faceDownImage).otherwise(faceUpImage));
        else
            imageView.imageProperty().bind(Bindings.when(card.facedUpProperty().or(inHandObservable)).then(faceUpImage).otherwise(faceDownImage));
    }

    CardFrame(GameField gameRoot, Card card, DoubleBinding widthProperty, DoubleBinding heightProperty){
        super();

        this.gameRoot = gameRoot;

        getChildren().add(imageView);

        this.faceDownImage = new Image(Utils.getAsset("Cards/hidden.png").toURI().toString());
        this.faceUpImage = Utils.getCardImage(card);

        this.card = card;

        // todo implement this in hand observable better
        this.inHandObservable = new SimpleBooleanProperty(true);
        card.owner.getBoard().getCardsOnHand().addListener((InvalidationListener) (o)->{
            inHandObservable.set(card.owner.getBoard().getCardsOnHand().contains(card));
        });


        this.widthProperty = widthProperty;
        this.heightProperty = heightProperty;

        bindImage(false);
        bindImageHeight(heightProperty);
        bindImageWidth(widthProperty);
        minWidthProperty().bind(widthProperty);
        minHeightProperty().bind(heightProperty);

        if(card instanceof Monster)
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));

        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new DropShadow(28, Color.BLUE))
                        .otherwise(new DropShadow())
        );

        setOnMouseClicked(e->{
            // todo we haven't handled clicking on dead cards
            fireEvent(new ClickOnCard(this));
        });
        setOnMousePressed(e->{
            mouseDifX = getTranslateX() - e.getSceneX();
            mouseDifY = getTranslateY() - e.getSceneY();
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
        setOnMouseEntered(e->{
            if(!card.isFacedUp() && !inHandObservable.get()) {
                flipCardAnimation.setRate(Math.abs(flipCardAnimation.getRate()));
                flipCardAnimation.play();
            }
        });
        setOnMouseExited(e->{
            if(!card.isFacedUp() && !inHandObservable.get()) {
                flipCardAnimation.setRate(-Math.abs(flipCardAnimation.getRate()));
                flipCardAnimation.play();
            }
        });
        setOnContextMenuRequested(e->{
            ContextMenu contextMenu = new ContextMenu();
            int buttonFontSize = 15;
            contextMenu.getItems().addAll(
                    new MenuItem("", new CustomButton("summon", buttonFontSize, ()-> gameRoot.runAndAlert(
                        ()-> DuelMenuController.getInstance().summonCard(card),
                        ()->{}
                    ))),
                    new MenuItem("", new CustomButton("set", buttonFontSize, ()-> gameRoot.runAndAlert(
                            ()-> DuelMenuController.getInstance().setCard(card),
                            ()->{}
                    ))),
                    new MenuItem("", new CustomButton("change position", buttonFontSize, ()->{
                        ArrayList<CustomButton> buttons = new ArrayList<>();
                        for(MonsterState state : new MonsterState[]{MonsterState.DEFENSIVE_HIDDEN, MonsterState.DEFENSIVE_OCCUPIED, MonsterState.OFFENSIVE_OCCUPIED}){
                            buttons.add(new CustomButton(state.getName(), buttonFontSize, ()->{
                                gameRoot.runAndAlert(
                                        ()->DuelMenuController.getInstance().changeCardPosition(card, state),
                                        ()->{}
                                );
                            }));
                        }
                        new AlertBox().display(gameRoot, "choose state", buttons);
                    })),
                    new MenuItem("", new CustomButton("flip summon", buttonFontSize, ()->gameRoot.runAndAlert(
                            ()-> DuelMenuController.getInstance().flipSummon(card),
                            ()->{}
                    ))),
                    new MenuItem("", new CustomButton("activate effect", buttonFontSize, ()->{
                        gameRoot.runAndAlert(
                                ()-> DuelMenuController.getInstance().activateEffect(card),
                                ()->{}
                        );
                    })),
                    new MenuItem("", new CustomButton("direct attack", buttonFontSize, ()->gameRoot.runAndAlert(
                            ()-> DuelMenuController.getInstance().directAttack(card),
                            ()->{}
                    )))
            );
            contextMenu.getItems().forEach(item->{
                item.setOnAction(E-> item.getGraphic().getOnMouseClicked().handle(null));
            });
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
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

    public void moveByBindingCoordinates(DoubleBinding x, DoubleBinding y, Duration animationDuration, boolean visible, Runnable afterAnimation){
        synchronized (this) {
            setVisible(visible);
            layoutXProperty().unbind();
            layoutYProperty().unbind();
            TranslateTransition tt = new TranslateTransition(animationDuration, this);
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

    public void select(){
        isSelected.set(true);
    }
    public void deselect(){
        isSelected.set(false);
    }
}
