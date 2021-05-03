package view;

import view.CommandLine.Command;

import java.util.Scanner;

public class ScoreboardMenu extends BaseMenu {
    ScoreboardMenu(Scanner scanner) {
        super(scanner);
        this.cmd.addCommand(new Command(
                "scoreboard show",
                command -> {
                    controller.ScoreboardMenu.showScoreboard();
                }
        ));
    }
}
