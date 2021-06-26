package YuGiOh.view.cardSelector;

import YuGiOh.controller.LogicException;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;

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
        CustomPrinter.println("card selected", Color.Default);
    }

    public void deselectCard() throws LogicException {
        if (selectedCardAddress == null)
            throw new LogicException("no card is selected");
        selectedCardAddress = null;
        CustomPrinter.println("card deselected", Color.Default);
    }

    public Card getSelectedCard() throws LogicException {
        if (!isAnyCardSelected())
            throw new LogicException("no card is selected");
        return game.getCardByCardAddress(selectedCardAddress);
    }

    public void showSelectedCard() throws LogicException {
        CustomPrinter.println(getSelectedCard().toString(), Color.Default);
    }

    public boolean isAnyCardSelected() {
        return selectedCardAddress != null;
    }

    public void refresh() {
        selectedCardAddress = null;
    }
}