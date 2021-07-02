package YuGiOh.view.gui;

import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.view.gui.event.ClickOnCardEvent;
import YuGiOh.view.gui.event.DropCardEvent;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;

import java.util.ArrayList;

public class CardFrame extends DraggablePane {
    private final BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    private final Card card;

    private final SimpleBooleanProperty inHandObservable;
    private final DoubleBinding widthProperty, heightProperty;
    private final ImageView imageView = new ImageView();
    private final Image faceUpImage, faceDownImage;

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
    private void bindImage(boolean forceFaceUp){
        imageView.imageProperty().unbind();
        if(forceFaceUp)
            imageView.setImage(faceUpImage);
        else
            imageView.imageProperty().bind(Bindings.when(card.facedUpProperty().or(inHandObservable)).then(faceUpImage).otherwise(faceDownImage));
    }

    CardFrame(GameField gameRoot, Card card, DoubleBinding widthProperty, DoubleBinding heightProperty){
        super();

        getChildren().add(imageView);

        this.faceDownImage = new Image(Utils.getAsset("Cards/hidden.png").toURI().toString());
        this.faceUpImage = Utils.getCardImage(card);

        this.card = card;

        // todo implement this in hand observable better
        this.inHandObservable = new SimpleBooleanProperty(true);
        card.owner.getBoard().getCardsOnHand().addListener((InvalidationListener) (o)->{
            inHandObservable.set(card.owner.getBoard().getCardsOnHand().contains(card));
        });

        DoubleBinding inHandCof = Bindings.when(inHandObservable).then(1.3).otherwise(1.0);
        this.widthProperty = widthProperty.multiply(inHandCof);
        this.heightProperty = heightProperty.multiply(inHandCof);

        bindImage(false);
        bindImageHeight(this.heightProperty);
        bindImageWidth(this.widthProperty);
        minWidthProperty().bind(this.widthProperty);
        minHeightProperty().bind(this.heightProperty);

        if(card instanceof Monster)
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));

        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new DropShadow(28, Color.BLUE))
                        .otherwise(new DropShadow())
        );
        setOnMouseClicked(e->{
            // todo we haven't handled clicking on dead cards
            GuiReporter.getInstance().report(new ClickOnCardEvent(this));
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
                    new MenuItem("", new CustomButton("summon", buttonFontSize, ()-> gameRoot.addRunnableToMainThreadForCard(
                            card,
                            ()-> DuelMenuController.getInstance().summonCard(card)
                    ))),
                    new MenuItem("", new CustomButton("special summon", buttonFontSize, ()-> gameRoot.addRunnableToMainThreadForCard(
                            card,
                            ()-> DuelMenuController.getInstance().specialSummon(card)
                    ))),
                    new MenuItem("", new CustomButton("set", buttonFontSize, ()-> gameRoot.addRunnableToMainThreadForCard(
                            card,
                            ()-> DuelMenuController.getInstance().setCard(card)
                    ))),
                    new MenuItem("", new CustomButton("change position", buttonFontSize, ()->{
                        ArrayList<CustomButton> buttons = new ArrayList<>();
                        for(MonsterState state : new MonsterState[]{MonsterState.DEFENSIVE_HIDDEN, MonsterState.DEFENSIVE_OCCUPIED, MonsterState.OFFENSIVE_OCCUPIED}){
                            buttons.add(new CustomButton(state.getName(), buttonFontSize, ()->{
                                gameRoot.addRunnableToMainThreadForCard(
                                        card,
                                        ()->DuelMenuController.getInstance().changeCardPosition(card, state)
                                );
                            }));
                        }
                        new AlertBox().display(gameRoot, "choose state", buttons);
                    })),
                    new MenuItem("", new CustomButton("flip summon", buttonFontSize, ()->gameRoot.addRunnableToMainThreadForCard(
                            card,
                            ()-> DuelMenuController.getInstance().flipSummon(card)
                    ))),
                    new MenuItem("", new CustomButton("activate effect", buttonFontSize, ()->{
                        gameRoot.addRunnableToMainThreadForCard(
                                card,
                                ()-> DuelMenuController.getInstance().activateEffect(card)
                        );
                    })),
                    new MenuItem("", new CustomButton("direct attack", buttonFontSize, ()->gameRoot.addRunnableToMainThreadForCard(
                            card,
                            ()-> DuelMenuController.getInstance().directAttack(card)
                    )))
            );
            contextMenu.getItems().forEach(item->{
                item.setOnAction(E-> item.getGraphic().getOnMouseClicked().handle(null));
            });
            contextMenu.setStyle("-fx-background-color: #006699;");
            contextMenu.show(this, e.getScreenX(), e.getScreenY());
        });
    }

    public void animateCardMoving(DoubleBinding x, DoubleBinding y, Duration animationDuration) {
        MainGameThread.getInstance().onlyBlockRunningThreadThenDoInGui(()-> {
            layoutXProperty().unbind();
            layoutYProperty().unbind();
            TranslateTransition tt = new TranslateTransition(animationDuration, this);
            tt.setFromX(0);
            tt.setFromY(0);
            tt.toXProperty().bind(x.add(layoutXProperty().negate()));
            tt.toYProperty().bind(y.add(layoutYProperty().negate()));
            tt.setOnFinished(e -> {
                moveByBindingCoordinates(x, y);
                setTranslateX(0);
                setTranslateY(0);
                MainGameThread.getInstance().unlockTheThreadIfMain();
            });
            tt.play();
        });
    }

    public void moveByBindingCoordinates(DoubleBinding x, DoubleBinding y){
        layoutXProperty().bind(x);
        layoutYProperty().bind(y);
        setTranslateX(0);
        setTranslateY(0);
    }

    public void select(){
        isSelected.set(true);
    }
    public void deselect(){
        isSelected.set(false);
    }

    @Override
    void onDrop(Bounds bounds) {
        GuiReporter.getInstance().report(new DropCardEvent(this, bounds));
    }

    @Override
    void onDrag() {
    }
}
