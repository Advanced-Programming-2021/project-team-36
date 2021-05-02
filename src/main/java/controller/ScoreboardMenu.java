package controller;

import model.User;

import java.util.ArrayList;

public class ScoreboardMenu extends BaseMenu {
    private static User user;

    private static void showScoreboard() {
        ArrayList<User> users = User.retrieveUsersBasedOnScore();
        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (i > 0 && users.get(i - 1).getScore() > user.getScore())
                rank = i + 1;
            System.out.println(rank + "- " + user.getNickname() + ": " + user.getScore());
        }
    }

    protected static void showCurrentMenu() {
        System.out.println("Scoreboard Menu");
    }

    protected static void navigateToMenu(String menu) {
        // TODO : Pending Shayan's Implementations
    }

    protected static void exit() {
        // TODO : Fully Terminate The Program
    }

    private static void start() {
        // Nothing to do here. This method is actually redundant.
    }

    public static void programControl() {
        // TODO : Pending Shayan's Implementations
    }
}