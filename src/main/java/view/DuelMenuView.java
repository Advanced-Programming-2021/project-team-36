package view;

import Utils.*;
import controller.DuelMenuController;
import controller.LogicException;
import model.ModelException;
import model.Player.AIPlayer;
import view.CommandLine.Command;
import view.CommandLine.CommandLineException;

public class DuelMenuView extends BaseMenuView {
    public DuelMenuView() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.getInstance().selectCard(Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent")));
                },
                Options.monsterZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.getInstance().selectCard(Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent")));
                },
                Options.spellZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.getInstance().selectCard(Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent")));
                },
                Options.fieldZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.getInstance().selectCard(Parser.cardAddressParser("hand", mp.get("hand"), mp.containsKey("opponent")));
                },
                Options.handZone(true)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.getInstance().deselectCard();
                },
                Options.Deselect(true)
        ));
        this.cmd.addCommand(new Command(
                "next phase",
                mp -> {
                    DuelMenuController.getInstance().goNextPhase();
                }
        ));
        this.cmd.addCommand(new Command(
                "summon",
                mp -> {
                    DuelMenuController.getInstance().summonCard();
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    DuelMenuController.getInstance().setCard(DuelMenuController.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    DuelMenuController.getInstance().changeCardPosition(DuelMenuController.getInstance().getSelectedCard(), Parser.cardStateParser(mp.get("position")));
                },
                Options.cardPosition(true)
        ));

        // todo add regex so that attack [number] and attack direct don't be messed up
        this.cmd.addCommand(new Command(
                "attack direct",
                mp -> {
                    DuelMenuController.getInstance().directAttack(DuelMenuController.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "attack [number]",
                mp -> {
                    DuelMenuController.getInstance().attack(Parser.monsterPositionIdParser(mp.get("number")));
                }
        ));
        this.cmd.addCommand(new Command(
                "flip-summon",
                mp -> {
                    DuelMenuController.getInstance().flipSummon(DuelMenuController.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "activate effect",
                mp -> {
                    DuelMenuController.getInstance().activateEffect(DuelMenuController.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "show graveyard",
                mp -> {
                    DuelMenuController.getInstance().showGraveYard();
                }
        ));
        this.cmd.addCommand(new Command(
                "card show",
                mp -> {
                    DuelMenuController.getInstance().showSelectedCard();
                },
                Options.selected(true)
        ));
        this.cmd.addCommand(new Command(
                "show hand",
                mp -> {
                    DuelMenuController.getInstance().showHand();
                }
        ));
        this.cmd.addCommand(new Command(
                "show board",
                mp -> {
                    DuelMenuController.getInstance().showBoard();
                }
        ));
    }

    @Override
    protected String getMenuName() {
        return "Duel Menu";
    }
}
