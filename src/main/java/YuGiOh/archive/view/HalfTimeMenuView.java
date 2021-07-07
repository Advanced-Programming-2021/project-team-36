package YuGiOh.archive.view;

import YuGiOh.controller.LogicException;
import YuGiOh.archive.menu.HalfTimeMenuController;
import YuGiOh.model.ModelException;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.CustomScanner;
import YuGiOh.utils.Debugger;
import YuGiOh.utils.RoutingException;
import YuGiOh.archive.view.CommandLine.Command;
import YuGiOh.archive.view.CommandLine.CommandLineException;

public class HalfTimeMenuView extends BaseMenuView {

    public HalfTimeMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();

        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    HalfTimeMenuController.getInstance().showDeck(mp.containsKey("side"));
                },
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck add-card",
                mp -> {
                    HalfTimeMenuController.getInstance().addCardToDeck(Parser.cardParser(mp.get("card")));
                },
                Options.card(true)
        ));
        this.cmd.addCommand(new Command(
                "deck rm-card",
                mp -> {
                    HalfTimeMenuController.getInstance().removeCardFromDeck(Parser.cardParser(mp.get("card")));
                },
                Options.card(true)
        ));
        this.cmd.addCommand(new Command(
                "ready",
                mp -> {
                    HalfTimeMenuController.getInstance().ready();
                }
        ));
    }

    @Override
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

    @Override
    public String getMenuName() {
        return "Half-Time Menu";
    }
}
