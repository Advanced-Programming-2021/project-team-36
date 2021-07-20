package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.BaseMenuApi;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.network.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ShowAllCardsView extends BaseMenuView {
    private static final int rowSize = 5;
    private static final double imageWidth = 90, imageHeight = imageWidth * 614.0 / 421.0;
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg2.dds.png";

    private static ShowAllCardsView instance;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox vBox;

    private BaseMenuApi api;

    public ShowAllCardsView() {
        instance = this;
    }

    public static ShowAllCardsView getInstance() {
        if (instance == null)
            instance = new ShowAllCardsView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/ShowAllCards.fxml"));
            ShowAllCardsView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        try {
            this.api = new BaseMenuApi(ClientConnection.getOrCreateInstance());
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
        relocateNodeFromCenter(vBox, scene.getWidth() * 0.5, scene.getHeight() * 0.5);
    }

    private void renderInitialSettings() {
        api.getUserFromServer().thenAccept(user->{
            int i = 0;
            for (Card card : user.getCards()) {
                ImageView imageView = new ImageView(Utils.getCardImageView(card));
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(imageWidth);
                imageView.setFitHeight(imageHeight);
                gridPane.add(imageView, i % rowSize, i / rowSize);
                i ++;
            }
        });
    }

    @FXML
    private void exit() {
        DeckMenuView.getInstance().run();
    }
}
