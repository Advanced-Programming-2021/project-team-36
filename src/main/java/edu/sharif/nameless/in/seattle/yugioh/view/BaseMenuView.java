package edu.sharif.nameless.in.seattle.yugioh.view;

import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.ProgramController;
import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.utils.*;
import edu.sharif.nameless.in.seattle.yugioh.view.CommandLine.Command;
import edu.sharif.nameless.in.seattle.yugioh.view.CommandLine.CommandLineException;
import edu.sharif.nameless.in.seattle.yugioh.view.CommandLine.CommandLine;

abstract public class BaseMenuView {
    protected final CommandLine cmd;

    public BaseMenuView() {
        this.cmd = new CommandLine();
        addCommands();
    }

    public void runNextCommand() {
        try {
            String line = CustomScanner.nextLine();
            if (Debugger.getCaptureMode())
                Debugger.captureCommand(line);
            this.cmd.runNextCommand(line);
        } catch (CommandLineException | ParserException | ModelException | LogicException | RoutingException e) {
            CustomPrinter.println(e.getMessage(), Color.Default);
        }
    }

    protected void addCommands() {
        this.cmd.addCommand(new Command(
                "menu enter [menuName]",
                mp -> {
                    ProgramController.getInstance().navigateToMenu(Parser.menuParser(mp.get("menuName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "menu exit",
                mp -> {
                    ProgramController.getInstance().menuExit();
                }
        ));
        this.cmd.addCommand(new Command(
                "menu show-current",
                mp -> {
                    CustomPrinter.println(getMenuName(), Color.Default);
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
        this.cmd.addCommand(new Command(
                "debug import test",
                mp -> {
                    Debugger.importTest(mp.get("file"), mp.get("count"));
                },
                Options.file(true),
                Options.count(false)
        ));
        this.cmd.addCommand(new Command(
                "debug",
                mp -> {
                    Debugger.setAutomaticSave(mp.get("automatic_save"));
                },
                Options.automatic_save(false)
        ));
        this.cmd.addCommand(new Command(
                "load from database",
                mp -> {
                    DatabaseHandler.loadFromDatabase(mp.get("file"));
                    System.out.println("successfully loaded from database");
                },
                Options.file(false)
        ));
    }

    abstract protected String getMenuName();
}
