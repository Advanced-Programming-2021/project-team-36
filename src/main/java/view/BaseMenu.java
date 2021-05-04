package view;

import Utils.ParserException;
import controller.LogicException;
import model.ModelException;
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
        } catch (CommandLineException | ParserException | ModelException | LogicException e) {
            System.out.println(e.getMessage());
        }
    }
}
