package view;

import Utils.Parser;
import Utils.ParserException;
import Utils.Router;
import Utils.RoutingException;
import controller.LogicException;
import model.ModelException;
import view.CommandLine.Command;
import view.CommandLine.CommandLine;
import view.CommandLine.CommandLineException;

import java.util.Scanner;

abstract public class BaseMenu {
    protected final Scanner scanner;
    protected final CommandLine cmd;

    public BaseMenu(Scanner scanner){
        this.scanner = scanner;
        this.cmd = new CommandLine();
        addCommands();
    }
    public void runNextCommand() {
        try {
            String line = scanner.nextLine();
            this.cmd.runNextCommand(line);
        } catch (CommandLineException | ParserException | ModelException | LogicException | RoutingException e) {
            System.out.println(e.getMessage());
        }
    }
    protected void addCommands(){
        this.cmd.addCommand(new Command(
                "menu enter [menuName]",
                mp -> {
                    Router.navigateToMenu(Parser.menuParser(mp.get("menuName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "menu exit",
                mp -> {
                    exitMenu();
                }
        ));
        this.cmd.addCommand(new Command(
                "menu show-current",
                mp -> {
                    System.out.println(getMenuName());
                }
        ));
        this.cmd.addCommand(new Command(
                "help",
                mp -> {
                    this.cmd.printAllHelpers();
                }
        ));
    }
    protected String getMenuName(){
        return this.getClass().getName().replaceAll(".*\\.", "").replaceAll("Menu", "");
        // this is a hack. fix it! todo
    }
    abstract public BaseMenu getNavigatingMenuObject(Class<?> menu) throws RoutingException;
    abstract public void exitMenu() throws RoutingException;
}
