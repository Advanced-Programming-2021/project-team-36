package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.HalfTimeMenuApi;
import YuGiOh.model.deck.BaseDeck;
import YuGiOh.model.deck.Deck;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.network.ClientConnection;
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
    private Card selectedMainCard, selectedSideCard;
    private ImageView selectedImageView;
    private Deck deck;

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

    private HalfTimeMenuApi api;

    public HalfTimeView() {
        instance = this;
    }

    public static HalfTimeView getInstance() {
        if (instance == null)
            instance = new HalfTimeView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/HalfTimeView.fxml"));
            HalfTimeView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        try {
            this.api = new HalfTimeMenuApi(ClientConnection.getOrCreateInstance());
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "check your connection to server and retry!").showAndWait();
            LoginMenuView.init(stage);
            return;
        }
        scene.setRoot(root);
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
        api.getDeck().thenAccept(deck->{
            this.deck = deck;
            ((VBox) switchButton.getParent()).getChildren().remove(switchButton);
            deckLabel.setText(deck.getName());
            renderDeck(deck.getMainDeck(), mainGridPane, mainScrollPane);
            renderDeck(deck.getSideDeck(), sideGridPane, sideScrollPane);
            mainGridPane.setHgap(HGAP);
            sideGridPane.setHgap(HGAP);
        });
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
            if (deck == this.deck.getMainDeck()) { // todo if this is ok over network
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
        api.ready().whenComplete((res, ex)->{
            if(ex != null) {
                if (ex.getCause() instanceof LogicException) {
                    new Alert(Alert.AlertType.ERROR, ex.getCause().getMessage()).showAndWait();
                }
            } else {
                // todo here also check if duel has started
                DuelMenuView.init(stage);
            }
        });
    }
    @FXML
    private void switchCard() {
        if (selectedMainCard != null) {
            showErrorAsync(api.removeCardFromDeck(selectedMainCard));
            mainGridPane.getChildren().remove(selectedImageView);
            sideGridPane.getChildren().add(selectedImageView);
        } else {
            showErrorAsync(api.addCardToDeck(selectedSideCard));
            sideGridPane.getChildren().remove(selectedImageView);
            mainGridPane.getChildren().add(selectedImageView);
        }
        // todo exit if error happened
        selectedSideCard = selectedMainCard = null;
        selectedImageView = null;
        mainHBox.getChildren().remove(switchButton);
        sideHBox.getChildren().remove(switchButton);
        new Alert(Alert.AlertType.INFORMATION, "Card was switched successfully!").showAndWait();
    }
}
