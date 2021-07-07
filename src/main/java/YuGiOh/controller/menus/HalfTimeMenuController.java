package YuGiOh.controller.menus;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.PlayerReadyExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;


public class HalfTimeMenuController {
    @Getter
    public static HalfTimeMenuController instance;
    private final PlayerController playerController;

    public HalfTimeMenuController(PlayerController playerController) {
        this.playerController = playerController;
        instance = this;
    }

    private Player getPlayer() {
        return playerController.getPlayer();
    }

    public void addCardToDeck(Card card) throws LogicException {
        if (!getPlayer().getSideDeck().hasCard(card))
            throw new LogicException("you don't have this card in your side deck");
        getPlayer().getMainDeck().addCard(card);
        getPlayer().getSideDeck().removeCard(card);
        CustomPrinter.println("card moved to main deck successfully", Color.Default);
    }

    public void removeCardFromDeck(Card card) throws LogicException {
        if (!getPlayer().getMainDeck().hasCard(card))
            throw new LogicException("you don't have this card in your main deck");
        getPlayer().getMainDeck().removeCard(card);
        getPlayer().getSideDeck().addCard(card);
        CustomPrinter.println("card moved to side deck successfully", Color.Default);
    }

    public void showDeck(boolean side) {
        CustomPrinter.println(getPlayer().getDeck().info(side), Color.Default);
    }

    public void ready() throws PlayerReadyExceptionEvent, LogicException {
        if (!getPlayer().getMainDeck().isValid())
            throw new LogicException("your main deck is not valid");
        throw new PlayerReadyExceptionEvent();
    }
}
