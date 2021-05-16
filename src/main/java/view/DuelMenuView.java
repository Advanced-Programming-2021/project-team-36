package view;

import controller.GameController;
import controller.LogicException;
import controller.ProgramController;
import controller.cardSelector.CardSelector;
import controller.cardSelector.MultiCardSelector;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import controller.events.GameEvent;
import controller.menu.MainMenuController;
import utils.CustomPrinter;
import utils.CustomScanner;
import controller.menu.DuelMenuController;
import model.CardAddress;
import model.ModelException;
import model.card.Card;
import utils.RoutingException;
import view.CommandLine.Command;
import view.CommandLine.CommandLine;
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
        this.cmd.addCommand(new Command(
                "goh khordam",
                mp -> {
                    ProgramController.getInstance().navigateToMenu(MainMenuController.getInstance());
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

    public Card[] askUserToChooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        CustomPrinter.println(message);
        MultiCardSelector multiCardSelector = new MultiCardSelector(GameController.instance.getGame());
        int selected;
        while((selected = multiCardSelector.getSelectedCards().size()) < numberOfCards){
            CustomPrinter.println(String.format("you have to select %d cards(q to quit)", numberOfCards - selected));
            String line = CustomScanner.nextLine();

            if(line.equalsIgnoreCase("q"))
                throw new ResistToChooseCard();
            try {
                // todo clean this shit
                CommandLine commandLine = new CommandLine();
                commandLine.addCommand(new Command(
                        "select",
                        mp -> {
                            CardAddress cardAddress = Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent"));
                            Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                            if(card == null)
                                throw new LogicException("no card found in the given position");
                            if(condition.canSelect(card))
                                throw new LogicException("you can't select this card");
                            MultiCardSelector.getInstance().selectCard(cardAddress);
                        },
                        Options.monsterZone(true),
                        Options.opponent(false)
                ));
                commandLine.addCommand(new Command(
                        "select",
                        mp -> {
                            CardAddress cardAddress = Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent"));
                            Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                            if(card == null)
                                throw new LogicException("no card found in the given position");
                            if(condition.canSelect(card))
                                throw new LogicException("you can't select this card");
                            MultiCardSelector.getInstance().selectCard(cardAddress);
                        },
                        Options.spellZone(true),
                        Options.opponent(false)
                ));
                commandLine.addCommand(new Command(
                        "select",
                        mp -> {
                            CardAddress cardAddress = Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent"));
                            Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                            if(card == null)
                                throw new LogicException("no card found in the given position");
                            if(condition.canSelect(card))
                                throw new LogicException("you can't select this card");
                            MultiCardSelector.getInstance().selectCard(cardAddress);
                        },
                        Options.fieldZone(true),
                        Options.opponent(false)
                ));
                commandLine.addCommand(new Command(
                        "select",
                        mp -> {
                            CardAddress cardAddress = Parser.cardAddressParser("hand", mp.get("hand"), false);
                            Card card = GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
                            if(card == null)
                                throw new LogicException("no card found in the given position");
                            if(condition.canSelect(card))
                                throw new LogicException("you can't select this card");
                            MultiCardSelector.getInstance().selectCard(cardAddress);
                        },
                        Options.handZone(true)
                ));
                commandLine.addCommand(new Command(
                        "select",
                        mp -> {
                            CustomPrinter.println("not implemented yet");
                            // todo implement deselect in multiCard
                        },
                        Options.Deselect(true)
                ));
                commandLine.runNextCommand(line);
            } catch (CommandLineException | ParserException | LogicException | ModelException | GameEvent | RoutingException e) {
                CustomPrinter.println(e.getMessage());
            }
        }
        return (Card[]) MultiCardSelector.getInstance().getSelectedCards().toArray();
    }

    @Override
    protected String getMenuName() {
        return "Duel Menu";
    }
}
