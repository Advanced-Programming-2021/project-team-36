package YuGiOh.controller.menu;

import YuGiOh.model.deck.Deck;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.exception.eventException.PlayerReadyExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.Color;
import YuGiOh.network.packet.Request;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;


public class HalfTimeMenuController extends BaseMenuController {
    private static Player getPlayer(Request request) {
        return Player.getPlayerByUser(request.getUser());
    }

    public static void addCardToDeck(Request request, Card card) throws LogicException {
        if (!getPlayer(request).getSideDeck().hasCard(card))
            throw new LogicException("you don't have this card in your side deck");
        getPlayer(request).getMainDeck().addCard(card);
        getPlayer(request).getSideDeck().removeCard(card);
        CustomPrinter.println("card moved to main deck successfully", Color.Default);
    }

    public static void removeCardFromDeck(Request request, Card card) throws LogicException {
        if (!getPlayer(request).getMainDeck().hasCard(card))
            throw new LogicException("you don't have this card in your main deck");
        getPlayer(request).getMainDeck().removeCard(card);
        getPlayer(request).getSideDeck().addCard(card);
        CustomPrinter.println("card moved to side deck successfully", Color.Default);
    }

    public static void ready(Request request) throws PlayerReadyExceptionEvent, LogicException {
        if (!getPlayer(request).getMainDeck().isValid())
            throw new LogicException("your main deck is not valid");
        throw new PlayerReadyExceptionEvent();
    }

    public static Deck getDeck(Request request) {
        return getPlayer(request).getDeck();
    }
}
