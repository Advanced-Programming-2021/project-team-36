package view;

import Utils.Parser;
import Utils.Router;
import Utils.RoutingException;
import view.CommandLine.Command;

import java.util.Scanner;

public class ImportAndExportMenu extends BaseMenu { // Should this even be a menu?
    ImportAndExportMenu(Scanner scanner){
        super(scanner);
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        // todo complete this
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<?> menu) throws RoutingException {
        if(menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if(menu.equals(MainMenu.class))
            return new MainMenu(scanner);
        if(menu.equals(ImportAndExportMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if (!Debugger.getMode())
            throw new RoutingException("menu navigation is not possible");
        if(menu.equals(ProfileMenu.class))
            return new ProfileMenu(scanner);
        if(menu.equals(ScoreboardMenu.class))
            return new ScoreboardMenu(scanner);
        if(menu.equals(ShopMenu.class))
            return new ShopMenu(scanner);
        if(menu.equals(DeckMenu.class))
            return new DeckMenu(scanner);
        if(menu.equals(DuelMenu.class))
            return new DuelMenu(scanner);
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    protected String getMenuName() {
        return "Import/Export Menu";
    }

    @Override
    public void exitMenu() throws RoutingException {
        Router.navigateToMenu(MainMenu.class);
    }
}
