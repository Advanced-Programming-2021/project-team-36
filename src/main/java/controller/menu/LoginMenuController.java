package controller.menu;

import utils.CustomPrinter;
import utils.RoutingException;
import controller.ProgramController;
import lombok.Getter;
import model.ModelException;
import model.User;
import view.*;

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
            throw new ModelException(String.format("user with nickname %s already exists", username));
        new User(username, nickname, password);
        CustomPrinter.println("user created successfully!");
    }

    public void login(String username, String password) throws ModelException, RoutingException {
        User user = User.getUserByUsername(username);
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        assert user != null;
        if (!user.authenticate(password))
            throw new ModelException("Username and password didn’t match!");
        ProgramController.getInstance().navigateToMenu(new MainMenuController(user));
        CustomPrinter.println("user logged in successfully!");
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(this.getClass()))
            throw new RoutingException("can't navigate to your current menu!");
        if (menu.equals(ImportAndExportMenuController.class))
            return new ImportAndExportMenuController(); // TODO : is this correct?
        throw new RoutingException("please login first");
    }

    @Override
    public void exitMenu() {
        ProgramController.getInstance().programExit();
    }
}