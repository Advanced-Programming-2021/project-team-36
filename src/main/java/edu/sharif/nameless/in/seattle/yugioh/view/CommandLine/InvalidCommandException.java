package edu.sharif.nameless.in.seattle.yugioh.view.CommandLine;

public class InvalidCommandException extends CommandLineException {
    public InvalidCommandException() {
        super("invalid command!");
    }
}