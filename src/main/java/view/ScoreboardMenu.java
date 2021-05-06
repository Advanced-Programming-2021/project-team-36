package view;

import Utils.Router;
import Utils.RoutingException;
import controller.ScoreboardMenuController;
import view.CommandLine.Command;

public class ScoreboardMenu extends BaseMenu {
    ScoreboardMenu() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "scoreboard show",
                command -> {
                    ScoreboardMenuController.showScoreboard(Context.getInstance());
                }
        ));
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<? extends BaseMenu> menu) throws RoutingException {
        if (menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenu.class))
            return new MainMenu();
        if (menu.equals(ScoreboardMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if (!Debugger.getMode())
            throw new RoutingException("menu navigation is not possible");
        if (menu.equals(ProfileMenu.class))
            return new ProfileMenu();
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
        return "Scoreboard Menu";
    }

    @Override
    public void exitMenu() throws RoutingException {
        Router.navigateToMenu(MainMenu.class);
    }
}
