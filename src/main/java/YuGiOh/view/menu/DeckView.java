package YuGiOh.view.menu;

import YuGiOh.MainApplication;
import YuGiOh.controller.menu.DeckMenuController;
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

public class DeckView extends BaseMenuView {
    private static final double imageWidth = 90, imageHeight = imageWidth * 614.0 / 421.0, HGAP = 1.8;
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg2.dds.png";

    private static DeckView instance;
    private Deck deck;
    private Card selectedMainCard, selectedSideCard;
    private ImageView selectedImageView;

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
    private Button removeButton;

    public DeckView() {
        instance = this;
    }

    public static DeckView getInstance() {
        if (instance == null)
            instance = new DeckView();
        return instance;
    }

    public static void init(Stage primaryStage, Deck deck) {
        try {
            Pane root = FXMLLoader.load(MainApplication.class.getResource("/fxml/DeckView.fxml"));
            DeckView.getInstance().start(primaryStage, root, deck);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, Deck deck) {
        this.stage = primaryStage;
        this.root = root;
        this.deck = deck;
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
        ((VBox) removeButton.getParent()).getChildren().remove(removeButton);
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
            sideHBox.getChildren().remove(removeButton);
            mainHBox.getChildren().remove(removeButton);
            if (deck == this.deck.getMainDeck()) {
                selectedMainCard = card;
                mainHBox.getChildren().add(removeButton);
            } else {
                selectedSideCard = card;
                sideHBox.getChildren().add(removeButton);
            }
        });
    }

    private Card selectCard() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.getEditor().setPromptText("card name");
        textInputDialog.setTitle("Deck Creation");
        textInputDialog.setHeaderText("Enter the desired card name below:");
        while (textInputDialog.getEditor().getText() == null ||
                textInputDialog.getEditor().getText().equals(""))
            textInputDialog.showAndWait();
        return Utils.getCard(textInputDialog.getEditor().getText());
    }

    private void addCardToDeck(boolean side) {
        Card card = selectCard();
        try {
            DeckMenuController.getInstance().addCardToDeck(card, deck, false, side);
            new Alert(Alert.AlertType.INFORMATION, "Card added to deck successfully!").showAndWait();
            if (!side)
                addCardToGrid(mainGridPane, deck.getMainDeck(), card, mainGridPane.getColumnCount());
            else
                addCardToGrid(sideGridPane, deck.getSideDeck(), card, sideGridPane.getColumnCount());
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }
    @FXML
    private void addCardToMainDeck() {
        addCardToDeck(false);
    }
    @FXML
    private void addCardToSideDeck() {
        addCardToDeck(true);
    }
    @FXML
    private void removeCard() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this card from the deck?",
                ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (!alert.getResult().getText().equalsIgnoreCase("Yes"))
            return;
        if (selectedMainCard != null) {
            deck.getMainDeck().removeCard(selectedMainCard);
            mainGridPane.getChildren().remove(selectedImageView);
        } else {
            deck.getSideDeck().removeCard(selectedSideCard);
            sideGridPane.getChildren().remove(selectedImageView);
        }
        selectedSideCard = selectedMainCard = null;
        selectedImageView = null;
        mainHBox.getChildren().remove(removeButton);
        sideHBox.getChildren().remove(removeButton);
        new Alert(Alert.AlertType.INFORMATION, "Card removed from deck successfully!").showAndWait();

    }
    @FXML
    private void exit() {
        DeckMenuView.getInstance().run();
    }
}
