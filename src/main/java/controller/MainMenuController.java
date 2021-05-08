package controller;

import Utils.Router;
import Utils.RoutingException;
import model.Game;
import model.ModelException;
import model.Player.AIPlayer;
import model.Player.HumanPlayer;
import model.Player.Player;
import model.User;
import view.Context;

public class MainMenuController {
    public static void startNewDuel(Context context, User secondUser, int round) throws RoutingException, ModelException {
        context.startGame(new Game(
                new HumanPlayer(context.getUser()),
                new HumanPlayer(secondUser)
        ));
        System.out.printf("its %s's turn%n", context.getGame().getCurrentPlayer().getUser().getNickname());
        DuelMenuController.printCurrentPhase(context);
        Router.navigateToMenu(view.DuelMenu.class);
    }

    public static void startDuelWithAI(Context context, int round) throws RoutingException, ModelException {
        context.startGame(new Game(
                new HumanPlayer(context.getUser()),
                new AIPlayer()
        ));
        System.out.printf("its %s's turn%n", context.getGame().getCurrentPlayer().getUser().getNickname());
        DuelMenuController.printCurrentPhase(context);
        Router.navigateToMenu(view.DuelMenu.class);
    }

    public static void logout(Context context) throws RoutingException {
        context.logout();
        Router.navigateToMenu(view.LoginMenu.class);
        System.out.println("user logged out successfully!");
    }
}
