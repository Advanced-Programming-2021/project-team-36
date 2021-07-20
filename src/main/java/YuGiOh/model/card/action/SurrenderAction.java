package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.event.Event;
import YuGiOh.model.card.event.NonGameEvent;
import YuGiOh.model.enums.GameResult;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class SurrenderAction extends Action {

    public SurrenderAction(Player player) {
        super(new NonGameEvent(), ()->{
            Game game = GameController.instance.getGame();
            player.setLifePoint(0);
            throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, player, game.getOtherPlayer(player), game.getOtherPlayer(player).getLifePoint());
        });
    }

    @Override
    protected CompletableFuture<Void> preprocess() {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void validateEffect() throws ValidateResult, ValidateResult {

    }
}
