package YuGiOh.controller.menu;

import YuGiOh.model.card.Utils;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import YuGiOh.network.packet.Request;
import lombok.Getter;
import lombok.experimental.UtilityClass;


public class DeckMenuController extends BaseMenuController {
    public void createDeck(Request request, String deckName) throws LogicException {
        if (request.getUser().getDeckByName(deckName) != null)
            throw new LogicException(String.format("Deck with name %s already exists", deckName));
        request.getUser().addDeck(new Deck(deckName));
    }

    public void deleteDeck(Request request, String deckName) {
        request.getUser().deleteDeck(request.getUser().getDeckByName(deckName));
    }

    public void setActiveDeck(Request request, String deckName) {
        request.getUser().setActiveDeck(request.getUser().getDeckByName(deckName));
    }

    public void addCardToDeck(Request request, String cardName, String deckName, boolean force, boolean side) throws LogicException{
        Deck deck = request.getUser().getDeckByName(deckName);
        Card card = Utils.getCard(cardName);
        if (deck.getCardFrequency(card) >= request.getUser().getCardFrequency(card) && !force)
            throw new LogicException(String.format("you have only %d of %s and your deck already contains %d of them. you can't add any more to %s", request.getUser().getCardFrequency(card), card.getName(), deck.getCardFrequency(card), deck.getName()));
        if (deck.getCardFrequency(card) >= 3 && !force)
            throw new LogicException(String.format("there are already three cards with name %s in deck %s", card.getName(), deck.getName()));
        if (!side) {
            if (deck.getMainDeck().isFull() && !force)
                throw new LogicException("main deck is full");
            deck.getMainDeck().addCard(card);
        } else {
            if (deck.getSideDeck().isFull() && !force)
                throw new LogicException("side deck is full");
            deck.getSideDeck().addCard(card);
        }
    }

    public void removeCardFromDeck(Request request, String cardName, String deckName, boolean side) {
        Deck deck = request.getUser().getDeckByName(deckName);
        if(side)
            deck.getSideDeck().removeCardByName(cardName);
        else
            deck.getMainDeck().removeCardByName(cardName);
    }

    public Deck deckParser(Request request, String deckName) {
        return request.getUser().getDeckByName(deckName);
    }
}
