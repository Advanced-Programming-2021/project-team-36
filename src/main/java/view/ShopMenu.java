package view;

import Utils.Parser;
import Utils.Router;
import Utils.RoutingException;
import view.CommandLine.Command;

import java.util.Scanner;

public class ShopMenu extends BaseMenu {
    ShopMenu(Scanner scanner){
        super(scanner);
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "shop buy [cardName]",
                mp -> {
                    controller.ShopMenu.buyCard(Context.getInstance(), Parser.cardParser(mp.get("cardName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "shop show",
                mp -> {
                    controller.ShopMenu.showAll(Context.getInstance());
                },
                Options.all(true)
        ));
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<?> menu) throws RoutingException {
        if(menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if(menu.equals(MainMenu.class))
            return new MainMenu(scanner);
        if(menu.equals(ProfileMenu.class))
            return new ProfileMenu(scanner);
        if(menu.equals(ScoreboardMenu.class))
            return new ScoreboardMenu(scanner);
        if(menu.equals(ShopMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
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
