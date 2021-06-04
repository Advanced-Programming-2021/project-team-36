package edu.sharif.nameless.in.seattle.yugioh.controller.menu;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.view.ProfileMenuView;
import lombok.Getter;


public class ProfileMenuController extends BaseMenuController {
    @Getter
    public static ProfileMenuController instance;
    private final User user;

    public ProfileMenuController(User user) {
        this.view = new ProfileMenuView();
        this.user = user;
        instance = this;
    }

    public void changeNickname(String nickname) throws LogicException {
        if (User.getUserByNickname(nickname) != null)
            throw new LogicException("user with nickname " + nickname + " already exists");
        user.setNickname(nickname);
        CustomPrinter.println("nickname changed successfully!", Color.Default);
    }

    public void changePassword(String oldPassword, String newPassword) throws LogicException {
        if (!user.authenticate(oldPassword))
            throw new LogicException("current password is invalid");
        if (user.getPassword().equals(newPassword))
            throw new LogicException("please enter a new password");
        user.setPassword(newPassword);
        CustomPrinter.println("password changed successfully!", Color.Default);
    }

    @Override
    public void exitMenu() throws RoutingException {
        ProgramController.getInstance().navigateToMenu(MainMenuController.class);
    }

    @Override
    public BaseMenuController getNavigatingMenuObject(Class<? extends BaseMenuController> menu) throws RoutingException {
        if (menu.equals(this.getClass()))
            throw new RoutingException("can't navigate to your current menu!");
        if (menu.equals(LoginMenuController.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenuController.class))
            return MainMenuController.getInstance();
        throw new RoutingException("menu navigation is not possible");
    }

}