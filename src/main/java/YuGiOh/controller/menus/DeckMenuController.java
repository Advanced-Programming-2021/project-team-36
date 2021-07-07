package YuGiOh.controller.menus;

import YuGiOh.controller.LogicException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.Deck;
import lombok.Getter;


public class DeckMenuController extends BaseMenuController {
    @Getter
    public static DeckMenuController instance;
    @Getter
    private final User user;

    public DeckMenuController(User user){
        this.user = user;
        instance = this;
    }

    public void createDeck(String deckName) throws LogicException {
        if (user.getDeckByName(deckName) != null)
            throw new LogicException(String.format("Deck with name %s already exists", deckName));
        user.addDeck(new Deck(deckName));
    }

    public void deleteDeck(String deckName) {
        user.deleteDeck(user.getDeckByName(deckName));
    }

    public void setActiveDeck(String deckName) {
        user.setActiveDeck(user.getDeckByName(deckName));
    }

    public void addCardToDeck(Card card, Deck deck, boolean force, boolean side) throws LogicException{
        if (deck.getCardFrequency(card) >= user.getCardFrequency(card) && !force)
            throw new LogicException(String.format("you have only %d of %s and your deck already contains %d of them. you can't add any more to %s", user.getCardFrequency(card), card.getName(), deck.getCardFrequency(card), deck.getName()));
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

    public Deck deckParser(String deckName) {
        return user.getDeckByName(deckName);
    }
}
