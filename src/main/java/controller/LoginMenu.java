package controller;

import model.User;

public class LoginMenu extends BaseMenu {
    private static void createUser(String username, String nickname, String password) {
        String error = User.checkValidity(username, nickname, password);
        if (!error.equals("")) {
            System.out.println(error);
            return;
        }
        new User(username, password, nickname);
        System.out.println("user created successfully!");
    }

    private static void login(String username, String password) {
        if (!User.authenticateUser(username, password)) {
            System.out.println("Username and password didn't match");
            return;
        }
        User.logIn(username, password);
        System.out.println("user logged in successfully!");
        navigateToMenu("Main Menu");
    }

    protected static void showCurrentMenu() {
        System.out.println("Login Menu");
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