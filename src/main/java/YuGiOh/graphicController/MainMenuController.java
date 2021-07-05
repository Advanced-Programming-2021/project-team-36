package YuGiOh.graphicController;

import YuGiOh.Main;
import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.controller.menu.ScoreboardMenuController;
import YuGiOh.controller.menu.ShopMenuController;
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

    public void startNewDuel(User secondUser, int round) throws RoutingException, ModelException {
        Duel duel = new Duel(
                new HumanPlayer(user),
                new HumanPlayer(secondUser),
                round
        );
        CustomPrinter.println(String.format("start new duel between %s and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
        /*ProgramController.getInstance().navigateToMenu(
            new DuelMenuController(game)
        );*/
    }

    public void startDuelWithAI(int round, AIMode aiMode) throws RoutingException, ModelException {
        Duel duel = new Duel(
                new HumanPlayer(user),
                new AIPlayer(aiMode),
                round
        );
        CustomPrinter.println(String.format("start new duel between %s and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
        /*ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(game)
        );*/
    }

    public void startDuelAiWithAI(int round, AIMode aiMode1, AIMode aiMode2) throws ModelException {
        Duel duel = new Duel(
                new AIPlayer(aiMode1),
                new AIPlayer(aiMode2),
                round
        );
        CustomPrinter.println(String.format("start new duel between  and %s", duel.getFirstPlayer().getUser().getNickname(), duel.getSecondPlayer().getUser().getNickname()), Color.Default);
        /*ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(game)
        );*/
    }

    public void logout() {
        LoginMenuView.getInstance().run();
        CustomPrinter.println("user logged out successfully!", Color.Default);
    }
}
