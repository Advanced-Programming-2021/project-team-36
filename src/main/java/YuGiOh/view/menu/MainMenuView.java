package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.LoginMenuApi;
import YuGiOh.controller.MediaPlayerController;
import YuGiOh.model.User;
import YuGiOh.network.ClientConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import YuGiOh.controller.menu.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/MainMenu.fxml"));
            MainMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
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
        MediaPlayerController.getInstance().playThemeMedia();
    }

    @FXML
    private void loadProfileMenu() {
        ProfileMenuView.init(stage);
    }

    @FXML
    private void loadScoreboardMenu() {
        ScoreboardMenuView.init(stage);
    }

    @FXML
    private void loadDeckMenu() {
        DeckMenuView.init(stage);
    }

    @FXML
    private void loadShopMenu() {
        ShopMenuView.init(stage);
    }

    @FXML
    private void startNewDuel() {
        StartNewDuelView.init(stage, 0);
    }

    @FXML
    private void startNewDuelWithAI() {
        StartNewDuelView.init(stage, 1);
    }

    @FXML
    private void loadCardFactoryMenu() {
        CardFactoryMenuView.init(stage);
    }

    public void logout() {
        new Alert(Alert.AlertType.INFORMATION, "user logged out successfully!").showAndWait();
        ClientConnection.disconnectAll();
        LoginMenuView.getInstance().run();
    }
}
