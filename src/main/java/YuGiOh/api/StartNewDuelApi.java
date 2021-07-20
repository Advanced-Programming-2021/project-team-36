package YuGiOh.api;

import YuGiOh.controller.menu.StartNewDuelController;
import YuGiOh.model.enums.AIMode;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.concurrent.CompletableFuture;

public class StartNewDuelApi extends BaseMenuApi {
    public StartNewDuelApi(NetworkConnection connection) {
        super(connection);
    }

    public CompletableFuture<Void> startNewDuel(String secondUsername, int round) {
        return askToSendRequestVoid(()-> new Request(StartNewDuelController.class.getDeclaredMethod("startNewDuel", Request.class, String.class, int.class), secondUsername, round));
    }
    public CompletableFuture<Void> startDuelWithAI(int round, AIMode aiMode) {
        return askToSendRequestVoid(()-> new Request(StartNewDuelController.class.getDeclaredMethod("startDuelWithAI", Request.class, int.class, AIMode.class), round, aiMode));
    }
    public CompletableFuture<Boolean> doesUserStart() {
        return askToSendRequest(()-> new Request(StartNewDuelController.class.getDeclaredMethod("doesUserStart", Request.class)))
                .thenApply(response -> (boolean) response.getData());
    }
}
