package controller;

import model.User;

public class MainMenu extends BaseMenu {
    public static void startNewDuel(User secondPlayer, int round) {

    }
    public static void startDuelWithAI(int round) {

    }
    public static void logout() {
        User.logOut();
        System.out.println("user logged out successfully!");
        navigateToMenu("Login Menu");
    }
    protected static void showCurrentMenu() {

    }
    protected static void navigateToMenu(String menu) {

    }
    protected static void exit() {

    }
    public static void programControl() {

    }
}