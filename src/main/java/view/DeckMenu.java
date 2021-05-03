package view;

import view.CommandLine.Command;

import java.util.ConcurrentModificationException;
import java.util.Scanner;

public class DeckMenu extends BaseMenu {
    DeckMenu(Scanner scanner) {
        super(scanner);
        this.cmd.addCommand(new Command(
                "deck create [deckName]",
                mp -> {
                    controller.DeckMenu.createDeck(mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck delete [deckName]",
                mp -> {
                    controller.DeckMenu.deleteDeck(mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck set-active [deckName]",
                mp -> {
                    controller.DeckMenu.setActiveDeck(mp.get("deckName"));
                }
        ));
        this.cmd.addCommand(new Command(
                "deck add-card",
                mp -> {
                    controller.DeckMenu.addCardToDeck(mp.get("card"), mp.get("deck"), mp.containsKey("side"));
                },
                Options.card(true),
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck rm-card",
                mp -> {
                    controller.DeckMenu.removeCardFromDeck(mp.get("card"), mp.get("deck"), mp.containsKey("side"));
                },
                Options.card(true),
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    controller.DeckMenu.showAllDecks();
                },
                Options.all(true)
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    controller.DeckMenu.showDeck(mp.get("deck"), mp.containsKey("side"));
                },
                Options.deck(true),
                Options.side()
        ));
        this.cmd.addCommand(new Command(
                "deck show",
                mp -> {
                    controller.DeckMenu.showAllCards();
                },
                Options.cards(true)
        ));
    }
}
