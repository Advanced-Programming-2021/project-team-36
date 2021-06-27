package YuGiOh.view.cardSelector;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.CommandLine.Command;
import YuGiOh.view.CommandLine.CommandLine;
import YuGiOh.view.Options;
import YuGiOh.view.Parser;
import lombok.Getter;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;

public class CardSelector {
    private CardAddress selectedCardAddress;

    public void selectCard(CardAddress cardAddress) throws LogicException {
        if (GameController.getInstance().getGame().getCardByCardAddress(cardAddress) == null)
            throw new LogicException("no card found in the given position");
        selectedCardAddress = cardAddress;
        CustomPrinter.println("card selected", Color.Green);
    }

    public void deselectCard() throws LogicException {
        if (selectedCardAddress == null)
            throw new LogicException("no card is selected");
        selectedCardAddress = null;
        CustomPrinter.println("card deselected", Color.Green);
    }

    public Card getSelectedCard() throws LogicException {
        if (!isAnyCardSelected())
            throw new LogicException("no card is selected");
        return GameController.getInstance().getGame().getCardByCardAddress(selectedCardAddress);
    }

    public void showSelectedCard() throws LogicException {
        CustomPrinter.println(getSelectedCard().toString(), Color.Default);
    }

    public boolean isAnyCardSelected() {
        return selectedCardAddress != null;
    }

    public void refresh() {
        selectedCardAddress = null;
    }

    public static void addSelectingCommandsToCmd(CommandLine cmd, CardActionRunnable cardActionRunnable, CardSelector selectorToDeselect){
        cmd.addCommand(new Command(
                "select",
                mp -> {
                    cardActionRunnable.run(Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent")));
                },
                Options.monsterZone(true),
                Options.opponent(false)
        ));
        cmd.addCommand(new Command(
                "select",
                mp -> {
                    cardActionRunnable.run(Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent")));
                },
                Options.spellZone(true),
                Options.opponent(false)
        ));
        cmd.addCommand(new Command(
                "select",
                mp -> {
                    cardActionRunnable.run(Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent")));
                },
                Options.fieldZone(true),
                Options.opponent(false)
        ));
        cmd.addCommand(new Command(
                "select",
                mp -> {
                    cardActionRunnable.run(Parser.cardAddressParser("hand", mp.get("hand"), false));
                },
                Options.handZone(true)
        ));
        if(selectorToDeselect != null) {
            cmd.addCommand(new Command(
                    "select",
                    mp -> {
                        selectorToDeselect.deselectCard();
                    },
                    Options.Deselect(true)
            ));
        }
    }

    public interface CardActionRunnable{
        void run(CardAddress cardAddress) throws LogicException;
    }
}