package YuGiOh.view;

import YuGiOh.Main;
import YuGiOh.controller.menus.*;
import YuGiOh.model.Animation;
import YuGiOh.model.User;
import YuGiOh.model.enums.AIMode;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class NewGameView extends BaseMenuView {
    private static final int ARROW_SIZE = 30;
    private static final Color TRIANGLE_COLOR = Color.rgb(255, 200, 0);
    private static final String backgroundImageAddress = "assets/Backgrounds/GUI_T_TowerBg1.dds.png";
    private static final String coinAssetURL = "assets/Coin/";

    private static NewGameView instance;

    private int numberOfRounds = 3, gameMode;
    private Animation animation;
    private ArrayList<String> imageURLs = new ArrayList<>();
    private boolean winner;

    private Node leftArrow, rightArrow;
    @FXML
    private ImageView backgroundImageView, animatedCoin;
    @FXML
    private Label numberOfRoundsLabel;
    @FXML
    private VBox vBox, coinTossVBox;
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
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/NewGameMenu.fxml"));
            NewGameView.getInstance().start(primaryStage, root, gameMode);
        } catch (IOException ignored) {
        }
    }

    private void start(Stage primaryStage, Pane root, int gameMode) {
        this.stage = primaryStage;
        this.root = root;
        this.gameMode = gameMode;
        Random random = new Random();
        this.winner = random.nextInt(100) % 2 == 1;
        scene = new Scene(root);
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
        stage.setResizable(true);
        stage.show();
    }

    public void renderInitialSettings() {
        root.getChildren().remove(coinTossVBox);
        if (gameMode != 0)
            ((VBox) vBox.getParent()).getChildren().remove(vBox);
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
        for (int i = 21; i <= 26; i++)
            imageURLs.add(coinAssetURL + "Gold_" + i + ".png");
        for (int i = 17; i <= 20; i++)
            imageURLs.add(coinAssetURL + "Gold_" + i + ".png");
        for (int i = 11; i <= 16; i++)
            imageURLs.add(coinAssetURL + "Gold_" + i + ".png");
        for (int i = 27; i <= 30; i++)
            imageURLs.add(coinAssetURL + "Gold_" + i + ".png");
        animation = new Animation(imageURLs.toArray(new String[imageURLs.size()]));
        animatedCoin.setImage(animation.getImage());
    }

    private void coinToss() {
        root.getChildren().remove(1);
        root.getChildren().add(coinTossVBox);
        coinTossVBox.toFront();


        int iterations = 10 * imageURLs.size();
        if (!winner)
            iterations += 10;

        int finalIterations = iterations + 1;
        AnimationTimer coinAnimator = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                animatedCoin.setImage(animation.getImage());
                if (animation.getCurrentImageId() > finalIterations) {
                    this.stop();
                    try {
                        Thread.sleep(1000);
                        MediaPlayerController.getInstance().stopThemeMedia();
                        DuelMenuView.init(stage);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        };
        coinAnimator.start();
    }

    @FXML
    private void startGame() {
        try {
            if (gameMode == 0) {
                String secondPlayerUsername = secondPlayerUsernameTextField.getText();
                MainMenuController.getInstance().startNewDuel(winner, secondPlayerUsername, numberOfRounds);
            } else
                MainMenuController.getInstance().startDuelWithAI(winner, 3, AIMode.NORMAL);
            coinToss();
        } catch (Exception exception) {
            new Alert(Alert.AlertType.ERROR, exception.getMessage()).showAndWait();
        }
    }

    @FXML
    private void exit() {
        MainMenuView.getInstance().run();
    }
}
