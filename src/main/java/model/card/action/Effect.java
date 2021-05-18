package model.card.action;

import controller.LogicException;
import controller.cardSelector.ResistToChooseCard;
import controller.events.GameOverEvent;

public interface Effect {
    void run() throws GameOverEvent, ResistToChooseCard, LogicException;
}
