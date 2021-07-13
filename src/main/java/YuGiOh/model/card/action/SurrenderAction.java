package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.card.event.Event;
import YuGiOh.model.card.event.NonGameEvent;
import YuGiOh.model.enums.GameResult;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public class SurrenderAction extends Action {

    public SurrenderAction() {
        super(new NonGameEvent(), ()->{
            Game game = GameController.instance.getGame();
            game.getCurrentPlayer().setLifePoint(0);
            throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, game.getCurrentPlayer(), game.getOpponentPlayer(), game.getOpponentPlayer().getLifePoint());
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
