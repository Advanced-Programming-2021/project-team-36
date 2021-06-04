package edu.sharif.nameless.in.seattle.yugioh.view.CommandLine;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.view.ParserException;

import java.util.Map;

public interface Handler {
    void run(Map<String, String> cmd) throws ParserException, ModelException, LogicException, RoutingException, InvalidCommandException, ResistToChooseCard;
}
