package YuGiOh.controller.cardSelector;

import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.controller.LogicException;
import lombok.Getter;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;

import java.util.HashSet;
import java.util.Set;

public class MultiCardSelector {
    @Getter
    private static MultiCardSelector instance;
    Game game;

    @Getter
    private final Set<Card> selectedCards;

    public MultiCardSelector(Game game){
        this.game = game;
        selectedCards = new HashSet<>();
        instance = this;
    }

    public void selectCard(CardAddress cardAddress) throws LogicException {
        Card card = game.getCardByCardAddress(cardAddress);
        if(card == null)
            throw new LogicException("no card found in the given position");
        if(isCardSelected(cardAddress))
            throw new LogicException("you have selected this card before");
        selectedCards.add(card);
        CustomPrinter.println("card selected", Color.Default);
    }

    public boolean isCardSelected(Card card){
        return selectedCards.contains(card);
    }

    public boolean isCardSelected(CardAddress cardAddress) throws LogicException {
        Card card = game.getCardByCardAddress(cardAddress);
        if(card == null)
            throw new LogicException("no card found in the given position");
        return isCardSelected(card);
    }

    public void deselectCard(CardAddress cardAddress) throws LogicException {
        Card card = game.getCardByCardAddress(cardAddress);
        if(!isCardSelected(cardAddress))
            throw new LogicException("no such card is selected");
        selectedCards.remove(card);
        CustomPrinter.println("card deselected", Color.Default);
    }
}
