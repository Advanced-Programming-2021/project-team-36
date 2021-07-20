package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.LoginMenuApi;
import YuGiOh.api.ShopMenuApi;
import YuGiOh.model.exception.ModelException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.network.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ShopMenuView extends BaseMenuView {
    private static final int rowSize = 5;
    private static final double imageWidth = 70, imageHeight = imageWidth * 614.0 / 421.0;
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg2.dds.png";
    private static ShopMenuView instance;

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
    private Button buyButton;
    @FXML
    private VBox buttonVBox, selectedCardVBox;
    @FXML
    private Label cardNameLabel, cardPriceLabel, cardBalanceLabel, creditBalanceLabel;

    private ShopMenuApi api;

    public ShopMenuView() {
        instance = this;
    }

    public static ShopMenuView getInstance() {
        if (instance == null)
            instance = new ShopMenuView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/ShopMenu.fxml"));
            ShopMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        try {
            this.api = new ShopMenuApi(ClientConnection.getOrCreateInstance());
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
        stage.show();
        stage.setResizable(false);
        relocateNodeFromCenter(scrollPane, scene.getWidth() * 0.5, 0);
        scrollPane.setLayoutY(scene.getHeight() * 0.05);
        relocateNodeFromCenter(buttonVBox, scene.getWidth() * 0.5, scene.getHeight() * 0.90);
    }

    private void renderInitialSettings() {
        Card[] cards = Utils.getAllCards();
        for (int i = 0; i < cards.length; i ++) {
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
        api.getUserFromServer().thenAccept(user -> {
            if (selectedImageView != null)
                selectedImageView.setTranslateY(-4);
            imageView.setTranslateY(4);
            cardImageView.setImage(Utils.getCardImageView(card));
            cardNameLabel.setText(card.getName());
            cardPriceLabel.setText("Price:  " + card.getPrice());
            cardBalanceLabel.setText("Balance:  " + user.getCardFrequency(card));
            creditBalanceLabel.setText("Credit:  " + user.getBalance());
            if (selectedCard == null) {
                selectedCardVBox.setOpacity(1);
                relocateNodeFromCenter(selectedCardVBox, 0, scene.getHeight() * 0.325);
                relocateFromRight(selectedCardVBox, 20);
                rescale(scrollPane, 0.9, 0.9);
                relocateNodeFromCenter(scrollPane, 0, scene.getHeight() * 0.45);
                relocateFromLeft(scrollPane, 20);
            }
            buyButton.setOpacity(1);
            selectedCard = card;
            selectedImageView = imageView;
            disableBuyButton(user.getBalance() < selectedCard.getPrice());

        });
    }

    private void disableBuyButton(boolean disable) {
        buyButton.setDisable(disable);
        if (disable)
            buyButton.getStyleClass().add("disabled-button");
        else if (buyButton.getStyleClass().contains("disabled-button"))
            buyButton.getStyleClass().remove("disabled-button");
    }

    @FXML
    private void buyCard() {
        api.buy(selectedCard).thenCompose(res -> api.getUserFromServer())
                .whenComplete((currentUser, ex)->{
                    if(ex == null) {
                        cardBalanceLabel.setText("Balance:  " + currentUser.getCardFrequency(selectedCard));
                        creditBalanceLabel.setText("Credit:  " + currentUser.getBalance());
                        disableBuyButton(currentUser.getBalance() < selectedCard.getPrice());
                        new Alert(Alert.AlertType.INFORMATION, "You successfully bought " + selectedCard.getName() + "!").showAndWait();
                    } else {
                        new Alert(Alert.AlertType.ERROR, ex.getCause().getMessage()).showAndWait();
                    }
                });
    }

    @FXML
    private void importCard() {
    }
    @FXML
    private void exportCard() {
    }
    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
