package view;

import Utils.Router;
import Utils.RoutingException;
import controller.LoginMenuController;
import view.CommandLine.Command;

import java.util.Scanner;

public class LoginMenu extends BaseMenu {
    public LoginMenu(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void addCommands(){
        super.addCommands();
        this.cmd.addCommand(new Command(
                "user login",
                mp -> {
                    LoginMenuController.login(Context.getInstance(), mp.get("u"), mp.get("p"));
                },
                Options.username(true),
                Options.password(true)
        ));
        this.cmd.addCommand(new Command(
                "user create",
                mp -> {
                    LoginMenuController.createUser(Context.getInstance(), mp.get("u"), mp.get("n"), mp.get("p"));
                },
                Options.username(true),
                Options.nickname(true),
                Options.password(true)
        ));
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<?> menu) throws RoutingException {
        if(menu.equals(LoginMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if(menu.equals(ImportAndExportMenu.class))
            return new ImportAndExportMenu(scanner);
        if(Context.getInstance().isLoggedIn() && menu.equals(MainMenu.class))
            return new MainMenu(scanner);
        throw new RoutingException("please login first");
    }

    @Override
    public void exitMenu() {
        Router.gameOver();
    }
}
