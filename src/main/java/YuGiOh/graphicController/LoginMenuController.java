package YuGiOh.graphicController;

import YuGiOh.Main;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.Cheat;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.RoutingException;
import YuGiOh.graphicView.LoginMenuView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;

public class LoginMenuController extends BaseMenuController {
    @Getter
    public static LoginMenuController instance;

    public LoginMenuController(){
        //this.view = LoginMenuView.getInstance();
        instance = this;
    }

    public void start(Stage primaryStage) {
        try {
            Pane root = FXMLLoader.load(Main.class.getResource("/fxml/LoginMenu.fxml"));
            LoginMenuView.getInstance().start(primaryStage, root);
        } catch (IOException ignored) {
        }
    }

    public void createUser(String username, String nickname, String password) throws ModelException {
        if (User.getUserByUsername(username) != null)
            throw new ModelException(String.format("user with username %s already exists", username));
        if (User.getUserByNickname(nickname) != null)
            throw new ModelException(String.format("user with nickname %s already exists", nickname));
        new User(username, nickname, password);
        CustomPrinter.println("user created successfully!", Color.Default);
    }

    public void login(String username, String password) throws ModelException {
        User user = User.getUserByUsername(username);
        if (User.getUserByUsername(username) == null)
            throw new ModelException("Username and password didn’t match!");
        assert user != null;
        if (!user.authenticate(password))
            throw new ModelException("Username and password didn’t match!");
        new MainMenuController(user);
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