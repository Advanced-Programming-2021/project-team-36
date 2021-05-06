package view;

import Utils.Parser;
import Utils.Router;
import Utils.RoutingException;
import controller.DuelMenuController;
import view.CommandLine.Command;

public class DuelMenu extends BaseMenu {
    DuelMenu() {
        super();
    }

    @Override
    protected void addCommands() {
        super.addCommands();
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.selectCard(Context.getInstance(), Parser.cardAddressParser("monster", mp.get("monster"), mp.containsKey("opponent")));
                },
                Options.monsterZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.selectCard(Context.getInstance(), Parser.cardAddressParser("spell", mp.get("spell"), mp.containsKey("opponent")));
                },
                Options.spellZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.selectCard(Context.getInstance(), Parser.cardAddressParser("field", mp.get("field"), mp.containsKey("opponent")));
                },
                Options.fieldZone(true),
                Options.opponent(false)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.selectCard(Context.getInstance(), Parser.cardAddressParser("hand", mp.get("hand"), mp.containsKey("opponent")));
                },
                Options.handZone(true)
        ));
        this.cmd.addCommand(new Command(
                "select",
                mp -> {
                    DuelMenuController.deselectCard(Context.getInstance());
                },
                Options.Deselect(true)
        ));
        this.cmd.addCommand(new Command(
                "next phase",
                mp -> {
                    DuelMenuController.goNextPhase(Context.getInstance());
                }
        ));
        this.cmd.addCommand(new Command(
                "summon",
                mp -> {
                    DuelMenuController.summonCard(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()));
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    DuelMenuController.setCard(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()));
                }
        ));
        this.cmd.addCommand(new Command(
                "set",
                mp -> {
                    DuelMenuController.changeCardPosition(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()), Parser.cardStateParser(mp.get("position")));
                },
                Options.cardPosition(true)
        ));

        // todo add regex so that attack [number] and attack direct don't be messed up
        this.cmd.addCommand(new Command(
                "attack direct",
                mp -> {
                    DuelMenuController.directAttack(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()));
                }
        ));
        this.cmd.addCommand(new Command(
                "attack [number]",
                mp -> {
                    DuelMenuController.attack(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()), Parser.monsterPositionIdParser(mp.get("number")));
                }
        ));
        this.cmd.addCommand(new Command(
                "flip-summon",
                mp -> {
                    DuelMenuController.flipSummon(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()));
                }
        ));
        this.cmd.addCommand(new Command(
                "activate effect",
                mp -> {
                    DuelMenuController.activateEffect(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()));
                }
        ));
        this.cmd.addCommand(new Command(
                "show graveyard",
                mp -> {
                    DuelMenuController.showGraveYard(Context.getInstance());
                }
        ));
        this.cmd.addCommand(new Command(
                "card show",
                mp -> {
                    DuelMenuController.showCard(Context.getInstance(), DuelMenuController.getSelectedCard(Context.getInstance()));
                },
                Options.selected(true)
        ));

    }

    @Override
    public BaseMenu getNavigatingMenuObject(Class<? extends BaseMenu> menu) throws RoutingException {
        // this is strange here
        // todo move if only we finished the game

        if (menu.equals(LoginMenu.class))
            throw new RoutingException("you must logout for that!");
        if (menu.equals(MainMenu.class))
            return new MainMenu();
        if (Debugger.getMode())
            throw new RoutingException("you cannot navigate out of an ongoing game");
        throw new RoutingException("menu navigation is not possible");
    }

    @Override
    protected String getMenuName() {
        return "Duel Menu";
    }

    @Override
    public void exitMenu() throws RoutingException {
        // todo decide to delete the game from context or not
        Router.navigateToMenu(MainMenu.class);
    }
}
