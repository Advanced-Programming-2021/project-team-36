package YuGiOh.archive.view;

import YuGiOh.archive.menu.ScoreboardMenuController;
import YuGiOh.archive.view.CommandLine.Command;

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
    public String getMenuName() {
        return "Scoreboard Menu";
    }
}
