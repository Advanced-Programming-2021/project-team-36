package YuGiOh.api;

import YuGiOh.controller.menu.ProfileMenuController;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.concurrent.CompletableFuture;

public class ProfileMenuApi extends BaseMenuApi {
    public ProfileMenuApi(NetworkConnection connection) {
        super(connection);
    }
    public CompletableFuture<Void> changeNickname(String nickname) {
        return askToSendRequestVoid(()-> new Request(ProfileMenuController.class.getDeclaredMethod("changeNickname", Request.class, String.class), nickname));
    }

    public CompletableFuture<Void> changePassword(String oldPassword, String newPassword) {
        return askToSendRequestVoid(()-> new Request(ProfileMenuController.class.getDeclaredMethod("changePassword", Request.class, String.class, String.class), oldPassword, newPassword));
    }
}
