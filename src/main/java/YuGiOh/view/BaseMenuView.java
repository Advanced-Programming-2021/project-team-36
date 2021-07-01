package YuGiOh.view;

import YuGiOh.controller.LogicException;
import YuGiOh.controller.ProgramController;
import YuGiOh.model.ModelException;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.*;
import YuGiOh.view.CommandLine.Command;
import YuGiOh.view.CommandLine.CommandLine;
import YuGiOh.view.CommandLine.CommandLineException;
import YuGiOh.utils.*;
import YuGiOh.controller.menu.LoginMenuController;
import YuGiOh.utils.DatabaseHandler;
import YuGiOh.utils.Debugger;
import YuGiOh.view.CommandLine.Command;


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
            CustomPrinter.println(e.getMessage(), Color.Red);
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
    }

    abstract public String getMenuName();
}
