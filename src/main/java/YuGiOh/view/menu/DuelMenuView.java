package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.api.DuelMenuApi;
import YuGiOh.api.LoginMenuApi;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.action.MonsterAttackAction;
import YuGiOh.model.card.action.NextPhaseAction;
import YuGiOh.model.card.action.SurrenderAction;
import YuGiOh.model.card.event.MonsterAttackEvent;
import YuGiOh.network.ClientConnection;
import YuGiOh.view.game.Utils;
import YuGiOh.controller.GameController;
import YuGiOh.controller.menu.*;
import YuGiOh.model.Duel;
import YuGiOh.model.Game;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.card.Card;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.FinishSelectingCondition;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.game.GameMapLocationIml;
import YuGiOh.view.game.GuiReporter;
import YuGiOh.view.game.component.*;
import YuGiOh.view.game.event.RoundOverEvent;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DuelMenuView extends BaseMenuView {
    private static DuelMenuView instance;
    @FXML
    private GameNavBar navBar;
    @FXML
    private Text selectModeText;
    @FXML
    private DuelInfoBox infoBox;
    @FXML
    private GameField gameField;

    @Getter
    private CardSelector selector;

    @Getter
    private DuelMenuApi api;

    private Player player;

    public DuelMenuView() {
        instance = this;
    }

    public static DuelMenuView getInstance() {
        if (instance == null)
            instance = new DuelMenuView();
        return instance;
    }

    public static void init(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(ClientApplication.class.getResource("/fxml/DuelMenu.fxml"));
            DuelMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        try {
            this.api = new DuelMenuApi(ClientConnection.getOrCreateInstance());
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "check your connection to server and retry!").showAndWait();
            LoginMenuView.init(stage);
            return;
        }

        this.stage = primaryStage;
        this.root = root;

        this.api.getPlayer().thenAccept(player -> {
            this.player = player;
        });
        this.api.getGame().thenAccept(game->{
            this.gameField.init(game.getFirstPlayer(), game.getSecondPlayer(), new GameMapLocationIml(game.getFirstPlayer()));
            this.infoBox.init(gameField, game);
            this.selector = new CardSelector(infoBox);
            PauseTransition delay = new PauseTransition(Duration.millis(3000));
            delay.setOnFinished(e-> DuelMenuController.getInstance().runNewGame());
            delay.play();
            run();
            stage.setResizable(true);
            stage.setFullScreen(true);
            // todo we have to remove gui reporter
            GuiReporter.getInstance().addEventHandler(RoundOverEvent.MY_TYPE, e->{
                if(DuelMenuController.getInstance().getDuel().isFinished())
                    endOfDuel();
                else
                    anotherDuel();
            });
        });
    }

    public void run() {
        renderScene();
        stage.setScene(scene);
        stage.show();
    }

    private void renderScene() {
        scene = new Scene(root);
        this.gameField.prefHeightProperty().bind(root.heightProperty().multiply(0.9));
        this.gameField.prefWidthProperty().bind(root.widthProperty().multiply(0.8));
        this.infoBox.prefWidthProperty().bind(root.widthProperty().multiply(0.2));
        this.infoBox.prefHeightProperty().bind(root.heightProperty().multiply(1));
        this.navBar.prefHeightProperty().bind(root.heightProperty().multiply(0.1));
        this.selectModeText.wrappingWidthProperty().bind(gameField.widthProperty().divide(3));

        this.root.setMinWidth(400);
        this.root.setMinHeight(400);
        this.root.prefWidthProperty().bind(scene.widthProperty());
        this.root.prefHeightProperty().bind(scene.heightProperty());

        this.navBar.setSpacing(40);
        this.navBar.getChildren().add(new CustomButton("surrender", 23, ()-> showErrorAsync(api.requestToAction(new SurrenderAction(player)))));
        this.navBar.getChildren().add(new CustomButton("next phase", 23, ()-> showErrorAsync(api.requestToAction(new NextPhaseAction()))));
        scene.setCursor(new ImageCursor(Utils.getImage("Cursor/pen.png")));
    }

    public void attackRequest(Monster attacker, Monster defender) {
        // todo is it ok to build action in view?
        DuelMenuView.showErrorAsync(api.requestToAction(new MonsterAttackAction(new MonsterAttackEvent(attacker, defender))));
    }

    public CompletableFuture<Boolean> askUser(String question, String yes, String no) {
        return new AlertBox().displayTextChoicesStandAlone(question, Arrays.asList(yes, no)).thenApply(res -> res == 0);
    }

    public CompletableFuture<List<Card>> askUserToChooseCard(String message, SelectCondition selectCondition, FinishSelectingCondition finishCondition) {
        selectModeText.setText("Selecting Cards...");
        selectModeText.setFont(Font.font(25));
        selectModeText.setFill(Color.RED);
        announce(message);

        CompletableFuture<List<Card>> ret = new CompletableFuture<>();

        selector.refresh(selectCondition, CardSelector.SelectMode.Choosing);
        System.out.println("SELECTING MODE");
        selector.setOnAction(()->{
            System.out.println("CLICKED!");
            if(finishCondition.canFinish(selector.getSelectedCards())){
                selectModeText.setFill(Color.GREEN);
                List<Card> cards = selector.getSelectedCards();
                resetSelector();
                System.out.println("SELECTION COMPLETED!");
                ret.complete(cards);
            } else {
                selectModeText.setFill(Color.RED);
            }
        });
        return ret;
    }

    public CompletableFuture<Integer> askUserToChoose(String question, List<String> choices) {
        return new AlertBox().displayTextChoicesStandAlone(question, choices);
    }

    public CompletableFuture<Void> announce(String message){
        return new AlertBox().displayMessageStandAlone(message);
    }


    public void resetSelector(){
        selectModeText.setText("");
        selectModeText.setFill(Color.BLACK);
        selector.refresh();
    }

    public void anotherDuel() {
        askUserToChoose(DuelMenuController.getInstance().getDuel().getLastGameState() + "\n" + "are you ready for the next round? ", Arrays.asList("yes"))
                .thenRun(()-> HalfTimeView.init(stage));
    }

    public void endOfDuel() {
        askUserToChoose(DuelMenuController.getInstance().getDuel().getLastGameState(), Arrays.asList("back to main menu"))
                .thenRun(()-> MainMenuView.getInstance().run());
    }
}