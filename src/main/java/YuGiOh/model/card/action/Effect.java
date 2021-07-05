package YuGiOh.model.card.action;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import lombok.extern.java.Log;

public interface Effect {
    void run() throws RoundOverExceptionEvent, ResistToChooseCard, LogicException, ValidateResult;
}
