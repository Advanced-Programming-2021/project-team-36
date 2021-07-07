package YuGiOh.archive.view.CommandLine;

import YuGiOh.controller.LogicException;
import YuGiOh.model.ModelException;
import YuGiOh.utils.RoutingException;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;
import YuGiOh.archive.view.ParserException;

import java.util.Map;

public interface Handler {
    void run(Map<String, String> cmd) throws ParserException, ModelException, LogicException, RoutingException, InvalidCommandException, ResistToChooseCard;
}
