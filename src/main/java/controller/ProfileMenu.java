package controller;

import model.User;

public class ProfileMenu extends BaseMenu {
    public static void changeNickname(String nickname) {
        User user = User.getCurrentUser();
        if (User.getUserByNickname(nickname) != null) {
            System.out.println("user with nickname " + nickname + " already exists");
        }
        user.setNickname(nickname);
        System.out.println("nickname changed successfully!");
    }
    public static void changePassword(String oldPassword, String newPassword) {
        User user = User.getCurrentUser();
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
    protected static void showCurrentMenu() {
        System.out.println("Profile Menu");
    }
    protected static void navigateToMenu(String menu) {
        // TODO : Pending Shayan's Implementations
    }
    protected static void exit() {
        // TODO : Fully Terminate The Program
    }
    public static void programControl() {
        // TODO : Pending Shayan's Implementations
    }
}