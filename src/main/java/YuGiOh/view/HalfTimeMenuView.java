package YuGiOh.view;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.menu.HalfTimeMenuController;
import YuGiOh.model.Game;
import YuGiOh.model.ModelException;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.CustomScanner;
import YuGiOh.utils.Debugger;
import YuGiOh.utils.RoutingException;
import YuGiOh.view.CommandLine.Command;
import YuGiOh.view.CommandLine.CommandLine;
import YuGiOh.view.CommandLine.CommandLineException;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import lombok.Getter;

import java.util.List;

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
