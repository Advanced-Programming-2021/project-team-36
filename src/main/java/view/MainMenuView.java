package view;

import Utils.RoutingException;
import controller.MainMenuController;
import view.CommandLine.Command;

public class MainMenuView extends BaseMenuView {
    public MainMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "duel",
                mp -> {
                    MainMenuController.getInstance().startNewDuel(Parser.UserParser(mp.get("second_player")), Parser.RoundParser(mp.get("round")));
                },
                Options.newRound(true),
                Options.secondPlayer(true),
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "duel",
                mp -> {
                    MainMenuController.getInstance().startDuelWithAI(Parser.RoundParser(mp.get("round")));
                },
                Options.newRound(true),
                Options.ai(true),
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "user logout",
                mp -> {
                    MainMenuController.getInstance().logout();
                }
        ));
    }

    @Override
    protected String getMenuName() {
        return "Main Menu";
    }
}
