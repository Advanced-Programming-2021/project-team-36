package view;

import Utils.Parser;
import Utils.Router;
import controller.LogicException;
import view.CommandLine.Command;

import java.util.Scanner;

public class LoginMenu extends BaseMenu {
    public LoginMenu(Scanner scanner){
        super(scanner);
        this.cmd.addCommand(new Command(
                "user login",
                mp -> {
                    controller.LoginMenu.login(mp.get("u"), mp.get("p"));
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
                "menu enter [menuName]",
                mp -> {
                    Class<?> menu = Parser.menuParser(mp.get("menuName"));
                    if(menu.equals(LoginMenu.class))
                        throw new LogicException("cant navigate to your current menu!");
                    throw new LogicException("please login first");
                }
        ));
    }
}
