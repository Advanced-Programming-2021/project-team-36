package view.CommandLine;

import Utils.ParserException;
import Utils.RoutingException;
import controller.LogicException;
import model.ModelException;

import java.util.Map;

public interface Handler {
    void run(Map<String, String> cmd) throws ParserException, ModelException, LogicException, RoutingException;
}
