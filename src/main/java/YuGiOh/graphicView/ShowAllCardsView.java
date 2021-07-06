package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.DeckMenuController;
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

public class ShowAllCardsView extends BaseMenuView {
    private static final int rowSize = 5;
    private static final double imageWidth = 90, imageHeight = imageWidth * 614.0 / 421.0;

    private static ShowAllCardsView instance;

    @FXML
    private GridPane gridPane;
    @FXML
    private VBox vBox;

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
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/ShowAllCards.fxml"));
            ShowAllCardsView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        scene = new Scene(root);
        run();
    }

    public void run() {
        renderInitialSettings();
        stage.setScene(scene);
        stage.show();
        relocateNodeFromCenter(vBox, scene.getWidth() * 0.5, scene.getHeight() * 0.5);
    }

    private void renderInitialSettings() {
        int i = 0;
        for (Card card : DeckMenuController.getInstance().getUser().getCards()) {
            ImageView imageView = new ImageView(Utils.getCardImageView(card));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(imageWidth);
            imageView.setFitHeight(imageHeight);
            gridPane.add(imageView, i % rowSize, i / rowSize);
            i ++;
        }
    }

    @FXML
    private void exit() {
        DeckMenuView.getInstance().run();
    }
}
