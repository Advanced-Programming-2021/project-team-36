package view.CommandLine;

import controller.events.GameEvent;
import view.ParserException;
import Utils.RoutingException;
import controller.LogicException;
import model.ModelException;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;

import java.util.*;

public class Command {
    String usage;
    String[] prefixCommand;
    Options options;
    Handler handler;
    List<String> optionNames;

    private static String[] toStringArray(String str) {
        return Arrays.stream(str.split(" ")).filter(e -> !e.isEmpty()).toArray(String[]::new);
    }

    public Command(String prefixCommand, Handler handler, Option... options) {
        this.prefixCommand = toStringArray(prefixCommand);
        this.usage = prefixCommand;
        this.handler = handler;
        this.options = new Options();
        this.optionNames = new ArrayList<>();
        for (Option option : options) {
            this.options.addOption(option);
            this.optionNames.add(option.getOpt());
        }
    }

    boolean isSpecialItem(String item) {
        return item.matches("\\[.*]");
    }

    String getSpecialItemName(String item) {
        assert isSpecialItem(item);
        return item.substring(1, item.length() - 1);
    }

    boolean initStringMatch(String commandString) {
        String[] commandArray = toStringArray(commandString);
        if (prefixCommand.length > commandArray.length)
            return false;
        for (int i = 0; i < prefixCommand.length; i++) {
            if (!isSpecialItem(prefixCommand[i]) && !prefixCommand[i].equals(commandArray[i]))
                return false;
        }
        return true;
    }

    void tryRunCommand(String commandString) throws CommandLineException, ParserException, ModelException, LogicException, RoutingException, GameEvent {
        if (!initStringMatch(commandString))
            throw new InvalidCommandException();
        CommandLineParser parser = new GnuParser();
        String[] commandArray = toStringArray(commandString);
        String[] splitCommandArray = Arrays.copyOfRange(commandArray, this.prefixCommand.length, commandArray.length);
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, splitCommandArray);
        } catch (Exception e) {
            throw new InvalidCommandException();
        }
        Map<String, String> mp = new HashMap<>();
        for (String optName : optionNames) {
            if (commandLine.hasOption(optName))
                mp.put(optName, commandLine.getOptionValue(optName));
        }
        for (int i = 0; i < prefixCommand.length; i++) {
            if (isSpecialItem(prefixCommand[i])) {
                mp.put(getSpecialItemName(prefixCommand[i]), commandArray[i]);
            }
        }
        handler.run(mp);
    }

    void printHelper() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(usage, options);
    }
}
