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

    public CompletableFuture<Void> startNewDuel(boolean userGoesFirst, String secondUsername, int round) {
        return askToSendRequestVoid(()-> new Request(StartNewDuelController.class.getDeclaredMethod("startNewDuel", Request.class, boolean.class, String.class, int.class), userGoesFirst, secondUsername, round));
    }
    public CompletableFuture<Void> startDuelWithAI(boolean userGoesFirst, int round, AIMode aiMode) {
        return askToSendRequestVoid(()-> new Request(StartNewDuelController.class.getDeclaredMethod("startDuelWithAI", Request.class, boolean.class, int.class, AIMode.class), userGoesFirst, round, aiMode));
    }
}
