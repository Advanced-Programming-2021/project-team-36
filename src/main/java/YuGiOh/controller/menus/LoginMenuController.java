package YuGiOh.controller.menus;

import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import YuGiOh.utils.Cheat;
import lombok.Getter;

public class LoginMenuController extends BaseMenuController {
    @Getter
    public static LoginMenuController instance;

    public LoginMenuController(){
        instance = this;
    }

    public void createUser(String username, String nickname, String password) throws ModelException {
        if (User.getUserByUsername(username) != null)
            throw new ModelException(String.format("user with username %s already exists", username));
        if (User.getUserByNickname(nickname) != null)
            throw new ModelException(String.format("user with nickname %s already exists", nickname));
        new User(username, nickname, password);
    }

    public User login(String username, String password) throws ModelException {
        User user = User.getUserByUsername(username);
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        assert user != null;
        if (!user.authenticate(password))
            throw new ModelException("Username and password didn’t match!");
        new MainMenuController(user);
        return user;
    }

    public void cheatLogin(String username, String nickname, String password) throws ModelException {
        // TODO: Do we need this?
        createUser(username, nickname, password);
        User user = User.getUserByUsername(username);
        Cheat.buildSuperUser(user);
    }
}