package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.controller.menu.FactoryMenuController;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CardFactoryMenuView extends BaseMenuView {
    private static final int rowSize = 5;
    private static final double imageWidth = 70, imageHeight = imageWidth * 614.0 / 421.0;
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg2.dds.png";
    private static CardFactoryMenuView instance;

    private User user;
    private Card selectedCard;
    private ImageView selectedImageView;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView cardImageView;
    @FXML
    private Button selectButton;
    @FXML
    private VBox buttonVBox, selectedCardVBox;
    @FXML
    private Label cardNameLabel, creditBalanceLabel;

    public CardFactoryMenuView() {
        instance = this;
    }

    public static CardFactoryMenuView getInstance() {
        if (instance == null)
            instance = new CardFactoryMenuView();
        return instance;
    }

    public static void init(Stage primaryStage, User user) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/CardFactoryMenuView.fxml"));
            CardFactoryMenuView.getInstance().start(primaryStage, root, user);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, User user) {
        this.stage = primaryStage;
        this.root = root;
        this.user = user;
        scene.setRoot(root);
        new FactoryMenuController(user);
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
        stage.show();
        stage.setResizable(false);
        selectedCard = null;
        selectedImageView = null;
        selectedCardVBox.setOpacity(0);
        selectButton.setOpacity(0);
        relocateNodeFromCenter(buttonVBox, scene.getWidth() * 0.5, scene.getHeight() * 0.90);
        relocateNodeFromCenter(scrollPane, scene.getWidth() * 0.5, 0);
        scrollPane.setLayoutY(scene.getHeight() * 0.05);
        scrollPane.setLayoutX(300 - 352/2);
    }

    private void renderInitialSettings() {
        Card[] cards = Utils.getAllMonsters();
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            ImageView imageView = new ImageView(Utils.getCardImageView(card));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(imageWidth);
            imageView.setFitHeight(imageHeight);
            gridPane.add(imageView, i % rowSize, i / rowSize);
            imageView.setOnMouseClicked(mouseEvent -> selectCard(card, imageView));
        }
        scrollPane.maxWidthProperty().bind(gridPane.widthProperty().multiply(1.05));
    }

    private void selectCard(Card card, ImageView imageView) {
        cardImageView.setImage(Utils.getCardImageView(card));
        cardNameLabel.setText(card.getName());
        creditBalanceLabel.setText("Credit:  " + user.getBalance());
        if (selectedCard == null) {
            selectedCardVBox.setOpacity(1);
            relocateNodeFromCenter(selectedCardVBox, 0, scene.getHeight() * 0.325);
            relocateFromRight(selectedCardVBox, 20);
            rescale(scrollPane, 0.9, 0.9);
            relocateNodeFromCenter(scrollPane, 0, scene.getHeight() * 0.45);
            relocateFromLeft(scrollPane, 20);
        }
        selectButton.setOpacity(1);
        selectedCard = card;
        selectedImageView = imageView;
    }

    @FXML
    private void selectSpecifiedCard() {
        try {
            FactoryMenuController.getInstance().selectBaseMonster((Monster) selectedCard);
            FactoryView.init(stage, selectedCard);
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
