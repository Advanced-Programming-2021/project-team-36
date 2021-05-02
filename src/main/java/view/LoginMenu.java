package view;

import view.CommandLine.Command;

import java.util.Scanner;

public class LoginMenu extends BaseMenu {
    public LoginMenu(Scanner scanner){
        super(scanner);
        this.cmd.addCommand(new Command("login", "user login")
                .addRequiredOpt("u", "username", true)
                .addRequiredOpt("p", "password", true)
                .addHandler(command -> {
                    controller.LoginMenu.login(command.getOptionValue("u"), command.getOptionValue("p"));
                }));
    }
}
