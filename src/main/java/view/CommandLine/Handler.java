package view.CommandLine;

import controller.cardSelector.ResistToChooseCard;
import view.ParserException;
import utils.RoutingException;
import controller.LogicException;
import model.ModelException;

import java.util.Map;

public interface Handler {
    void run(Map<String, String> cmd) throws ParserException, ModelException, LogicException, RoutingException, InvalidCommandException, ResistToChooseCard;
}
