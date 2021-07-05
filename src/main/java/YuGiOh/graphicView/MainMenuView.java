package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.LoginMenuController;
import YuGiOh.graphicController.MainMenuController;
import YuGiOh.graphicController.ProfileMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuView extends BaseMenuView {
    private static MainMenuView instance;

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
        }
        new MainMenuController(user);
    }

    public void start(Stage primaryStage, Pane root, User user) {
        this.stage = primaryStage;
        this.root = root;
        new MainMenuController(user);
        run();
    }

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
    }

    private void renderScene() {
        if (scene == null)
            scene = new Scene(root);
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
        // TODO: Kasra
    }

    @FXML
    private void loadShopMenu() {
        // TODO: Kasra
    }

    @FXML
    private void startNewDuel() {
        //TODO: Shayan
    }

    @FXML
    private void startNewDuelWithAI() {
        // TODO: Also Shayan
    }

    @FXML
    private void startAIVersusAIDuel() {
        // TODO : Yet again, Shayan
    }

    public void logout() {
        new Alert(Alert.AlertType.INFORMATION, "user logged out successfully!").showAndWait();
        LoginMenuView.getInstance().run();
    }
}
