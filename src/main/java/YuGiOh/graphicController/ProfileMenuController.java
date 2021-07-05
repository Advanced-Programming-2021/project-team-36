package YuGiOh.graphicController;

import YuGiOh.controller.LogicException;
import YuGiOh.model.User;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;


public class ProfileMenuController extends BaseMenuController {
    @Getter
    public static ProfileMenuController instance;
    @Getter
    private final User user;

    public ProfileMenuController(User user) {
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
}