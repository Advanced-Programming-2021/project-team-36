package view;

import Utils.Parser;
import Utils.RoutingException;
import controller.MainMenuController;
import view.CommandLine.Command;

import java.util.Scanner;

public class MainMenu extends BaseMenu {
    MainMenu(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void addCommands(){
        super.addCommands();
        this.cmd.addCommand(new Command(
                "duel",
                mp -> {
                    MainMenuController.startNewDuel(Context.getInstance(), Parser.UserParser(mp.get("second_player")), Parser.RoundParser(mp.get("round")));
                },
                Options.newRound(true),
                Options.secondPlayer(true),
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "duel",
                mp -> {
                    MainMenuController.startDuelWithAI(Context.getInstance(), Parser.RoundParser(mp.get("round")));
                },
                Options.newRound(true),
                Options.ai(true),
                Options.round(true)
        ));
        this.cmd.addCommand(new Command(
                "user logout",
                mp -> {
                    MainMenuController.logout(Context.getInstance());
                }
        ));
    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<? extends BaseMenu> menu) throws RoutingException {
        if(menu.equals(LoginMenu.class) && Context.getInstance().isLoggedIn())
            throw new RoutingException("you must logout for that!");
        else if(menu.equals(LoginMenu.class))
            return new LoginMenu(scanner);
        if(menu.equals(MainMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
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
        if(menu.equals(ImportAndExportMenu.class))
            return new ImportAndExportMenu(scanner);
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    public void exitMenu() throws RoutingException {
        MainMenuController.logout(Context.getInstance());
    }
}
