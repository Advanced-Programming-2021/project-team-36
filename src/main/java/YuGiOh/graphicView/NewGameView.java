package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.graphicController.MainMenuController;
import YuGiOh.model.enums.AIMode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class NewGameView extends BaseMenuView {
    private static final int ARROW_SIZE = 30;
    private static final Color TRIANGLE_COLOR = Color.rgb(255, 200, 0);

    private static NewGameView instance;

    private int numberOfRounds = 3, gameMode;
    private Node leftArrow, rightArrow;
    @FXML
    private Label numberOfRoundsLabel;
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;
    @FXML
    private TextField secondPlayerUsernameTextField;

    public NewGameView() {
        instance = this;
    }

    public static NewGameView getInstance() {
        if (instance == null)
            instance = new NewGameView();
        return instance;
    }

    public static void init(Stage primaryStage, int gameMode) {
        try {
            VBox root = FXMLLoader.load(Main.class.getResource("/fxml/NewGameMenu.fxml"));
            NewGameView.getInstance().start(primaryStage, root, gameMode);
        } catch (IOException ignored) {
        }
    }

    private void start(Stage primaryStage, VBox root, int gameMode) {
        this.stage = primaryStage;
        this.root = root;
        this.gameMode = gameMode;
        scene = new Scene(root);
        run();
    }

    public void run() {
        renderInitialSettings();
        stage.setScene(scene);
        stage.show();
    }

    public void renderInitialSettings() {
        if (gameMode != 0)
            ((VBox)vBox.getParent()).getChildren().remove(vBox);
        numberOfRoundsLabel.setText(numberOfRounds + "");
        leftArrow = getTriangle("left", ARROW_SIZE, TRIANGLE_COLOR, mouseEvent -> {
            if (numberOfRounds == 3)
                numberOfRounds = 1;
            numberOfRoundsLabel.setText(numberOfRounds + "");
        });
        rightArrow = getTriangle("right", ARROW_SIZE, TRIANGLE_COLOR, mouseEvent -> {
            if (numberOfRounds == 1)
                numberOfRounds = 3;
            numberOfRoundsLabel.setText(numberOfRounds + "");
        });
        hBox.getChildren().add(0, leftArrow);
        hBox.getChildren().add(2, rightArrow);
    }
    @FXML
    private void startGame() {
        try {
            if (gameMode == 0) {
                String secondPlayerUsername = secondPlayerUsernameTextField.getText();
                MainMenuController.getInstance().startNewDuel(secondPlayerUsername, numberOfRounds);
                DuelMenuView.init(stage); // TODO: Is this okay?
            } else {
                MainMenuController.getInstance().startDuelWithAI(3, AIMode.NORMAL);
                DuelMenuView.init(stage); // TODO: Is this?
            }
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }
    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
