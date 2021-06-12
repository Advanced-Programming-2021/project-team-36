package edu.sharif.nameless.in.seattle.yugioh.view;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.DuelMenuController;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.LoginMenuController;
import edu.sharif.nameless.in.seattle.yugioh.controller.menu.MainMenuController;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.HumanPlayer;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterState;
import edu.sharif.nameless.in.seattle.yugioh.utils.DatabaseHandler;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.CardSelector;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.SelectCondition;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.AlertBox;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.CustomButton;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.DuelInfoBox;
import edu.sharif.nameless.in.seattle.yugioh.view.gui.GameField;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DuelMenuView extends Application {
    private final double WIDTH = 1400, HEIGHT = 1000;
    private Scene scene;
    private final StackPane stackPane;
    private final HBox root;
    private GameField gameField;
    private DuelInfoBox infoBox;
    @Getter
    private CardSelector selector;
    private Game game;

    public DuelMenuView(Game game){
        super();
        this.game = game;
        root = new HBox();
        stackPane = new StackPane(root);
        stackPane.setMinWidth(WIDTH);
        stackPane.setMinHeight(HEIGHT);
    }

    @Override
    public void start(Stage stage) {
        gameField = new GameField(game, root.widthProperty().multiply(0.8), root.heightProperty().multiply(1));
        infoBox = new DuelInfoBox(game, root.widthProperty().multiply(0.2), root.heightProperty().multiply(1));
        infoBox.setGameField(gameField);
        gameField.setInfoBox(infoBox);
        root.getChildren().addAll(infoBox, gameField);
        selector = new CardSelector(gameField, infoBox);
        scene = new Scene(stackPane, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public boolean askUser(String question, String yes, String no) {
        // todo handle this better
        return new AlertBox().displayYesNoStandAlone(question, yes, no);
    }

    public Card askUserToChooseCard(String message, SelectCondition condition) throws ResistToChooseCard {
        // todo handle this better
        List<Card> cards = new ArrayList<>();
        game.getAllCards().forEach(c->{
            if(condition.canSelect(c))
                cards.add(c);
        });
        List<CustomButton> buttons = new ArrayList<>();
        cards.forEach(c->{
            buttons.add(new CustomButton(c.getName(), 17, ()->{}));
        });
        int ret = new AlertBox().displayChoicesStandAlone(message, buttons);
        if(ret == -1)
            throw new ResistToChooseCard();
        return cards.get(ret);
    }

    public int askUserToChoose(String question, List<String> choices) throws ResistToChooseCard {
        // todo handle this better
        ArrayList<CustomButton> buttons = new ArrayList<>();
        choices.forEach(s->buttons.add(new CustomButton(s, 17, ()->{})));
        int ret = new AlertBox().displayChoicesStandAlone(question, buttons);
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

    public void fireEventOnGameField(Event event){
        gameField.fireEvent(event);
    }
    public <T extends Event> void addEventListenerOnGameField(EventType<T> type, EventHandler<? super T> handler){
        gameField.addEventHandler(type, handler);
    }
}
