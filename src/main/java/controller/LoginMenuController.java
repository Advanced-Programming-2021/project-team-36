package controller;

import Utils.Router;
import Utils.RoutingException;
import model.ModelException;
import model.User;
import view.Context;

public class LoginMenuController {
    public static void createUser(Context context, String username, String nickname, String password) throws ModelException {
        if (User.getUserByUsername(username) != null)
            throw new ModelException(String.format("user with username %s already exists", username));
        if (User.getUserByNickname(nickname) != null)
            throw new ModelException(String.format("user with nickname %s already exists", username));
        new User(username, nickname, password);
        System.out.println("user created successfully!");
    }

    public static void login(Context context, String username, String password) throws ModelException, RoutingException {
        User user = User.getUserByUsername(username);
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        assert user != null;
        if (!user.authenticate(password))
            throw new ModelException("Username and password didn’t match!");
        context.login(user);
        Router.navigateToMenu(view.MainMenu.class);
        System.out.println("user logged in successfully!");
    }
}