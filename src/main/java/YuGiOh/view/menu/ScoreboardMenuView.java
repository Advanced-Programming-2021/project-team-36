package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.controller.menu.*;
import YuGiOh.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardMenuView extends BaseMenuView {
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg4.dds.png";
    private static ScoreboardMenuView instance;

    @FXML
    private ImageView backgroundImageView;
    @FXML
    private VBox mainBox;

    public ScoreboardMenuView() {
        instance = this;
    }

    public static ScoreboardMenuView getInstance() {
        if (instance == null)
            instance = new ScoreboardMenuView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/ScoreboardMenu.fxml"));
            ScoreboardMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
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
        renderScene();
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        relocateNodeFromCenter(mainBox.getParent(), root.getWidth() / 2, root.getHeight() * 0.4);
    }

    private void renderScene() {
        ArrayList<User> users = User.retrieveUsersBasedOnScore();
        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (i > 0 && users.get(i - 1).getScore() > user.getScore())
                rank = i + 1;
            Label label = new Label(rank + ".  " + user.getNickname() + ":  " + user.getScore());
            if (user == MainMenuController.getInstance().getUser())
                label.getStyleClass().add("highlighted-user");
            else
                label.getStyleClass().add("user");
            label.setText("  " + label.getText() + "  ");
            mainBox.getChildren().add(i, label);
        }
    }

    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
