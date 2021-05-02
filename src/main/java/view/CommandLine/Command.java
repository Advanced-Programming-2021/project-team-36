package view.CommandLine;

import org.apache.commons.cli.*;
import org.apache.commons.cli.CommandLine;

import java.util.Arrays;

public class Command {
    String usage;
    String[] prefixCommand;
    Options options;
    Handler handler;

    private static String[] toStringArray(String str){
        return Arrays.stream(str.split(" ")).filter(e -> !e.isEmpty()).toArray(String[]::new);
    }

    public Command(String usage, String prefixCommand){
        this.prefixCommand = toStringArray(prefixCommand);
        this.options = new Options();
        this.usage = usage;
    }

    public Command addOpt(String opt) {
        options.addOption(new Option(opt, "empty description"));
        return this;
    }
    public Command addOpt(String opt, boolean hasArg) {
        options.addOption(new Option(opt, hasArg, "empty description"));
        return this;
    }
    public Command addOpt(String opt, String longOpt, boolean hasArg) {
        options.addOption(opt, longOpt, hasArg, "empty description");
        return this;
    }
    public Command addRequiredOpt(String opt) {
        Option option = new Option(opt, "empty description");
        option.setRequired(true);
        options.addOption(option);
        return this;
    }
    public Command addRequiredOpt(String opt, boolean hasArg) {
        Option option = new Option(opt, hasArg, "empty description");
        option.setRequired(true);
        options.addOption(option);
        return this;
    }
    public Command addRequiredOpt(String opt, String longOpt, boolean hasArg) {
        Option option = new Option(opt, longOpt, hasArg, "empty description");
        option.setRequired(true);
        options.addOption(option);
        return this;
    }

    public Command addHandler(Handler handler){
        this.handler = handler;
        return this;
    }

    boolean initStringMatch(String commandString) {
        String[] commandArray = toStringArray(commandString);
        if (prefixCommand.length > commandArray.length)
            return false;
        for (int i = 0; i < prefixCommand.length; i++) {
            if (!prefixCommand[i].equals(commandArray[i]))
                return false;
        }
        return true;
    }
    void tryRunCommand(String commandString) throws InvalidCommandException {
        if (!initStringMatch(commandString))
            throw new InvalidCommandException();
        CommandLineParser parser = new GnuParser();
        String[] commandArray = toStringArray(commandString);
        String[] splitCommandArray = Arrays.copyOfRange(commandArray, this.prefixCommand.length, commandArray.length);
        try {
            handler.run(parser.parse(options, splitCommandArray));
        } catch (Exception e){
            throw new InvalidCommandException();
        }
    }
    void printHelper(){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(usage, options);
    }
}
