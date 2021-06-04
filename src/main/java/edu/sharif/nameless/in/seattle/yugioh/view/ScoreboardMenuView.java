package edu.sharif.nameless.in.seattle.yugioh.view;

import edu.sharif.nameless.in.seattle.yugioh.controller.menu.ScoreboardMenuController;
import edu.sharif.nameless.in.seattle.yugioh.view.CommandLine.Command;

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
