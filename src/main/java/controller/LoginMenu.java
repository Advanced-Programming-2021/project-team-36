package controller;

import model.ModelException;
import model.User;

public class LoginMenu extends BaseMenu {
    public static void createUser(String username, String nickname, String password) throws ModelException {
        if (User.getUserByUsername(username) != null)
            throw new ModelException(String.format("user with username %s already exists", username));
        if (User.getUserByNickname(nickname) != null)
            throw new ModelException(String.format("user with nickname %s already exists", username));
        new User(username, password, nickname);
        System.out.println("user created successfully!");
    }

    public static void login(User user, String password) {
        if (!User.authenticateUser(user, password)) {
            System.out.println("Username and password didn't match");
            return;
        }
        User.logIn(user, password);
        System.out.println("user logged in successfully!");
        navigateToMenu("Main Menu");
    }

    public static void showCurrentMenu() {
        System.out.println("Login Menu");
    }

    public static void navigateToMenu(String menu) {
        // TODO : Pending Shayan's Implementations
    }

    public static void exit() {
        // TODO : Fully Terminate The Program
    }

    public static void programControl() {
        // TODO : Pending Shayan's Implementations
    }
}