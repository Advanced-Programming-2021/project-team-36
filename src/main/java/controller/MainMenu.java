package controller;

import Utils.Router;
import Utils.RoutingException;
import model.User;
import view.Context;

public class MainMenu {
    public static void startNewDuel(Context context, User secondUser, int round) throws RoutingException {

        Router.navigateToMenu(view.DuelMenu.class);
    }
    public static void startDuelWithAI(Context context, int round) throws RoutingException {

        Router.navigateToMenu(view.DuelMenu.class);
    }
    public static void logout(Context context) throws RoutingException {
        context.logout();
        Router.navigateToMenu(view.LoginMenu.class);
        System.out.println("user logged out successfully!");
    }
}