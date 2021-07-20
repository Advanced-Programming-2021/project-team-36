package YuGiOh.api;

import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.model.User;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;
import YuGiOh.network.packet.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class BaseMenuApi {
    protected NetworkConnection connection;

    public BaseMenuApi(NetworkConnection connection) {
        this.connection = connection;
    }
    protected CompletableFuture<Response<?>> askToSendRequest(ExceptionalRequestBuilder requestBuilder) {
        try {
            try {
                return this.connection.send(requestBuilder.build())
                        .thenCompose(response -> {
                            if(response.getException().isPresent())
                                return CompletableFuture.failedFuture(response.getException().get());
                            else
                                return CompletableFuture.completedFuture(response);
                        });
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return CompletableFuture.failedFuture(ioException);
            }
        } catch (NoSuchMethodException | SecurityException e) {
            throw new Error(e);
        }
    }
    protected CompletableFuture<Void> askToSendRequestVoid(ExceptionalRequestBuilder requestBuilder) {
        return askToSendRequest(requestBuilder).thenAccept(res->{});
    }
    public CompletableFuture<User> getUserFromServer() {
        return askToSendRequest(()-> new Request(LoginMenuController.class.getDeclaredMethod("getUser", Request.class)))
                .thenApply(res -> (User) res.getUser());
    }

    protected interface ExceptionalRequestBuilder {
        Request build() throws NoSuchMethodException, SecurityException;
    }
}
