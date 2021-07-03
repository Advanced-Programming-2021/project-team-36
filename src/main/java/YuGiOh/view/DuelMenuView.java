package YuGiOh.view;

import YuGiOh.controller.MainGameThread;
import YuGiOh.controller.menu.MainMenuController;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.FinishSelectingCondition;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.gui.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DuelMenuView extends Application {
    private Stage stage;
    private final double WIDTH = 1024, HEIGHT = 768;
    private Scene scene;
    private StackPane stackPane;
    private HBox root;
    private GameField gameField;
    private DuelInfoBox infoBox;
    @Getter
    private CardSelector selector;
    private Game game;

    public void startNewGame(Game game) {
        this.game = game;
        root = new HBox();
        stackPane = new StackPane(root);
        stackPane.setMinWidth(WIDTH);
        stackPane.setMinHeight(HEIGHT);
        gameField = new GameField(game, root.widthProperty().multiply(0.8), root.heightProperty().multiply(1), new GameMapLocationIml(game));
        infoBox = new DuelInfoBox(game, root.widthProperty().multiply(0.2), root.heightProperty().multiply(1));
        infoBox.setGameField(gameField);
        root.getChildren().addAll(infoBox, gameField);
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
                    MainGameThread.getInstance().suspend();
                else
                    MainGameThread.getInstance().resume();
            }
        });
    }

    // all of this asking user must happen in Query Thread!

    public boolean askUser(String question, String yes, String no) {
        return MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui((MainGameThread.Task<Boolean>) ()->
                new AlertBox().displayYesNoStandAlone(question, yes, no));
    }

    public List<Card> askUserToChooseCard(String message, SelectCondition selectCondition, FinishSelectingCondition finishCondition) throws ResistToChooseCard {
//        MainGameThread.getInstance().onlyBlockRunningThreadThenDoInGui(()->{
//            Pane spaceRoot = new Pane();
//            spaceRoot.setBackground(new Background(new BackgroundFill(Color.ROSYBROWN, CornerRadii.EMPTY, Insets.EMPTY)));
//            gameField.getChildren().forEach(node->{
//                if(node instanceof CardFrame){
//                    if(selectCondition.canSelect(((CardFrame) node).getCard()))
//                        spaceRoot.getChildren().add(node);
//                    // we hope that layout does not change
//                }
//            });
//            Text text = new Text();
//            spaceRoot.getChildren().add(text);
//
//            selector.refresh(selectCondition, CardSelector.SelectMode.Choosing);
//            selector.setOnAction(() -> {
//                if(finishCondition.canFinish(selector.getSelectedCards()))
//                    text.setText("OK!");
//                else
//                    text.setText("BAD!");
//            });
//
//            Scene spaceScene = new Scene(spaceRoot, WIDTH, HEIGHT);
//            stage.setScene(spaceScene);
//            stage.show();
//        });
//        return selector.getSelectedCards();
        // todo we still can't choose from deck or graveyard
        announce(message);
        MainGameThread.getInstance().onlyBlockRunningThreadThenDoInGui(()->{
            selector.refresh(selectCondition, CardSelector.SelectMode.Choosing);
            selector.setOnAction(()->{
                if(finishCondition.canFinish(selector.getSelectedCards())){
                    MainGameThread.getInstance().unlockTheThreadIfMain();
                } else {
                    // todo show some error thing in gui
                }
            });
        });
        return selector.getSelectedCards();
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

    public void announce(String message){
        MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui(()->
            new AlertBox().displayMessageStandAlone(message)
        );
    }

    public void resetSelector(){
        selector.refresh();
    }
}
