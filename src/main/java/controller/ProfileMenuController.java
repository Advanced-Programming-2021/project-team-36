package controller;

import model.User;
import view.Context;


public class ProfileMenuController {
    public static void changeNickname(Context context, String nickname) {
        User user = context.getUser();
        if (User.getUserByNickname(nickname) != null) {
            System.out.println("user with nickname " + nickname + " already exists");
        }
        user.setNickname(nickname);
        System.out.println("nickname changed successfully!");
    }

    public static void changePassword(Context context, String oldPassword, String newPassword) {
        User user = context.getUser();
        if (!user.authenticate(oldPassword)) {
            System.out.println("current password is invalid");
            return;
        }
        if (user.getPassword().equals(newPassword)) {
            System.out.println("please enter a new password");
            return;
        }
        user.setPassword(newPassword);
        System.out.println("password changed successfully!");
    }
}