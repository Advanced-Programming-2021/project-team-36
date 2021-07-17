package YuGiOh.api;

import YuGiOh.controller.menu.FactoryMenuController;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Utils;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.exception.LogicException;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.concurrent.CompletableFuture;

public class FactoryMenuApi extends BaseMenuApi {
    public FactoryMenuApi(NetworkConnection connection) {
        super(connection);
    }
    public CompletableFuture<Void> submitThisMonster(Monster monster) {
        return askToSendRequestVoid(()-> new Request(FactoryMenuController.class.getDeclaredMethod("submitThisMonster", Request.class, Monster.class), monster));
    }
    public CompletableFuture<Integer> getPrice(Monster monster) {
        return askToSendRequest(()-> new Request(FactoryMenuController.class.getDeclaredMethod("getPrice", Monster.class), monster))
                .thenApply(res -> (Integer)res.getData());
    }
}
