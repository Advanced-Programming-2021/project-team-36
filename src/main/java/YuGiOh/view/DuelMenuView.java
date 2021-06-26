package YuGiOh.view;

import YuGiOh.model.Game;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.utils.CustomScanner;
import YuGiOh.utils.Debugger;
import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.utils.RoutingException;
import YuGiOh.model.enums.Color;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.ModelException;
import YuGiOh.model.card.Card;
import YuGiOh.view.CommandLine.Command;
import YuGiOh.view.CommandLine.CommandLine;
import YuGiOh.view.CommandLine.CommandLineException;
import YuGiOh.view.cardSelector.CardSelector;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import lombok.Getter;

import java.util.List;

public class DuelMenuView extends BaseMenuView {
    @Getter
    private CardSelector cardSelector;

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
                    DuelMenuController.getInstance().attack(CardSelector.getInstance().getSelectedCard(), Parser.monsterZoneParser(mp.get("number")));
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
        this.cmd.addCommand(new Command(
                "surrender",
                mp -> {
                    DuelMenuController.getInstance().surrender();
                }
        ));
        this.cmd.addCommand(new Command(
                "cheat increase",
                mp -> {
                    DuelMenuController.getInstance().increaseLP(mp.get("lp"));
                },
                Options.lp(true)
        ));
        this.cmd.addCommand(new Command(
                "cheat ultimate cheat",
                mp -> {
                    DuelMenuController.getInstance().ultimateCheat();
                }
        ));
    }

    public boolean askUser(String question, String yes, String no) {
        CustomPrinter.println(String.format("%s(%s/%s)", question, yes, no), Color.Cyan);
        String response = CustomScanner.nextLine();
        while (true) {
            if (response.equalsIgnoreCase(yes))
                return true;
            else if (response.equalsIgnoreCase(no))
                return false;
            else
                CustomPrinter.println(String.format("%s or %s?", yes, no), Color.Cyan);
        }
    }

    public Card askUserToChooseCard(String message, SelectCondition condition) throws ResistToChooseCard {
        CustomPrinter.println(message, Color.Default);
        CardSelector cardSelector = new CardSelector(GameController.instance.getGame());
        CustomPrinter.println("you have to select a card(q to quit)", Color.Cyan);
        String line = CustomScanner.nextLine();

        if (line.equalsIgnoreCase("q"))
            throw new ResistToChooseCard();
        try {
            // todo clean this shit
            CommandLine commandLine = new CommandLine();
            commandLine.addCommand(new Command(
                    "select",
                    mp -> {
                        CardAddress cardAddress = Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent"));
                        Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                        if (card == null)
                            throw new LogicException("no card found in the given position");
                        if (!condition.canSelect(card))
                            throw new LogicException("you can't select this card");
                        cardSelector.selectCard(cardAddress);
                    },
                    Options.monsterZone(true),
                    Options.opponent(false)
            ));
            commandLine.addCommand(new Command(
                    "select",
                    mp -> {
                        CardAddress cardAddress = Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent"));
                        Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                        if (card == null)
                            throw new LogicException("no card found in the given position");
                        if (!condition.canSelect(card))
                            throw new LogicException("you can't select this card");
                        cardSelector.selectCard(cardAddress);
                    },
                    Options.spellZone(true),
                    Options.opponent(false)
            ));
            commandLine.addCommand(new Command(
                    "select",
                    mp -> {
                        CardAddress cardAddress = Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent"));
                        Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                        if (card == null)
                            throw new LogicException("no card found in the given position");
                        if (!condition.canSelect(card))
                            throw new LogicException("you can't select this card");
                        cardSelector.selectCard(cardAddress);
                    },
                    Options.fieldZone(true),
                    Options.opponent(false)
            ));
            commandLine.addCommand(new Command(
                    "select",
                    mp -> {
                        CardAddress cardAddress = Parser.cardAddressParser("hand", mp.get("hand"), false);
                        Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                        if (card == null)
                            throw new LogicException("no card found in the given position");
                        if (!condition.canSelect(card))
                            throw new LogicException("you can't select this card");
                        cardSelector.selectCard(cardAddress);
                    },
                    Options.handZone(true)
            ));
            commandLine.runNextCommand(line);
        } catch (CommandLineException | ParserException | LogicException | ModelException | RoutingException e) {
            CustomPrinter.println(e.getMessage(), Color.Default);
            throw new ResistToChooseCard();
        }
        try {
            return cardSelector.getSelectedCard();
        } catch (LogicException logicException) {
            throw new ResistToChooseCard();
        }
    }

    public int askUserToChooseNumber(String question, int l, int r) {
        CustomPrinter.println(question, Color.Default);
        while (true) {
            String number = CustomScanner.nextLine();
            try {
                int id = Integer.parseInt(number);
                if (l > id || id > r)
                    throw new Exception();
                return id;
            } catch (Exception e) {
                CustomPrinter.println(String.format("number should be from %d to %d", l, r), Color.Cyan);
            }
        }
    }

    public int askUserToChoose(String question, List<String> choices) throws ResistToChooseCard {
        if(choices.isEmpty())
            throw new ResistToChooseCard();
        StringBuilder sb = new StringBuilder(question).append("\n");
        sb.append("enter the number to choose (or -1 to discard)\n");
        int count = 1;
        for(String s : choices)
            sb.append(count + ". " + s);
        CustomPrinter.println(question, Color.Default);
        int ret = -1;
        while (true) {
            String number = CustomScanner.nextLine();
            try {
                ret = Integer.parseInt(number);
                if (ret == -1)
                    break;
                if (1 <= ret && ret <= choices.size())
                    break;
                throw new Exception();
            } catch (Exception e) {
                CustomPrinter.println(String.format("number should be from %d to %d or -1", 1, choices.size()), Color.Cyan);
            }
        }
        if(ret == -1)
            throw new ResistToChooseCard();
        return ret;
    }

    public void startNewGame(Game game) {
        CustomPrinter.println("new round begins!", Color.Red);
        CustomPrinter.println("3 .. 2 .. 1 .. fight!", Color.Red);
        cardSelector = new CardSelector(game);
    }

    public void resetSelector(){
        cardSelector.refresh();
    }

    @Override
    public void runNextCommand() {
        try {
            String line = CustomScanner.nextLine();
            if (Debugger.getCaptureMode())
                Debugger.captureCommand(line);
            this.cmd.runNextCommand(line);
        } catch (CommandLineException | ParserException | ModelException | LogicException | RoutingException e) {
            CustomPrinter.println(e.getMessage(), Color.Red);
        }
    }

    @Override
    protected String getMenuName() {
        return "Duel Menu";
    }
}
