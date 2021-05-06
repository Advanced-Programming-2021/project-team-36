package view;

import Utils.Router;
import Utils.RoutingException;
import controller.ProfileMenuController;
import view.CommandLine.Command;

import java.util.Scanner;

public class ProfileMenu extends BaseMenu {
    ProfileMenu(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "profile change",
                mp -> {
                    ProfileMenuController.changeNickname(Context.getInstance(), mp.get("nickname"));
                },
                Options.nickname(true)
        ));
        this.cmd.addCommand(new Command(
                "profile change",
                mp -> {
                    ProfileMenuController.changePassword(Context.getInstance(), mp.get("current"), mp.get("new"));
                },
                Options.requirePassword(true),
                Options.currentPassword(true),
                Options.newPassword(true)
        ));
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<? extends BaseMenu> menu) throws RoutingException {
        if(menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenu.class))
            return new MainMenu(scanner);
        if (menu.equals(ProfileMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if (!Debugger.getMode())
            throw new RoutingException("menu navigation is not possible");
        if (menu.equals(ScoreboardMenu.class))
            return new ScoreboardMenu(scanner);
        if (menu.equals(ShopMenu.class))
            return new ShopMenu(scanner);
        if (menu.equals(DeckMenu.class))
            return new DeckMenu(scanner);
        if (menu.equals(ImportAndExportMenu.class))
            return new ImportAndExportMenu(scanner);
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    protected String getMenuName() {
        return "Profile Menu";
    }

    @Override
    public void exitMenu() throws RoutingException {
        Router.navigateToMenu(MainMenu.class);
    }
}
