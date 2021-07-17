package YuGiOh.view.game.component;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.view.game.*;
import YuGiOh.view.game.transition.CardRotateTransition;
import YuGiOh.view.game.transition.JumpingAnimation;
import YuGiOh.view.game.event.ClickOnCardEvent;
import YuGiOh.view.game.event.DropCardEvent;
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


    private void addButtonIfCan(ContextMenu contextMenu, String text, Card card, Action action, boolean include) {
        Color color = Color.DARKGREEN;
        if(!action.isValid()) {
            color = Color.DARKRED;
            if(!include)
                return;
        }
        MenuItem item = new MenuItem("", new CustomButton(text, 15, ()->{}, color));
        PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(card.getOwner());
        item.setOnAction(e-> GameController.getInstance().addRunnableToMainThreadForCard(card, ()-> controller.startChain(action)));
        contextMenu.getItems().add(item);
    }

    private void addEventListeners() {
        setOnMouseClicked(e -> {
            GuiReporter.getInstance().report(new ClickOnCardEvent(this));
        });
        setOnContextMenuRequested(e -> {
            ContextMenu contextMenu = new ContextMenu();
            PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(card.getOwner());
            if (card instanceof Monster) {
                addButtonIfCan(contextMenu, "summon", card, controller.normalSummon((Monster) card), true);
                addButtonIfCan(contextMenu, "special summon", card, controller.specialSummon((Monster) card), true);
                addButtonIfCan(contextMenu, "set", card, controller.setMonster((Monster) card), true);
                addButtonIfCan(contextMenu, "change position to OO", card, controller.changeMonsterPosition((Monster) card, MonsterState.OFFENSIVE_OCCUPIED), false);
                addButtonIfCan(contextMenu, "change position to OO", card, controller.changeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_OCCUPIED), false);
                addButtonIfCan(contextMenu, "change position to OO", card, controller.changeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_HIDDEN), false);
                addButtonIfCan(contextMenu, "flip summon", card, controller.flipSummon((Monster) card), true);
                addButtonIfCan(contextMenu, "activate effect", card, controller.activateMonsterEffect((Monster) card), true);
                addButtonIfCan(contextMenu, "direct attack", card, controller.directAttack((Monster) card), true);
            } else if (card instanceof Magic) {
                addButtonIfCan(contextMenu, "set", card, controller.setMagic((Magic) card), true);
            }
            if (card instanceof Spell) {
                addButtonIfCan(contextMenu, "activate effect", card, controller.activateSpellEffect((Spell) card), true);
            }
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
