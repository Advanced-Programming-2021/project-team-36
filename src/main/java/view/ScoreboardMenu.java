package view;

import Utils.Router;
import Utils.RoutingException;
import controller.ScoreboardMenuController;
import view.CommandLine.Command;

import java.util.Scanner;

public class ScoreboardMenu extends BaseMenu {
    ScoreboardMenu(Scanner scanner) {
        super(scanner);
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
        if(menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if(menu.equals(MainMenu.class))
            return new MainMenu(scanner);
        if(menu.equals(ProfileMenu.class))
            return new ProfileMenu(scanner);
        if(menu.equals(ScoreboardMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if(menu.equals(ShopMenu.class))
            return new ShopMenu(scanner);
        if(menu.equals(DeckMenu.class))
            return new DeckMenu(scanner);
        if(menu.equals(DuelMenu.class))
            return new DuelMenu(scanner);
        if(menu.equals(ImportAndExportMenu.class))
            return new ImportAndExportMenu(scanner);
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    public void exitMenu() throws RoutingException {
        Router.navigateToMenu(MainMenu.class);
    }
}
