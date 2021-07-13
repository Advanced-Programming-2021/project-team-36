package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.event.NonGameEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.GameResult;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.exception.ResistToChooseCard;

import java.util.concurrent.CompletableFuture;

public class DrawCardAction extends Action {
    private final Player player;

    public DrawCardAction(Player player) {
        super(new NonGameEvent(), ()->{
            Card card = player.getBoard().getMainDeck().getTopCard();
            if(card == null)
                throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, player, GameController.getInstance().getGame().getOtherPlayer(player), GameController.getInstance().getGame().getOtherPlayer(player).getLifePoint());
            player.getBoard().drawCardFromDeck();
            CustomPrinter.println(String.format("new card added to the hand : <%s>", card.getName()), Color.Blue);
            return CompletableFuture.completedFuture(null);
        });
        this.player = player;
    }

    @Override
    public void validateEffect() throws ValidateResult {
        Card card = player.getBoard().getMainDeck().getTopCard();
        if (card == null)
            throw new ValidateResult("There is no card to draw");
    }
}
