package edu.sharif.nameless.in.seattle.yugioh.view.CommandLine;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.utils.RoutingException;
import edu.sharif.nameless.in.seattle.yugioh.view.ParserException;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
    private final List<Command> commandList;

    public CommandLine() {
        commandList = new ArrayList<>();
    }

    public void addCommand(Command command) {
        commandList.add(command);
    }

    public void runNextCommand(String line) throws CommandLineException, ParserException, ModelException, LogicException, RoutingException {
        for (Command command : commandList) {
            try {
                command.tryRunCommand(line);
                return;
            } catch (InvalidCommandException | ResistToChooseCard ignored) {

            }
        }
        // if still alive, then no command has been matched
        for (Command command : commandList) {
            if (command.initStringMatch(line)) {
                command.printHelper();
                return;
            }
        }
        throw new InvalidCommandException();
    }

    public void printAllHelpers() {
        for (Command command : commandList) {
            command.printHelper();
        }
    }
}
