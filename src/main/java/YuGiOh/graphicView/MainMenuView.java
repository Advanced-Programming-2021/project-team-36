package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.DuelMenuController;
import YuGiOh.graphicController.MainMenuController;
import YuGiOh.model.Duel;
import YuGiOh.model.User;
import YuGiOh.model.enums.AIMode;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class MainMenuView extends BaseMenuView {
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg3.dds.png";
    private static MainMenuView instance;

    @FXML
    private ImageView backgroundImageView;

    public MainMenuView() {
        instance = this;
    }

    public static MainMenuView getInstance() {
        if (instance == null)
            instance = new MainMenuView();
        return instance;
    }

    public static void init(Stage primaryStage, User user) {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/MainMenu.fxml"));
            MainMenuView.getInstance().start(primaryStage, root, user);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    public void start(Stage primaryStage, Pane root, User user) {
        this.stage = primaryStage;
        this.root = root;
        new MainMenuController(user);
        scene.setRoot(root);
        try {
            backgroundImageView.setImage(new Image(new FileInputStream(backgroundImageAddress)));
            backgroundImageView.toBack();
        } catch (FileNotFoundException ignored) {
        }
        run();
    }

    public void run() {
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
    @FXML
    private void loadProfileMenu() {
        ProfileMenuView.init(stage, MainMenuController.getInstance().getUser());
    }
    @FXML
    private void loadScoreboardMenu() {
        ScoreboardMenuView.init(stage);
    }
    @FXML
    private void loadDeckMenu() {
        DeckMenuView.init(stage, MainMenuController.getInstance().getUser());
    }
    @FXML
    private void loadShopMenu() {
        ShopMenuView.init(stage, MainMenuController.getInstance().getUser());
    }
    @FXML
    private void startNewDuel() {
        NewGameView.init(stage, 0);
    }
    @FXML
    private void startNewDuelWithAI() {
        NewGameView.init(stage, 1);
    }

    public void logout() {
        new Alert(Alert.AlertType.INFORMATION, "user logged out successfully!").showAndWait();
        LoginMenuView.getInstance().run();
    }
}
