package view;

import Utils.Parser;
import Utils.Router;
import Utils.RoutingException;
import view.CommandLine.Command;

import java.util.Scanner;

public class DeckMenu extends BaseMenu {
    DeckMenu(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "deck create [deckName]",
                mp -> {
                    controller.DeckMenu.createDeck(Context.getInstance(), mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck delete [deckName]",
                mp -> {
                    controller.DeckMenu.deleteDeck(Context.getInstance(), mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck set-active [deckName]",
                mp -> {
                    controller.DeckMenu.setActiveDeck(Context.getInstance(), mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck add-card",
                mp -> {
                    controller.DeckMenu.addCardToDeck(Context.getInstance(), Parser.cardParser(mp.get("card")), mp.get("deck"), mp.containsKey("side"));
                },
                Options.card(true),
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck rm-card",
                mp -> {
                    controller.DeckMenu.removeCardFromDeck(Context.getInstance(), Parser.cardParser(mp.get("card")), mp.get("deck"), mp.containsKey("side"));
                },
                Options.card(true),
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    controller.DeckMenu.showAllDecks(Context.getInstance());
                },
                Options.all(true)
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    controller.DeckMenu.showDeck(Context.getInstance(), mp.get("deck"), mp.containsKey("side"));
                },
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    controller.DeckMenu.showAllCards(Context.getInstance());
                },
                Options.cards(true)
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
            return new ShopMenu(scanner);
        if(menu.equals(DeckMenu.class))
            throw new RoutingException("can't navigate to your current menu!");
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
