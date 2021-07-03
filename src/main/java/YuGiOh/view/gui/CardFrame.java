package YuGiOh.view.gui;

import YuGiOh.controller.GameController;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.view.gui.Transitions.CardRotateTransition;
import YuGiOh.view.gui.Transitions.JumpingAnimation;
import YuGiOh.view.gui.event.ClickOnCardEvent;
import YuGiOh.view.gui.event.DropCardEvent;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;

public class CardFrame extends DraggablePane {
    private final BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    private final Card card;

    private final DoubleBinding widthProperty, heightProperty;
    private final ImageView imageView;
    private final Image faceUpImage, faceDownImage;
    private final CardRotateTransition flipCardAnimation;
    private final JumpingAnimation jumpingAnimation;

    @Getter
    private final SimpleBooleanProperty forceImageFaceUp, forceFlipCardAnimation;

    public Image getImage(){
        return imageView.getImage();
    }

    public void bindImageWidth(DoubleBinding binding){
        imageView.fitWidthProperty().unbind();
        imageView.fitWidthProperty().bind(binding);
    }
    public void bindImageHeight(DoubleBinding binding){
        imageView.fitHeightProperty().unbind();
        imageView.fitHeightProperty().bind(binding);
    }
    public void setImageTranslateX(double value) {
        imageView.setTranslateX(value);
    }
    public void changeActivationFlippingAnimation(boolean activate) {
        if(activate)
            flipCardAnimation.activate();
        else
            flipCardAnimation.deactivate();
    }
    public void changeActivationJumpingAnimation(boolean activate) {
        if(activate)
            jumpingAnimation.activate();
        else
            jumpingAnimation.deactivate();
    }

    private SimpleBooleanProperty getInHandObservable() {
        SimpleBooleanProperty ret = new SimpleBooleanProperty(card.owner.getBoard().getCardsOnHand().contains(card));
        card.owner.getBoard().getCardsOnHand().addListener((InvalidationListener) (o)->{
            ret.set(card.owner.getBoard().getCardsOnHand().contains(card));
        });
        return  ret;
    }

    private SimpleBooleanProperty myTurnObservable() {
        SimpleBooleanProperty ret = new SimpleBooleanProperty(false);
        Game game = GameController.getInstance().getGame();
        Runnable refresh = ()->{
            ret.set(game.getCurrentPlayer().equals(card.owner));
        };
        refresh.run();
        game.phaseProperty().addListener((InvalidationListener) (o) -> refresh.run());
        // todo add owner
        return ret;
    }

    private SimpleBooleanProperty currentPlayerCanSee() {
        SimpleBooleanProperty ret = new SimpleBooleanProperty(false);
        Game game = GameController.getInstance().getGame();
        Runnable refresh = ()->{
            CardAddress address = game.getCardAddress(card);
            if(game.getCurrentPlayer().equals(address.getOwner())){
                ret.set(!address.isInGraveYard() && !address.isInDeck());
            } else {
                ret.set(card.isFacedUp());
            }
        };
        refresh.run();
        game.phaseProperty().addListener((InvalidationListener) (o) -> refresh.run());
        // todo add owner
        return ret;
    }

    CardFrame(GameField gameRoot, Card card, DoubleBinding widthProperty, DoubleBinding heightProperty){
        super();

        this.card = card;
        this.faceDownImage = new Image(Utils.getAsset("Cards/hidden.png").toURI().toString());
        this.faceUpImage = Utils.getCardImage(card);
        this.imageView = new ImageView();

        getChildren().add(imageView);

        this.forceImageFaceUp = new SimpleBooleanProperty(false);
        this.forceFlipCardAnimation = new SimpleBooleanProperty(false);

        SimpleBooleanProperty inHandObservable = getInHandObservable();

        SimpleBooleanProperty currentPlayerCanSee = currentPlayerCanSee();
        DoubleBinding inHandCof = Bindings.when(inHandObservable).then(1.3).otherwise(1.0);
        this.widthProperty = widthProperty.multiply(inHandCof);
        this.heightProperty = heightProperty.multiply(inHandCof);

        imageView.imageProperty().bind(Bindings.when(forceImageFaceUp).then(faceUpImage).otherwise(faceDownImage));

        SimpleBooleanProperty flipCardActivation = new SimpleBooleanProperty(false);
        flipCardActivation.bind(hoverProperty().or(currentPlayerCanSee).or(this.forceFlipCardAnimation));

        flipCardAnimation = new CardRotateTransition(this, flipCardActivation, widthProperty);
        flipCardAnimation.start();

        SimpleBooleanProperty jumpingActivation = new SimpleBooleanProperty(false);
        jumpingActivation.bind(hoverProperty().and(inHandObservable).and(myTurnObservable()));
        jumpingAnimation = new JumpingAnimation(this, jumpingActivation);
        jumpingAnimation.start();

        bindImageHeight(this.heightProperty);
        bindImageWidth(this.widthProperty);
        minWidthProperty().bind(this.widthProperty);
        minHeightProperty().bind(this.heightProperty);

        if(card instanceof Monster)
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));

        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new DropShadow(28, Color.BLUE))
                        .otherwise((Effect) null)
        );
        setOnMouseClicked(e->{
            GuiReporter.getInstance().report(new ClickOnCardEvent(this));
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

    public DoubleBinding getCenterXProperty() {
        return layoutXProperty().add(widthProperty.divide(2));
    }
    public DoubleBinding getCenterYProperty() {
        return layoutYProperty().add(heightProperty.divide(2));
    }

    public void moveByBindingCoordinates(DoubleBinding x, DoubleBinding y){
        layoutXProperty().bind(x.add(widthProperty.divide(2).negate()));
        layoutYProperty().bind(y.add(heightProperty.divide(2).negate()));
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
