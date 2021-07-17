package YuGiOh.controller.menu;

import YuGiOh.model.exception.LogicException;
import YuGiOh.model.User;
import YuGiOh.model.enums.Color;
import YuGiOh.network.packet.Request;
import YuGiOh.utils.CustomPrinter;


public class ProfileMenuController extends BaseMenuController {
    public static void changeNickname(Request request, String nickname) throws LogicException {
        if (User.getUserByNickname(nickname) != null)
            throw new LogicException("user with nickname " + nickname + " already exists");
        request.getUser().setNickname(nickname);
        CustomPrinter.println("nickname changed successfully!", Color.Default);
    }

    public static void changePassword(Request request, String oldPassword, String newPassword) throws LogicException {
        if (!request.getUser().authenticate(oldPassword))
            throw new LogicException("current password is invalid");
        if (request.getUser().getPassword().equals(newPassword))
            throw new LogicException("please enter a new password");
        request.getUser().setPassword(newPassword);
        CustomPrinter.println("password changed successfully!", Color.Default);
    }
}