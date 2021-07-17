package YuGiOh.api;

import YuGiOh.controller.menu.DeckMenuController;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import YuGiOh.model.exception.LogicException;
import YuGiOh.network.NetworkConnection;
import YuGiOh.network.packet.Request;

import java.util.concurrent.CompletableFuture;

public class DeckMenuApi extends BaseMenuApi {
    public DeckMenuApi(NetworkConnection connection) {
        super(connection);
    }

    public CompletableFuture<Void> createDeck(String deckName) {
        return askToSendRequestVoid(()->new Request(DeckMenuController.class.getDeclaredMethod("createDeck", Request.class, String.class), deckName));
    }

    public CompletableFuture<Void> deleteDeck(String deckName) {
        return askToSendRequestVoid(()->new Request(DeckMenuController.class.getDeclaredMethod("deleteDeck", Request.class, String.class), deckName));
    }

    public CompletableFuture<Void> setActiveDeck(String deckName) {
        return askToSendRequestVoid(()->new Request(DeckMenuController.class.getDeclaredMethod("setActiveDeck", Request.class, String.class), deckName));
    }

    public CompletableFuture<Void> addCardToDeck(String cardName, String deckName, boolean force, boolean side) {
        return askToSendRequestVoid(()->new Request(DeckMenuController.class.getDeclaredMethod("addCardToDeck", Request.class, String.class, String.class, boolean.class, boolean.class), cardName, deckName, force, side));
    }

    public CompletableFuture<Deck> deckParser(String deckName) {
        return askToSendRequest(()->new Request(DeckMenuController.class.getDeclaredMethod("deckParser", Request.class, String.class), deckName))
                .thenApply(res -> (Deck) res.getData());
    }

    public CompletableFuture<Void> removeCardFromDeck(String cardName, String deckName, boolean side) {
        return askToSendRequestVoid(()->new Request(DeckMenuController.class.getDeclaredMethod("removeCardFromDeck", Request.class, String.class, String.class, boolean.class), cardName, deckName, side));
    }
}
