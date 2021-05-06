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

    public BaseMenu(Scanner scanner) {
        this.scanner = scanner;
        this.cmd = new CommandLine();
        addCommands();
    }

    public void runNextCommand() {
        try {
            String line = scanner.nextLine();
            if (Debugger.getCaptureMode())
                Debugger.captureCommand(line);
            this.cmd.runNextCommand(line);
        } catch (CommandLineException | ParserException | ModelException | LogicException | RoutingException e) {
            System.out.println(e.getMessage());
        }
    }

    protected void addCommands() {
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
        this.cmd.addCommand(new Command(
                "debug",
                mp -> {
                    Debugger.setDebugMode(mp.get("mode"));
                },
                Options.mode(true)
        ));
        this.cmd.addCommand(new Command(
                "debug",
                mp -> {
                    Debugger.setCaptureMode(mp.get("capture"));
                },
                Options.captureMode(true)
        ));

    }

    abstract protected String getMenuName();

    abstract public BaseMenu getNavigatingMenuObject(Class<? extends BaseMenu> menu) throws RoutingException;

    abstract public void exitMenu() throws RoutingException;
}
