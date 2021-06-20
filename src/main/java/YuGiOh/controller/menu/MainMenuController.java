package YuGiOh.controller.menu;

import YuGiOh.controller.ProgramController;
import YuGiOh.model.Game;
import YuGiOh.model.ModelException;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.User;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.MainMenuView;
import lombok.Getter;

public class MainMenuController extends BaseMenuController {
    @Getter
    public static MainMenuController instance;
    private final User user;

    public MainMenuController(User user){
        this.view = new MainMenuView();
        this.user = user;
        instance = this;
    }

    public void startNewDuel(User secondUser, int round) throws RoutingException, ModelException {
        Game game = new Game(
                new HumanPlayer(user),
                new HumanPlayer(secondUser),
                round
        );
        CustomPrinter.println(String.format("start new duel between %s and %s", game.getFirstPlayer().getUser().getNickname(), game.getSecondPlayer().getUser().getNickname()), Color.Default);
        ProgramController.getInstance().navigateToMenu(
            new DuelMenuController(game)
        );
    }

    public void startDuelWithAI(int round) throws RoutingException, ModelException {
        Game game = new Game(
                new HumanPlayer(user),
                new AIPlayer(),
                round
        );
        CustomPrinter.println(String.format("start new duel between %s and %s", game.getFirstPlayer().getUser().getNickname(), game.getSecondPlayer().getUser().getNickname()), Color.Default);
        ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(game)
        );
    }

    public void startDuelAiWithAI(int round) throws ModelException {
        Game game = new Game(
                new AIPlayer(),
                new AIPlayer(),
                round
        );
        CustomPrinter.println(String.format("start new duel between  and %s", game.getFirstPlayer().getUser().getNickname(), game.getSecondPlayer().getUser().getNickname()), Color.Default);
        ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(game)
        );
    }

    public void logout() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(new LoginMenuController());
        CustomPrinter.println("user logged out successfully!", Color.Default);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(LoginMenuController.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenuController.class))
            throw new RoutingException("can't navigate to your current menu!");
        if (menu.equals(ProfileMenuController.class))
            return new ProfileMenuController(user);
        if (menu.equals(ScoreboardMenuController.class))
            return new ScoreboardMenuController(user);
        if (menu.equals(ShopMenuController.class))
            return new ShopMenuController(user);
        if (menu.equals(DeckMenuController.class))
            return new DeckMenuController(user);
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    public void exitMenu() throws RoutingException {
        MainMenuController.getInstance().logout();
    }
}
