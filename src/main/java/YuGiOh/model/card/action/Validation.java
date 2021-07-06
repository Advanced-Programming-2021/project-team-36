package YuGiOh.model.card.action;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.view.cardSelector.ResistToChooseCard;

public interface Validation {
    void run() throws ValidateResult;
}
