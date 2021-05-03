package view;

import Utils.Parser;
import view.CommandLine.Command;

import java.util.Scanner;

public class LoginMenu extends BaseMenu {
    public LoginMenu(Scanner scanner){
        super(scanner);
        this.cmd.addCommand(new Command(
                "user login",
                mp -> {
                    controller.LoginMenu.login(Parser.UserParser(mp.get("u")), mp.get("p"));
                },
                Options.username(true),
                Options.password(true)
        ));
        this.cmd.addCommand(new Command(
                "user create",
                mp -> {
                    controller.LoginMenu.createUser(mp.get("u"), mp.get("n"), mp.get("p"));
                },
                Options.username(true),
                Options.nickname(true),
                Options.password(true)
        ));
        this.cmd.addCommand(new Command(
                "user logout",
                mp -> {
                    controller.LoginMenu.logout();
                }
        ));
    }
}
