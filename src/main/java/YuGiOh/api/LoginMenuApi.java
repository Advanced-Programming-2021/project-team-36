package YuGiOh.api;

import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.model.User;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.concurrent.CompletableFuture;

public class LoginMenuApi extends BaseMenuApi {
    public LoginMenuApi(NetworkConnection connection) {
        super(connection);
    }
    public CompletableFuture<Void> createUser(String username, String nickname, String password) {
        return askToSendRequest(()->new Request(LoginMenuController.class.getDeclaredMethod("createUser", String.class, String.class, String.class), username, nickname, password))
                .thenAccept(res->{});
    }
    public CompletableFuture<User> login(String username, String password) {
        return askToSendRequest(()->new Request(LoginMenuController.class.getDeclaredMethod("login", Request.class, String.class, String.class), username, password))
                .thenApply(res-> (User) res.getData());
    }
}
