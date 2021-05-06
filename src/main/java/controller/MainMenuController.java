package controller;

import Utils.Router;
import Utils.RoutingException;
import model.Game;
import model.ModelException;
import model.Player;
import model.User;
import view.Context;

public class MainMenuController {
    public static void startNewDuel(Context context, User secondUser, int round) throws RoutingException, ModelException {
        context.startGame(new Game(
                new Player(context.getUser()),
                new Player(secondUser)
        ));
        Router.navigateToMenu(view.DuelMenu.class);
    }

    public static void startDuelWithAI(Context context, int round) throws RoutingException {
        // todo in yekam dastan dare. bayad kollan model kar hamoon ro avaz konim
        Router.navigateToMenu(view.DuelMenu.class);
    }

    public static void logout(Context context) throws RoutingException {
        context.logout();
        Router.navigateToMenu(view.LoginMenu.class);
        System.out.println("user logged out successfully!");
    }
}
