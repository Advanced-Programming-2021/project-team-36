package YuGiOh.api;

import YuGiOh.controller.menu.ShopMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.concurrent.CompletableFuture;

public class ShopMenuApi extends BaseMenuApi {
    public ShopMenuApi(NetworkConnection connection) {
        super(connection);
    }

    public CompletableFuture<Void> buy(Card card) {
        return askToSendRequestVoid(()-> new Request(ShopMenuController.class.getDeclaredMethod("buy", Request.class, Card.class), card));
    }
}
