package view;

import Utils.CustomPrinter;
import Utils.CustomScanner;
import controller.CardSelector;
import controller.menu.DuelMenuController;
import view.CommandLine.Command;

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
                    CardSelector.getInstance().selectCard(Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent")));
                },
                Options.monsterZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    CardSelector.getInstance().selectCard(Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent")));
                },
                Options.spellZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    CardSelector.getInstance().selectCard(Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent")));
                },
                Options.fieldZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    CardSelector.getInstance().selectCard(Parser.cardAddressParser("hand", mp.get("hand"), false));
                },
                Options.handZone(true)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    CardSelector.getInstance().deselectCard();
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
                    DuelMenuController.getInstance().summonCard(CardSelector.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    DuelMenuController.getInstance().setCard(CardSelector.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    DuelMenuController.getInstance().changeCardPosition(CardSelector.getInstance().getSelectedCard(), Parser.cardStateParser(mp.get("position")));
                },
                Options.cardPosition(true)
        ));

        // todo add regex so that attack [number] and attack direct don't be messed up
        this.cmd.addCommand(new Command(
                "attack direct",
                mp -> {
                    DuelMenuController.getInstance().directAttack(CardSelector.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "attack [number]",
                mp -> {
                    DuelMenuController.getInstance().attack(CardSelector.getInstance().getSelectedCard(), Parser.monsterPositionIdParser(mp.get("number")));
                }
        ));
        this.cmd.addCommand(new Command(
                "flip-summon",
                mp -> {
                    DuelMenuController.getInstance().flipSummon(CardSelector.getInstance().getSelectedCard());
                }
        ));
        this.cmd.addCommand(new Command(
                "activate effect",
                mp -> {
                    DuelMenuController.getInstance().activateEffect(CardSelector.getInstance().getSelectedCard());
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
                    CardSelector.getInstance().showSelectedCard();
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

    public boolean askUser(String question){
        CustomPrinter.println(question + "(yes/no)");
        String response = CustomScanner.nextLine();
        while(true) {
            if (response.equalsIgnoreCase("yes"))
                return true;
            else if (response.equalsIgnoreCase("no"))
                return false;
            else
                CustomPrinter.println("yes or no?");
        }
    }

    @Override
    protected String getMenuName() {
        return "Duel Menu";
    }
}
