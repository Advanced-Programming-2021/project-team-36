package view.CommandLine;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
    private final List<Command> commandList;

    public CommandLine(){
        commandList = new ArrayList<>();
    }
    public void addCommand(Command command){
        commandList.add(command);
    }
    public void runNextCommand(String line) throws InvalidCommandException {
        for(Command command : commandList){
            try {
                command.tryRunCommand(line);
                return;
            } catch (InvalidCommandException ignored){

            }
        }
        // if still alive, then no command has been matched
        for(Command command : commandList){
            if (command.initStringMatch(line)) {
                command.printHelper();
                return;
            }
        }
        throw new InvalidCommandException();
    }
}