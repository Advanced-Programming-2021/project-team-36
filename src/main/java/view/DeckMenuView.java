package view;

import controller.DeckMenuController;
import view.CommandLine.Command;

public class DeckMenuView extends BaseMenuView {
    public DeckMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "deck create [deckName]",
                mp -> {
                    DeckMenuController.getInstance().createDeck(mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck delete [deckName]",
                mp -> {
                    DeckMenuController.getInstance().deleteDeck(DeckMenuController.getInstance().deckParser(mp.get("deckName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck set-active [deckName]",
                mp -> {
                    DeckMenuController.getInstance().setActiveDeck(DeckMenuController.getInstance().deckParser(mp.get("deckName")));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck add-card",
                mp -> {
                    DeckMenuController.getInstance().addCardToDeck(Parser.cardParser(mp.get("card")), DeckMenuController.getInstance().deckParser(mp.get("deck")), mp.containsKey("side"));
                },
                Options.card(true),
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck rm-card",
                mp -> {
                    DeckMenuController.getInstance().removeCardFromDeck(Parser.cardParser(mp.get("card")), DeckMenuController.getInstance().deckParser(mp.get("deck")), mp.containsKey("side"));
                },
                Options.card(true),
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    DeckMenuController.getInstance().showAllDecks();
                },
                Options.all(true)
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    DeckMenuController.getInstance().showDeck(DeckMenuController.getInstance().deckParser(mp.get("deck")), mp.containsKey("side"));
                },
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    DeckMenuController.getInstance().showAllCards();
                },
                Options.cards(true)
        ));
    }

    @Override
    protected String getMenuName() {
        return "Deck Menu";
    }
}
