package YuGiOh.model.card.action;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;

public interface Effect {
    void run() throws RoundOverExceptionEvent, ResistToChooseCard, LogicException, ValidateResult;
}
