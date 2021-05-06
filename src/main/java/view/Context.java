package view;

import model.Game;
import model.User;

public class Context {
    private User user;
    private Game game;

    private static Context myObject;

    private Context() {
    }

    public static Context getInstance() {
        if (myObject == null)
            myObject = new Context();
        return myObject;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void login(User user) {
        this.user = user;
    }

    public void logout() {
        this.user = null;
    }

    public void startGame(Game game) {
        this.game = game;
    }

    public void endGame() {
        this.game = null;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }
}
