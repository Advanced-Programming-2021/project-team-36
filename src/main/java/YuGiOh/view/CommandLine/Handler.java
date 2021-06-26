package YuGiOh.view.CommandLine;

import YuGiOh.controller.LogicException;
import YuGiOh.model.ModelException;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.ParserException;

import java.util.Map;

public interface Handler {
    void run(Map<String, String> cmd) throws ParserException, ModelException, LogicException, RoutingException, InvalidCommandException, ResistToChooseCard;
}
