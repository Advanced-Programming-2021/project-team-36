package YuGiOh.archive.view.gui.component;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.menus.DuelMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;
import YuGiOh.archive.view.gui.*;
import YuGiOh.archive.view.gui.transition.CardRotateTransition;
import YuGiOh.archive.view.gui.transition.JumpingAnimation;
import YuGiOh.archive.view.gui.event.ClickOnCardEvent;
import YuGiOh.archive.view.gui.event.DropCardEvent;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.ImageCursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Getter;

public class CardFrame extends DraggablePane {
    @Getter
    private final BooleanProperty isSelected, flipCardActivation, jumpingCardActivation;
    @Getter
    private final Card card;

    private final ImageView imageView;
    private final Image faceUpImage, faceDownImage;
    private final CardRotateTransition flipCardAnimation;
    private final JumpingAnimation jumpingAnimation;
    private GameField gameField;

    @Getter
    private final SimpleBooleanProperty forceImageFaceUp, forceFlipCardAnimation;

    {
        forceImageFaceUp = new SimpleBooleanProperty(false);
        forceFlipCardAnimation = new SimpleBooleanProperty(false);
        isSelected = new SimpleBooleanProperty();
        this.imageView = new ImageView();
        flipCardActivation = new SimpleBooleanProperty(false);
        jumpingCardActivation = new SimpleBooleanProperty(false);
        flipCardAnimation = new CardRotateTransition(this, Bindings.when(flipCardActivation).then(true).otherwise(false));
        flipCardAnimation.start();
        jumpingAnimation = new JumpingAnimation(this, Bindings.when(jumpingCardActivation).then(true).otherwise(false));
        jumpingAnimation.start();
    }

    public void compressImage(double ratio) {
        imageView.setScaleX(ratio);
    }

    public Image getImage() {
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

    CardFrame(Card card) {
        super();

        this.card = card;
        this.faceDownImage = Utils.getImage("Cards/Unknown.jpg");
        this.faceUpImage = Utils.getCardImage(card);
        imageView.imageProperty().bind(Bindings.when(forceImageFaceUp).then(faceUpImage).otherwise(faceDownImage));
        imageView.fitHeightProperty().bind(heightProperty());
        imageView.fitWidthProperty().bind(widthProperty());
        effectProperty().bind(
                Bindings.when(hoverProperty().or(isSelected))
                        .then((Effect) new DropShadow(28, Color.BLUE))
                        .otherwise((Effect) null)
        );
        getChildren().add(imageView);
    }

    public void setGameField(GameField gameField) {
        this.gameField = gameField;

        BooleanBinding inHandBinding = ObservableBuilder.getInHandBinding(card);
        scaleXProperty().bind(Bindings.when(inHandBinding).then(1.3).otherwise(1.0));
        scaleYProperty().bind(Bindings.when(inHandBinding).then(1.3).otherwise(1.0));

        flipCardActivation.bind(currentPlayerCanSee().or(forceFlipCardAnimation).or(hoverProperty()));
        jumpingCardActivation.bind(hoverProperty().and(inHandBinding.and(ObservableBuilder.myTurnBinding(card))));

        if (card instanceof Monster) {
            rotateProperty().bind(Bindings.when(((Monster) card).isDefensive()).then(90).otherwise(0));
        }
        addEventListeners();

        ImageCursor imageCursor = new ImageCursor(Utils.getImage("Cursor/ax.png"));
        setCursor(imageCursor);
    }


    interface ValidRunner {
        void validRun(Card card, boolean validate) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard;
    }
    private void addButtonIfCan(ContextMenu contextMenu, String text, Card card, ValidRunner validRunner, boolean include) {
        Color color = Color.DARKGREEN;
        try {
            validRunner.validRun(card, true);
        } catch (Exception ignored) {
            color = Color.DARKRED;
            if(!include)
                return;
        }
        MenuItem item = new MenuItem("", new CustomButton(text, 15, ()->{}, color));
        item.setOnAction(e-> gameField.addRunnableToMainThreadForCard(card, ()->validRunner.validRun(card, false)));
        contextMenu.getItems().add(item);
    }

    private void addEventListeners() {
        setOnMouseClicked(e -> {
            GuiReporter.getInstance().report(new ClickOnCardEvent(this));
        });
        setOnContextMenuRequested(e -> {
            ContextMenu contextMenu = new ContextMenu();
            addButtonIfCan(contextMenu, "summon", card, (card, check)->DuelMenuController.getInstance().summonCard(card, check), true);
            addButtonIfCan(contextMenu, "special summon", card, (card, check)->DuelMenuController.getInstance().specialSummon(card, check), true);
            addButtonIfCan(contextMenu, "set", card, (card, check)->DuelMenuController.getInstance().setCard(card, check), true);
            addButtonIfCan(contextMenu, "change position to OO", card, (card, check)->DuelMenuController.getInstance().changeCardPosition(card, MonsterState.OFFENSIVE_OCCUPIED, check), false);
            addButtonIfCan(contextMenu, "change position to DO", card, (card, check)->DuelMenuController.getInstance().changeCardPosition(card, MonsterState.DEFENSIVE_OCCUPIED, check), false);
            addButtonIfCan(contextMenu, "change position to DH", card, (card, check)->DuelMenuController.getInstance().changeCardPosition(card, MonsterState.DEFENSIVE_HIDDEN, check), false);
            addButtonIfCan(contextMenu, "flip summon", card, (card, check)->DuelMenuController.getInstance().flipSummon(card, check), true);
            addButtonIfCan(contextMenu, "activate effect", card, (card, check)->DuelMenuController.getInstance().activateEffect(card, check), true);
            addButtonIfCan(contextMenu, "direct attack", card, (card, check)->DuelMenuController.getInstance().directAttack(card, check), true);
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

    public void moveByBindingCoordinates(DoubleBinding x, DoubleBinding y) {
        layoutXProperty().bind(x.add(widthProperty().divide(2).negate()));
        layoutYProperty().bind(y.add(heightProperty().divide(2).negate()));
        setTranslateX(0);
        setTranslateY(0);
    }

    public void select() {
        isSelected.set(true);
    }

    public void deselect() {
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
