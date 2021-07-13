package YuGiOh.view.menu;

import YuGiOh.MainApplication;
import YuGiOh.controller.GameController;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.exception.eventException.PlayerReadyExceptionEvent;
import YuGiOh.controller.menu.HalfTimeMenuController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.model.deck.BaseDeck;
import YuGiOh.model.deck.Deck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HalfTimeView extends BaseMenuView {
    private static final double imageWidth = 90, imageHeight = imageWidth * 614.0 / 421.0, HGAP = 1.8;
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg2.dds.png";

    private static HalfTimeView instance;
    private Deck deck;
    private Card selectedMainCard, selectedSideCard;
    private ImageView selectedImageView;
    private PlayerController playerController;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ScrollPane mainScrollPane, sideScrollPane;
    @FXML
    private GridPane mainGridPane, sideGridPane;
    @FXML
    private Label deckLabel;
    @FXML
    private HBox mainHBox, sideHBox;
    @FXML
    private Button switchButton;

    public HalfTimeView() {
        instance = this;
    }

    public static HalfTimeView getInstance() {
        if (instance == null)
            instance = new HalfTimeView();
        return instance;
    }

    public static void init(Stage primaryStage, PlayerController playerController) {
        try {
            Pane root = FXMLLoader.load(MainApplication.class.getResource("/fxml/HalfTimeView.fxml"));
            HalfTimeView.getInstance().start(primaryStage, root, playerController);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, PlayerController playerController) {
        this.stage = primaryStage;
        this.root = root;
        this.playerController = playerController;
        this.deck = playerController.getPlayer().getDeck();
        scene.setRoot(root);
        new HalfTimeMenuController(playerController);
        try {
            backgroundImageView.setImage(new Image(new FileInputStream(backgroundImageAddress)));
            backgroundImageView.toBack();
        } catch (FileNotFoundException ignored) {
        }
        run();
    }

    public void run() {
        renderInitialSettings();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void renderInitialSettings() {
        ((VBox) switchButton.getParent()).getChildren().remove(switchButton);
        deckLabel.setText(deck.getName());
        renderDeck(deck.getMainDeck(), mainGridPane, mainScrollPane);
        renderDeck(deck.getSideDeck(), sideGridPane, sideScrollPane);
        mainGridPane.setHgap(HGAP);
        sideGridPane.setHgap(HGAP);
    }

    private void renderDeck(BaseDeck deck, GridPane gridPane, ScrollPane scrollPane) {
        int i = 0;
        gridPane.getChildren().clear();
        for (Card card : deck.getCards())
            addCardToGrid(gridPane, deck, card, i++);
        scrollPane.setPrefHeight(imageHeight * 1.13);
    }

    private void addCardToGrid(GridPane gridPane, BaseDeck deck, Card card, int i) {
        ImageView imageView = new ImageView(Utils.getCardImageView(card));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        gridPane.add(imageView, i, 0);
        imageView.setOnMouseClicked(mouseEvent -> {
            if (selectedImageView != null) {
                selectedImageView.setEffect(null);
                selectedImageView.setTranslateY(-4);
            }
            imageView.setTranslateY(4);
            imageView.setEffect(new DropShadow(50, Color.rgb(5, 109, 225)));
            selectedSideCard = selectedMainCard = null;
            selectedImageView = imageView;
            sideHBox.getChildren().remove(switchButton);
            mainHBox.getChildren().remove(switchButton);
            if (deck == this.deck.getMainDeck()) {
                selectedMainCard = card;
                mainHBox.getChildren().add(switchButton);
            } else {
                selectedSideCard = card;
                sideHBox.getChildren().add(switchButton);
            }
        });
    }
    @FXML
    private void ready() {
        try {
            HalfTimeMenuController.getInstance().ready();
        } catch (PlayerReadyExceptionEvent playerReadyExceptionEvent) {
        } catch (LogicException exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
        if (GameController.getInstance().getCurrentPlayerController().equals(playerController) &&
                GameController.getInstance().getOpponentPlayerController().getPlayer() instanceof HumanPlayer)
            HalfTimeView.init(stage, GameController.getInstance().getOpponentPlayerController());
        else
            DuelMenuView.init(stage);
    }
    @FXML
    private void switchCard() {
        try {
            if (selectedMainCard != null) {
                HalfTimeMenuController.getInstance().removeCardFromDeck(selectedMainCard);
                mainGridPane.getChildren().remove(selectedImageView);
                sideGridPane.getChildren().add(selectedImageView);
            } else {
                HalfTimeMenuController.getInstance().addCardToDeck(selectedSideCard);
                sideGridPane.getChildren().remove(selectedImageView);
                mainGridPane.getChildren().add(selectedImageView);
            }
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
        selectedSideCard = selectedMainCard = null;
        selectedImageView = null;
        mainHBox.getChildren().remove(switchButton);
        sideHBox.getChildren().remove(switchButton);
        new Alert(Alert.AlertType.INFORMATION, "Card was switched successfully!").showAndWait();
    }
}
