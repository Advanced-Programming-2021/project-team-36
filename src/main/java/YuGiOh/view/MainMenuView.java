package YuGiOh.view;

import YuGiOh.controller.menu.MainMenuController;
import YuGiOh.utils.DatabaseHandler;
import YuGiOh.view.CommandLine.Command;

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
                "duel",
                mp -> {
                    MainMenuController.getInstance().startDuelAiWithAI(Parser.RoundParser(mp.get("round")));
                },
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "user logout",
                mp -> {
                    MainMenuController.getInstance().logout();
                }
        ));
        this.cmd.addCommand(new Command(
                "save to database",
                mp -> {
                    DatabaseHandler.saveToDatabase(mp.get("file"));
                },
                Options.file(false)
        ));
    }

    @Override
    protected String getMenuName() {
        return "Main Menu";
    }
}
