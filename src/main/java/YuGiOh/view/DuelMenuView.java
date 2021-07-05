package YuGiOh.view;

import YuGiOh.controller.MainGameThread;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.FinishSelectingCondition;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.gui.*;
import YuGiOh.view.gui.component.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DuelMenuView extends Application {
    private Stage stage;
    private final double WIDTH = 1024, HEIGHT = 768;
    private Scene scene;
    private GameNavBar navBar;
    private Text selectModeText;

    @Getter
    private CardSelector selector;

    public void startNewGame(Game game) {
        HBox root = new HBox();
        StackPane stackPane = new StackPane(root);
        stackPane.setMinWidth(WIDTH);
        stackPane.setMinHeight(HEIGHT);
        GameField gameField = new GameField(game, root.widthProperty().multiply(0.8), root.heightProperty().multiply(0.9), new GameMapLocationIml(game));
        navBar = new GameNavBar();
        navBar.minHeightProperty().bind(root.heightProperty().multiply(0.1));
        selectModeText = new Text();
        selectModeText.setFont(Font.font(30));
        navBar.addItem(selectModeText);
        DuelInfoBox infoBox = new DuelInfoBox(game, root.widthProperty().multiply(0.2), root.heightProperty().multiply(1));
        infoBox.setGameField(gameField);
        root.getChildren().addAll(infoBox, new VBox(navBar, gameField));
        selector = new CardSelector(infoBox);
        scene = new Scene(stackPane, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
        addPlayPauseController();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    public void addPlayPauseController(){
        // todo remove this for production
        AtomicBoolean stopped = new AtomicBoolean(false);
        scene.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.P){
                stopped.set(!stopped.get());
                if(stopped.get())
                    MainGameThread.getInstance().lockRunningThreadIfMain();
                else
                    MainGameThread.getInstance().unlockTheThreadIfMain();
            }
        });
    }

    // all of this asking user must happen in Query Thread!

    public boolean askUser(String question, String yes, String no) {
        return MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui((MainGameThread.Task<Boolean>) ()->
                new AlertBox().displayYesNoStandAlone(question, yes, no));
    }

    public List<Card> askUserToChooseCard(String message, SelectCondition selectCondition, FinishSelectingCondition finishCondition) throws ResistToChooseCard {
        selectModeText.setText("Selecting Time");
        selectModeText.setFill(Color.RED);
        announce(message);
        MainGameThread.getInstance().onlyBlockRunningThreadThenDoInGui(()->{
            selector.refresh(selectCondition, CardSelector.SelectMode.Choosing);
            selector.setOnAction(()->{
                if(finishCondition.canFinish(selector.getSelectedCards())){
                    selectModeText.setFill(Color.GREEN);
                    MainGameThread.getInstance().unlockTheThreadIfMain();
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

        int ret = MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui((MainGameThread.Task<Integer>) ()->
                new AlertBox().displayChoicesStandAlone(question, buttons));
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
        MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui(()->
            new AlertBox().displayMessageStandAlone(message)
        );
    }

    public void resetSelector(){
        selectModeText.setText("");
        selectModeText.setFill(Color.BLACK);
        selector.refresh();
    }
}
