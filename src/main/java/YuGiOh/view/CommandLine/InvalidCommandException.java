package YuGiOh.view.CommandLine;

public class InvalidCommandException extends CommandLineException {
    public InvalidCommandException() {
        super("invalid command!");
    }
}