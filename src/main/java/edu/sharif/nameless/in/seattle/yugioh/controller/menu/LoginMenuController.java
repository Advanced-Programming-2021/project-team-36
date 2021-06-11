package edu.sharif.nameless.in.seattle.yugioh.controller.menu;

import edu.sharif.nameless.in.seattle.yugioh.view.LoginMenuView;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.utils.Cheat;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.User;

public class LoginMenuController extends BaseMenuController {
    @Getter
    public static LoginMenuController instance;

    public LoginMenuController(){
        this.view = new LoginMenuView();
        instance = this;
    }

    public void createUser(String username, String nickname, String password) throws ModelException {
        if (User.getUserByUsername(username) != null)
            throw new ModelException(String.format("user with username %s already exists", username));
        if (User.getUserByNickname(nickname) != null)
            throw new ModelException(String.format("user with nickname %s already exists", nickname));
        new User(username, nickname, password);
        CustomPrinter.println("user created successfully!", Color.Default);
    }

    public void login(String username, String password) throws ModelException, RoutingException {
        User user = User.getUserByUsername(username);
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        assert user != null;
        if (!user.authenticate(password))
            throw new ModelException("Username and password didn’t match!");
        ProgramController.getInstance().navigateToMenu(new MainMenuController(user));
        CustomPrinter.println("user logged in successfully!", Color.Default);
    }

    public void cheatLogin(String username, String nickname, String password) throws ModelException {
        createUser(username, nickname, password);
        User user = User.getUserByUsername(username);
        Cheat.buildSuperUser(user);
        ProgramController.getInstance().navigateToMenu(new MainMenuController(user));
        CustomPrinter.println("user logged in successfully!", Color.Default);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(this.getClass()))
            throw new RoutingException("can't navigate to your current menu!");
        throw new RoutingException("please login first");
    }

    @Override
    public void exitMenu() {
        ProgramController.getInstance().programExit();
    }
}