package YuGiOh.graphicController;

import YuGiOh.Main;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.controller.menu.ScoreboardMenuController;
import YuGiOh.controller.menu.ShopMenuController;
import YuGiOh.graphicView.DuelMenuView;
import YuGiOh.graphicView.LoginMenuView;
import YuGiOh.model.Duel;
import YuGiOh.model.Game;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.model.enums.AIMode;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.RoutingException;
import YuGiOh.graphicView.MainMenuView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class MainMenuController extends BaseMenuController {
    @Getter
    public static MainMenuController instance;
    @Getter
    private final User user;

    public MainMenuController(User user){
        this.user = user;
        instance = this;
    }

    private void startDuel(Duel duel) throws LogicException {
        new DuelMenuController(duel);
        DuelMenuController.getInstance().getNewGameThread().start();
        CustomPrinter.println(String.format("start new duel between %s and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
    }

    public void startNewDuel(boolean userGoesFirst, String secondUsername, int round) throws LogicException, ModelException {
        User secondUser = User.getUserByUsername(secondUsername);
        if (secondUser == null)
            throw new ModelException("The specified user does not exist!");
        if (userGoesFirst) {
            startDuel(new Duel(
                    new HumanPlayer(user),
                    new HumanPlayer(secondUser),
                    round
            ));
        } else {
            startDuel(new Duel(
                    new HumanPlayer(secondUser),
                    new HumanPlayer(user),
                    round
            ));
        }
    }

    public void startDuelWithAI(boolean userGoesFirst, int round, AIMode aiMode) throws LogicException, ModelException {
        if (!userGoesFirst) {
            startDuel(new Duel(
                    new HumanPlayer(user),
                    new AIPlayer(aiMode),
                    round
            ));
        }
        else {
            startDuel(new Duel(
                    new AIPlayer(aiMode),
                    new HumanPlayer(user),
                    round
            ));
        }
    }
}
