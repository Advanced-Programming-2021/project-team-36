package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.DeckMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Utils;
import YuGiOh.model.deck.Deck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DeckMenuView extends BaseMenuView {
    private static final int rowSize = 5;
    private static final double imageWidth = 70, imageHeight = imageWidth * 614.0 / 421.0;
    private static DeckMenuView instance;

    private Card selectedCard;

    @FXML
    private ListView<Deck> listView;
    @FXML
    private Button buyButton;
    @FXML
    private HBox buttonHBox;

    public DeckMenuView() {
        instance = this;
    }

    public static DeckMenuView getInstance() {
        if (instance == null)
            instance = new DeckMenuView();
        return instance;
    }

    public static void init(Stage primaryStage, User user) {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/DeckMenu.fxml"));
            DeckMenuView.getInstance().start(primaryStage, root, user);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root, User user) {
        this.stage = primaryStage;
        this.root = root;
        new DeckMenuController(user);
        scene = new Scene(root);
        run();
    }

    public void run() {
        renderInitialSettings();
        stage.setScene(scene);
        stage.show();
    }

    private void renderInitialSettings() {

    }

    private void selectCard(Card card) {
    }

    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
