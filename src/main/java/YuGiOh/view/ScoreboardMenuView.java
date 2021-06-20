package YuGiOh.view;

import YuGiOh.controller.menu.ScoreboardMenuController;
import YuGiOh.view.CommandLine.Command;

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
