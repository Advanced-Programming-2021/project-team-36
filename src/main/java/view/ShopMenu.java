package view;

import Utils.Parser;
import Utils.Router;
import Utils.RoutingException;
import controller.ShopMenuController;
import view.CommandLine.Command;

public class ShopMenu extends BaseMenu {
    ShopMenu() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "shop buy [cardName]",
                mp -> {
                    ShopMenuController.buyCard(Context.getInstance(), Parser.cardParser(mp.get("cardName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "shop show",
                mp -> {
                    ShopMenuController.showAll(Context.getInstance());
                },
                Options.all(true)
        ));
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<? extends BaseMenu> menu) throws RoutingException {
        if (menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenu.class))
            return new MainMenu();
        if (menu.equals(ShopMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
        if (!Debugger.getMode())
            throw new RoutingException("menu navigation is not possible");
        if (menu.equals(ProfileMenu.class))
            return new ProfileMenu();
        if (menu.equals(ScoreboardMenu.class))
            return new ScoreboardMenu();
        if (menu.equals(DeckMenu.class))
            return new DeckMenu();
        if (menu.equals(ImportAndExportMenu.class))
            return new ImportAndExportMenu();
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    protected String getMenuName() {
        return "Shop Menu";
    }

    @Override
    public void exitMenu() throws RoutingException {
        Router.navigateToMenu(MainMenu.class);
    }
}
