package YuGiOh.model.card.action;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.cardSelector.ResistToChooseCard;
import YuGiOh.controller.events.GameOverEvent;

public interface Effect {
    void run() throws GameOverEvent, ResistToChooseCard, LogicException;
}
