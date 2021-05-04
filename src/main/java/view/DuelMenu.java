package view;

import Utils.Parser;
import org.apache.commons.cli.Option;
import view.CommandLine.Command;

import java.util.Scanner;

public class DuelMenu extends BaseMenu {
    DuelMenu(Scanner scanner) {
        super(scanner);
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    controller.DuelMenu.selectCard(Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent")));
                },
                Options.monsterZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    controller.DuelMenu.selectCard(Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent")));
                },
                Options.spellZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    controller.DuelMenu.selectCard(Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent")));
                },
                Options.fieldZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    controller.DuelMenu.selectCard(Parser.cardAddressParser("hand", mp.get("hand"), mp.containsKey("opponent")));
                },
                Options.handZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    controller.DuelMenu.deselectCard();
                },
                Options.Deselect(true)
        ));
        this.cmd.addCommand(new Command(
                "next phase",
                mp -> {
                    controller.DuelMenu.goNextPhase();
                }
        ));
        this.cmd.addCommand(new Command(
                "summon",
                mp -> {
                    controller.DuelMenu.summonCard(controller.DuelMenu.getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    controller.DuelMenu.setCard(controller.DuelMenu.getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    controller.DuelMenu.changeCardPosition(controller.DuelMenu.getSelectedCard(), Parser.cardStateParser(mp.get("position")));
                },
                Options.cardPosition(true)
        ));

        // todo add regex so that attack [number] and attack direct don't be messed up
        this.cmd.addCommand(new Command(
                "attack direct",
                mp -> {
                    controller.DuelMenu.directAttack(controller.DuelMenu.getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "attack [number]",
                mp -> {
                    controller.DuelMenu.attack(controller.DuelMenu.getSelectedCard(), Parser.monsterPositionIdParser(mp.get("number")));
                }
        ));
        this.cmd.addCommand(new Command(
                "flip-summon",
                mp -> {
                    controller.DuelMenu.flipSummon(controller.DuelMenu.getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "activate effect",
                mp -> {
                    controller.DuelMenu.activateEffect(controller.DuelMenu.getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "show graveyard",
                mp -> {
                    controller.DuelMenu.showGraveYard();
                }
        ));
        this.cmd.addCommand(new Command(
                "card show",
                mp -> {
                    controller.DuelMenu.showCard(controller.DuelMenu.getSelectedCard());
                },
                Options.selected(true)
        ));
    }
}
