package YuGiOh.view;

import YuGiOh.controller.MainGameThread;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.gui.AlertBox;
import YuGiOh.view.gui.CustomButton;
import YuGiOh.view.gui.DuelInfoBox;
import YuGiOh.view.gui.GameField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DuelMenuView extends Application {
    private Stage stage;
    private final double WIDTH = 1024, HEIGHT = 768;
    private Scene scene;
    private final StackPane stackPane;
    private final HBox root;
    private GameField gameField;
    private DuelInfoBox infoBox;
    @Getter
    private CardSelector selector;
    private Game game;

    public DuelMenuView(){
        super();
        root = new HBox();
        stackPane = new StackPane(root);
        stackPane.setMinWidth(WIDTH);
        stackPane.setMinHeight(HEIGHT);
    }

    public void startNewGame(Game game) {
        this.game = game;
        gameField = new GameField(game, root.widthProperty().multiply(0.8), root.heightProperty().multiply(1));
        infoBox = new DuelInfoBox(game, root.widthProperty().multiply(0.2), root.heightProperty().multiply(1));
        infoBox.setGameField(gameField);
        root.getChildren().clear();
        root.getChildren().addAll(infoBox, gameField);
        selector = new CardSelector(infoBox);
        stage.show();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        scene = new Scene(stackPane, WIDTH, HEIGHT);
        stage.setScene(scene);
        addPlayPauseController();
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

    public Card askUserToChooseCard(String message, SelectCondition condition) throws ResistToChooseCard {
        List<Card> cards = new ArrayList<>();
        game.getAllCards().forEach(c->{
            if (condition.canSelect(c))
                cards.add(c);
        });
        List<CustomButton> buttons = new ArrayList<>();
        cards.forEach(c->{
            buttons.add(new CustomButton(c.getName(), 17, ()->{}));
        });

        int ret = MainGameThread.getInstance().blockUnblockRunningThreadAndDoInGui((MainGameThread.Task<Integer>) ()->
                new AlertBox().displayChoicesStandAlone(message, buttons));
        if(ret == -1)
            throw new ResistToChooseCard();
        return cards.get(ret);
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
        new AlertBox().displayMessageStandAlone(message);
    }

    public void resetSelector(){
        selector.refresh();
    }
}
