package controller;

import Utils.RoutingException;
import lombok.Getter;
import model.User;
import view.*;


public class ProfileMenuController extends BaseMenuController {
    @Getter
    public static ProfileMenuController instance;
    private final User user;

    public ProfileMenuController(User user){
        this.view = new ProfileMenuView();
        this.user = user;
        instance = this;
    }

    public void changeNickname(String nickname) {
        if (User.getUserByNickname(nickname) != null) {
            System.out.println("user with nickname " + nickname + " already exists");
        }
        user.setNickname(nickname);
        System.out.println("nickname changed successfully!");
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!user.authenticate(oldPassword)) {
            System.out.println("current password is invalid");
            return;
        }
        if (user.getPassword().equals(newPassword)) {
            System.out.println("please enter a new password");
            return;
        }
        user.setPassword(newPassword);
        System.out.println("password changed successfully!");
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