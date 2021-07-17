package YuGiOh.view.menu;

import YuGiOh.ClientApplication;
import YuGiOh.model.card.action.NextPhaseAction;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
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
        Duel duel = DuelMenuController.getInstance().getDuel();

        this.stage = primaryStage;
        this.root = root;
        Game game = duel.getCurrentGame();
        DuelMenuController.getInstance().setView(this);
        this.gameField.init(game, new GameMapLocationIml(game));
        this.infoBox.init(gameField, game);
        this.selector = new CardSelector(infoBox);
        PauseTransition delay = new PauseTransition(Duration.millis(3000));
        delay.setOnFinished(e-> DuelMenuController.getInstance().runNewGame());
        delay.play();
        run();
        stage.setResizable(true);
        stage.setFullScreen(true);
        GuiReporter.getInstance().addEventHandler(RoundOverEvent.MY_TYPE, e->{
            if(DuelMenuController.getInstance().getDuel().isFinished())
                endOfDuel();
            else
                anotherDuel();
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
        this.navBar.getChildren().add(new CustomButton("surrender", 23, ()-> GameController.getInstance().addRunnableToMainThread(()-> GameController.getInstance().getCurrentPlayerController().surrender())));
        this.navBar.getChildren().add(new CustomButton("next phase", 23, ()-> GameController.getInstance().addRunnableToMainThread(()-> new NextPhaseAction().runEffect())));
        scene.setCursor(new ImageCursor(Utils.getImage("Cursor/pen.png")));
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
        // todo here we assume at least one of players are human?
        askUserToChoose(DuelMenuController.getInstance().getDuel().getLastGameState() + "\n" + "are you ready for the next round? ", Arrays.asList("yes"))
                .thenRun(()->{
                    if (GameController.getInstance().getCurrentPlayerController().getPlayer() instanceof HumanPlayer)
                        HalfTimeView.init(stage, GameController.getInstance().getCurrentPlayerController());
                    else
                        HalfTimeView.init(stage, GameController.getInstance().getOpponentPlayerController());
                });
    }

    public void endOfDuel() {
        askUserToChoose(DuelMenuController.getInstance().getDuel().getLastGameState(), Arrays.asList("back to main menu"))
                .thenRun(()->{
                    MainMenuView.getInstance().run();
                });
    }
}