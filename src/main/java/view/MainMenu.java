package view;

import Utils.Parser;
import view.CommandLine.Command;

import java.util.Scanner;


public class MainMenu extends BaseMenu {
    MainMenu(Scanner scanner) {
        super(scanner);
        this.cmd.addCommand(new Command(
                "duel",
                mp -> {
                    controller.MainMenu.startNewDuel(Parser.UserParser(mp.get("second-player")), Parser.RoundParser("round"));
                },
                Options.newRound(true),
                Options.secondPlayer(true),
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "duel",
                mp -> {
                    controller.MainMenu.startDuelWithAI(Parser.RoundParser("round"));
                },
                Options.newRound(true),
                Options.ai(true),
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "user logout",
                mp -> {
                    controller.MainMenu.logout();
                }
        ));

    }
}
