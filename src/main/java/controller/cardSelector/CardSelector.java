package controller.cardSelector;

import Utils.CustomPrinter;
import controller.LogicException;
import lombok.Getter;
import model.CardAddress;
import model.Game;
import model.card.Card;

public class CardSelector {
    @Getter
    private static CardSelector instance;
    Game game;

    @Getter
    private CardAddress selectedCardAddress;

    public CardSelector(Game game){
        this.game = game;
        instance = this;
    }

    public void selectCard(CardAddress cardAddress) throws LogicException {
        if (game.getCardByCardAddress(cardAddress) == null)
            throw new LogicException("no card found in the given position");
        selectedCardAddress = cardAddress;
        CustomPrinter.println("card selected");
    }

    public void deselectCard() throws LogicException {
        if (selectedCardAddress == null)
            throw new LogicException("no card is selected");
        selectedCardAddress = null;
        CustomPrinter.println("card deselected");
    }

    public Card getSelectedCard() throws LogicException {
        if (!isAnyCardSelected())
            throw new LogicException("no card is selected");
        return game.getCardByCardAddress(selectedCardAddress);
    }

    public void showSelectedCard() throws LogicException {
        CustomPrinter.println(getSelectedCard().toString());
    }

    public boolean isAnyCardSelected() {
        return selectedCardAddress != null;
    }

}
