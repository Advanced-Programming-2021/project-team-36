package YuGiOh.graphicView;

import YuGiOh.Main;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.MainGameThread;
import YuGiOh.graphicController.DuelMenuController;
import YuGiOh.model.Duel;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.FinishSelectingCondition;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.gui.GameMapLocationIml;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.component.*;
import YuGiOh.view.gui.event.RoundOverEvent;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class DuelMenuView extends BaseMenuView {
    private static DuelMenuView instance;
    private Game game;
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
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/DuelMenu.fxml"));
            DuelMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void start(Stage primaryStage, Pane root) {
        Duel duel = DuelMenuController.getInstance().getDuel();

        this.stage = primaryStage;
        this.root = root;
        this.game = duel.getCurrentGame();
        DuelMenuController.getInstance().setView(this);
        this.gameField.init(game, new GameMapLocationIml(game));
        this.infoBox.init(gameField, game);
        this.selector = new CardSelector(infoBox);
        DuelMenuController.getInstance().runNewGameThread();
        run();
        GuiReporter.getInstance().addEventHandler(RoundOverEvent.MY_TYPE, e->{
            if(DuelMenuController.getInstance().getDuel().isFinished())
                Platform.runLater(()->MainMenuView.getInstance().run());
            else
                Platform.runLater(()->DuelMenuView.init(primaryStage));
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
        this.navBar.getChildren().add(new CustomButton("surrender", 23, ()-> gameField.addRunnableToMainThread(()-> DuelMenuController.getInstance().surrender())));
        this.navBar.getChildren().add(new CustomButton("next phase", 23, ()-> gameField.addRunnableToMainThread(()-> DuelMenuController.getInstance().goNextPhase())));
        this.addPlayPauseController();
    }

    public void addPlayPauseController(){
        SimpleBooleanProperty stopped = new SimpleBooleanProperty(false);
        Button button = new CustomButton("", 23, ()-> {
            stopped.set(!stopped.get());
            if (stopped.get())
                MainGameThread.getInstance().suspend();
            else
                MainGameThread.getInstance().resume();
        });
        button.textProperty().bind(Bindings.when(stopped).then("play").otherwise("pause"));
        this.navBar.getChildren().add(button);
    }

    // all of this asking user must happen in Query Thread!

    public boolean askUser(String question, String yes, String no) {
        return new MainGameThread.TwoWayTicket<>((Callable<Boolean>) ()->
            new AlertBox().displayYesNoStandAlone(question, yes, no)
        ).runAndWait();
    }

    public List<Card> askUserToChooseCard(String message, SelectCondition selectCondition, FinishSelectingCondition finishCondition) {
        selectModeText.setText("Selecting Cards...");
        selectModeText.setFont(Font.font(25));
        selectModeText.setFill(Color.RED);
        announce(message);

        MainGameThread.OneWayTicket ticket = new MainGameThread.OneWayTicket();
        ticket.runOneWay(()->{
            selector.refresh(selectCondition, CardSelector.SelectMode.Choosing);
            selector.setOnAction(()->{
                if(finishCondition.canFinish(selector.getSelectedCards())){
                    selectModeText.setFill(Color.GREEN);
                    ticket.free();
                } else {
                    selectModeText.setFill(Color.RED);
                }
            });
        });
        List<Card> cards = selector.getSelectedCards();
        resetSelector();
        return cards;
    }

    public int askUserToChoose(String question, List<String> choices) throws ResistToChooseCard {
        ArrayList<CustomButton> buttons = new ArrayList<>();
        choices.forEach(s->buttons.add(new CustomButton(s, 17, ()->{})));

        int ret = new MainGameThread.TwoWayTicket<Integer>((Callable<Integer>) ()->
                new AlertBox().displayChoicesStandAlone(question, buttons)).runAndWait();
        if(ret == -1)
            throw new ResistToChooseCard();
        return ret;
    }

    public int askUserToChooseNoResist(String question, String... choices) {
        try {
            return askUserToChoose(question, Arrays.asList(choices));
        } catch (ResistToChooseCard e){
            return -1;
        }
    }


    public void announce(String message){
        Platform.runLater(()->{
            new AlertBox().displayMessageStandAlone(message);
        });
    }

    public void resetSelector(){
        selectModeText.setText("");
        selectModeText.setFill(Color.BLACK);
        selector.refresh();
    }
}