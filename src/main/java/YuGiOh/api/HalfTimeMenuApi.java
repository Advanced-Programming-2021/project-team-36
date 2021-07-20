package YuGiOh.api;

import YuGiOh.controller.menu.HalfTimeMenuController;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import YuGiOh.model.enums.Color;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.exception.eventException.PlayerReadyExceptionEvent;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class HalfTimeMenuApi extends BaseMenuApi {
    public HalfTimeMenuApi(NetworkConnection connection) {
        super(connection);
    }

    // todo maybe it should be cardId instead of card object

    public CompletableFuture<Void> addCardToDeck(Card card) {
        return askToSendRequestVoid(()-> new Request(HalfTimeMenuController.class.getDeclaredMethod("addCardToDeck", Request.class, Card.class), card));
    }

    public CompletableFuture<Void> removeCardFromDeck(Card card) {
        return askToSendRequestVoid(()-> new Request(HalfTimeMenuController.class.getDeclaredMethod("removeCardFromDeck", Request.class, Card.class), card));
    }

    public CompletableFuture<Void> ready() {
        return askToSendRequestVoid(()-> new Request(HalfTimeMenuController.class.getDeclaredMethod("ready", Request.class)));
    }

    public CompletableFuture<Deck> getDeck() {
        return askToSendRequest(()-> new Request(HalfTimeMenuController.class.getDeclaredMethod("getDeck", Request.class)))
            .thenApply(response -> (Deck) response.getData());
    }
}
