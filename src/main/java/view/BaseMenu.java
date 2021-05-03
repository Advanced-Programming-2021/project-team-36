package view;

import Utils.ParserException;
import view.CommandLine.CommandLine;
import view.CommandLine.CommandLineException;

import java.util.Scanner;

public class BaseMenu {
    private final Scanner scanner;
    protected final CommandLine cmd;

    public BaseMenu(Scanner scanner){
        this.scanner = scanner;
        this.cmd = new CommandLine();
    }
    public void runNextCommand(){
        try {
            String line = scanner.nextLine();
            this.cmd.runNextCommand(line);
        } catch (CommandLineException | ParserException e) {
            System.out.println(e.getMessage());
        }
    }
}
