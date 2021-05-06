package view;

import Utils.Router;
import Utils.RoutingException;
import controller.ProfileMenuController;
import view.CommandLine.Command;

public class ProfileMenu extends BaseMenu {
    ProfileMenu() {
        super();
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
        if (menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenu.class))
            return new MainMenu();
        if (menu.equals(ProfileMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if (!Debugger.getMode())
            throw new RoutingException("menu navigation is not possible");
        if (menu.equals(ScoreboardMenu.class))
            return new ScoreboardMenu();
        if (menu.equals(ShopMenu.class))
            return new ShopMenu();
        if (menu.equals(DeckMenu.class))
            return new DeckMenu();
        if (menu.equals(ImportAndExportMenu.class))
            return new ImportAndExportMenu();
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
