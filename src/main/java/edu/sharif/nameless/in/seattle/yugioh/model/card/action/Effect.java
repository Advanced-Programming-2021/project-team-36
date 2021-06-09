package edu.sharif.nameless.in.seattle.yugioh.model.card.action;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverEvent;

public interface Effect {
    void run() throws RoundOverEvent, ResistToChooseCard, LogicException;
}
