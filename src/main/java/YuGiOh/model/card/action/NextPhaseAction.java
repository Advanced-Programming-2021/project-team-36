package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.event.NonGameEvent;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.exception.ValidateResult;

import java.util.concurrent.CompletableFuture;

public class NextPhaseAction extends Action {
    public NextPhaseAction() {
        super(new NonGameEvent(), ()-> CompletableFuture.completedFuture(null).thenRun(() -> GameController.getInstance().goNextPhaseImplementationDoNotUseThisFunction()));
    }

    @Override
    public void validateEffect() throws ValidateResult, ValidateResult {
    }
}
