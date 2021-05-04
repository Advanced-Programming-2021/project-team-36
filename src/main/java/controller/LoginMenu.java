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

    public static void login(String username, String password) throws ModelException {
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        if (!User.getUserByUsername(username).getPassword().equals(password))
            throw new ModelException("Username and password didn’t match!");
        User user = User.getUserByUsername(username);
        User.setCurrentUser(user);
        System.out.println("user logged in successfully!");
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