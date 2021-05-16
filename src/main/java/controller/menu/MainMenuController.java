package controller.menu;

import utils.CustomPrinter;
import utils.RoutingException;
import controller.*;
import lombok.Getter;
import model.Game;
import model.ModelException;
import model.Player.AIPlayer;
import model.Player.HumanPlayer;
import model.User;
import view.*;

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
        ProgramController.getInstance().navigateToMenu(
            new DuelMenuController(
                    new Game(
                            new HumanPlayer(user),
                            new HumanPlayer(secondUser)
                    )
            )
        );
    }

    public void startDuelWithAI(int round) throws RoutingException, ModelException {
        ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(
                        new Game(
                                new HumanPlayer(user),
                                new AIPlayer()
                        )
                )
        );
    }

    public void startDuelDoubleAI(int round) throws RoutingException, ModelException {
        ProgramController.getInstance().navigateToMenu(
                new DuelMenuController(
                        new Game(
                                new AIPlayer(),
                                new AIPlayer()
                        )
                )
        );
    }

    public void logout() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(new LoginMenuController());
        CustomPrinter.println("user logged out successfully!");
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
