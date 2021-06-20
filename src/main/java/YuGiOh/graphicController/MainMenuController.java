package YuGiOh.graphicController;

import YuGiOh.Main;
import YuGiOh.graphicView.LoginMenuView;
import YuGiOh.model.Game;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
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

    public void start(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/MainMenu.fxml"));
            MainMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void startNewDuel(User secondUser, int round) throws RoutingException, ModelException {
        Game game = new Game(
                new HumanPlayer(user),
                new HumanPlayer(secondUser),
                round
        );
        CustomPrinter.println(String.format("start new duel between %s and %s", game.getFirstPlayer().getUser().getNickname(), game.getSecondPlayer().getUser().getNickname()), Color.Default);
        /*ProgramController.getInstance().navigateToMenu(
            new DuelMenuController(game)
        );*/
    }

    public void startDuelWithAI(int round) throws RoutingException, ModelException {
        Game game = new Game(
                new HumanPlayer(user),
                new AIPlayer(),
                round
        );
        CustomPrinter.println(String.format("start new duel between %s and %s", game.getFirstPlayer().getUser().getNickname(), game.getSecondPlayer().getUser().getNickname()), Color.Default);
        /*ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(game)
        );*/
    }

    public void startDuelAiWithAI(int round) throws ModelException {
        Game game = new Game(
                new AIPlayer(),
                new AIPlayer(),
                round
        );
        CustomPrinter.println(String.format("start new duel between  and %s", game.getFirstPlayer().getUser().getNickname(), game.getSecondPlayer().getUser().getNickname()), Color.Default);
        /*ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(game)
        );*/
    }

    public void logout() {
        LoginMenuView.getInstance().run();
        CustomPrinter.println("user logged out successfully!", Color.Default);
    }

    @Override
    public void exitMenu() throws RoutingException {
        MainMenuController.getInstance().logout();
    }
}
