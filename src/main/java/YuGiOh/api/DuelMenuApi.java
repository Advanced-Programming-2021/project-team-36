package YuGiOh.api;

import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.action.Action;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class DuelMenuApi extends BaseMenuApi {

    public DuelMenuApi(NetworkConnection connection) {
        super(connection);
    }

    public CompletableFuture<Void> requestToAction(Action action) {
        return askToSendRequestVoid(()-> new Request(DuelMenuController.class.getDeclaredMethod("requestToDoAction", Request.class, Action.class), action));
    }
    public CompletableFuture<Boolean> validateAction(Action action) {
        return askToSendRequest(()-> new Request(DuelMenuController.class.getDeclaredMethod("validateAction", Action.class), action));
    }
    public CompletableFuture<HashMap<Card, CardAddress>> getCardPositions() {

    }
    public CompletableFuture<Game> getGame() {

    }
    public CompletableFuture<Player> getPlayer() {

    }
}
