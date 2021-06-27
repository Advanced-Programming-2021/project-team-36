package YuGiOh.controller.menu;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.ProgramController;
import YuGiOh.controller.events.PlayerReadyExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.deck.BaseDeck;
import YuGiOh.model.deck.Deck;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.Debugger;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.DeckMenuView;
import YuGiOh.view.HalfTimeMenuView;
import YuGiOh.view.ParserException;
import lombok.Getter;

import java.util.Arrays;


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

    public void control() {
        CustomPrinter.println("nice job " + playerController.getPlayer().getUser().getNickname(), Color.Blue);
        CustomPrinter.println("now you are in half time", Color.Blue);
        CustomPrinter.println("when you say ready new round begins", Color.Blue);
        HalfTimeMenuView view = new HalfTimeMenuView();
        while (true) {
            view.runNextCommand();
        }
    }
}
