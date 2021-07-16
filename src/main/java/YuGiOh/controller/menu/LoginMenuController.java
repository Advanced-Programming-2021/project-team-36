package YuGiOh.controller.menu;

import YuGiOh.model.exception.ModelException;
import YuGiOh.model.User;
import YuGiOh.network.packet.JwtToken;
import YuGiOh.network.packet.Request;

public class LoginMenuController extends BaseMenuController {
    public static void createUser(String username, String nickname, String password) throws ModelException {
        if (User.getUserByUsername(username) != null)
            throw new ModelException(String.format("user with username %s already exists", username));
        if (User.getUserByNickname(nickname) != null)
            throw new ModelException(String.format("user with nickname %s already exists", nickname));
        new User(username, nickname, password);
    }

    public static User login(Request request, String username, String password) throws ModelException {
        User user = User.getUserByUsername(username);
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        assert user != null;
        if (!user.authenticate(password))
            throw new ModelException("Username and password didn’t match!");
        request.setToken(JwtToken.getTokenForUser(user));
        return user;
    }
}