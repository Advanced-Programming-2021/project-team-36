package controller;

import model.User;
import view.Context;

import java.util.ArrayList;

public class ScoreboardMenuController {
    private static User user;

    public static void showScoreboard(Context context) {
        ArrayList<User> users = User.retrieveUsersBasedOnScore();
        int rank = 1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (i > 0 && users.get(i - 1).getScore() > user.getScore())
                rank = i + 1;
            System.out.println(rank + "- " + user.getNickname() + ": " + user.getScore());
        }
    }
}