package YuGiOh.view.gui.component;

import YuGiOh.controller.GameController;
import YuGiOh.graphicController.DuelMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.transition.CardRotateTransition;
import YuGiOh.view.gui.transition.JumpingAnimation;
import YuGiOh.view.gui.event.ClickOnCardEvent;
import YuGiOh.view.gui.event.DropCardEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;

public class CardFrame extends DraggablePane {
    private final BooleanProperty isSelected = new SimpleBooleanProperty();
    @Getter
    private final Card card;

    private final ImageView imageView;
    private final Image faceUpImage, faceDownImage;
    private final CardRotateTransition flipCardAnimation;
    private final JumpingAnimation jumpingAnimation;
    private final GameField gameRoot;

    @Getter
    private final SimpleBooleanProperty forceImageFaceUp, forceFlipCardAnimation;

    {
        forceImageFaceUp = new SimpleBooleanProperty(false);
        forceFlipCardAnimation = new SimpleBooleanProperty(false);
        this.imageView = new ImageView();
    }
    public void compressImage(double ratio) {
        imageView.setScaleX(ratio);
    }

    public Image getImage(){
        return imageView.getImage();
    }

    public boolean isFacedUp() {
        return imageView.getImage().equals(faceUpImage);
    }

    private BooleanBinding currentPlayerCanSee() {
        return Bindings.when(ObservableBuilder.myTurnBinding(card))
                .then(Bindings.and(ObservableBuilder.inGraveyardBinding(card).not(), ObservableBuilder.inDeckBinding(card).not()))
                .otherwise(card.facedUpProperty());
    }

    CardFrame(GameField gameRoot, Card card){
        super();

        this.gameRoot = gameRoot;
        this.card = card;
        this.faceDownImage = Utils.getImage("Cards/Unknown.jpg");
        this.faceUpImage = Utils.getCardImage(card);
        imageView.imageProperty().bind(Bindings.when(forceImageFaceUp).then(faceUpImage).otherwise(faceDownImage));

        getChildren().add(imageView);

        BooleanBinding inHandBinding = ObservableBuilder.getInHandBinding(card);
        scaleXProperty().bind(Bindings.when(inHandBinding).then(1.3).otherwise(1.0));
        scaleYProperty().bind(Bindings.when(inHandBinding).then(1.3).otherwise(1.0));

        flipCardAnimation = new CardRotateTransition(this, currentPlayerCanSee().or(forceFlipCardAnimation).or(hoverProperty()));
        flipCardAnimation.start();

        jumpingAnimation = new JumpingAnimation(this, hoverProperty().and(inHandBinding.and(ObservableBuilder.myTurnBinding(card))));
        jumpingAnimation.start();

        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());

        if(card instanceof Monster) {
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));
        }

        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new DropShadow(28, Color.BLUE))
                        .otherwise((Effect) null)
        );
        addEventListeners();
    }

    private void addEventListeners() {
        setOnMouseClicked(e->{
            GuiReporter.getInstance().report(new ClickOnCardEvent(this));


            // todo remove this in production. just debug info
            System.out.println("CARD: " + card);
            System.out.println("CARD FRAME: " + this);
            System.out.println("card faced up: " + card.facedUpProperty().get());
            System.out.println("image faced up: " + isFacedUp());
            System.out.println("actual zone: " + GameController.getInstance().getGame().getCardZoneType(card));
            System.out.println("force image face up : " + forceImageFaceUp.get());
            System.out.println("force flip card animation: " + forceFlipCardAnimation.get());
            System.out.println(getWidth() + " " + getHeight() + " " + imageView.getFitWidth() + " " + imageView.getFitHeight());
            if(card instanceof Monster)
                System.out.println("monster state: " + ((Monster) card).getMonsterState());
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
        viewOrderProperty().bind(Bindings.when(hoverProperty()).then(-2).otherwise(-1));
    }

    public DoubleBinding getCenterXProperty() {
        return layoutXProperty().add(widthProperty().divide(2));
    }
    public DoubleBinding getCenterYProperty() {
        return layoutYProperty().add(heightProperty().divide(2));
    }

    public void moveByBindingCoordinates(DoubleBinding x, DoubleBinding y){
        layoutXProperty().bind(x.add(widthProperty().divide(2).negate()));
        layoutYProperty().bind(y.add(heightProperty().divide(2).negate()));
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
