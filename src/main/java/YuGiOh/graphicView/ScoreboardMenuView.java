package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ScoreboardMenuView extends BaseMenuView {
    private static ScoreboardMenuView instance;

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
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/ScoreboardMenu.fxml"));
            ScoreboardMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        this.stage = primaryStage;
        this.root = root;
        run();
    }

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
        relocateNodeFromCenter(mainBox, root.getWidth() / 2, root.getHeight() * 0.4);
    }

    private void renderScene() {
        if (scene == null) {
            scene = new Scene(root);
            ArrayList<User> users = User.retrieveUsersBasedOnScore();
            int rank = 1;
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                if (i > 0 && users.get(i - 1).getScore() > user.getScore())
                    rank = i + 1;
                Label label = new Label(rank + ".  " + user.getNickname() + ":  " + user.getScore());
                label.getStyleClass().add("textCss");
                mainBox.getChildren().add(i + 1, label);
            }
        }
    }

    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
