package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverExceptionEvent;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;

public interface Effect {
    void run() throws RoundOverExceptionEvent, ResistToChooseCard, LogicException;
}
