package view;

import controller.menu.ScoreboardMenuController;
import view.CommandLine.Command;

public class ScoreboardMenuView extends BaseMenuView {
    public ScoreboardMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "scoreboard show",
                command -> {
                    ScoreboardMenuController.getInstance().showScoreboard();
                }
        ));
    }

    @Override
    protected String getMenuName() {
        return "Scoreboard Menu";
    }
}
