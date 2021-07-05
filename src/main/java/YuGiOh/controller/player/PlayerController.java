package YuGiOh.controller.player;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.action.*;
import YuGiOh.model.card.event.*;
import YuGiOh.model.card.event.MonsterAttackEvent;
import YuGiOh.model.enums.*;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.model.card.Spell;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.controller.ChainController;
import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import lombok.Getter;
import YuGiOh.model.Board;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerController {
    @Getter
    Player player;

    PlayerController(Player player) {
        this.player = player;
    }

    abstract public void controlMainPhase1();

    abstract public void controlMainPhase2();

    abstract public void controlBattlePhase();

    abstract public boolean askRespondToChain();

    abstract public boolean askRespondToQuestion(String question, String yes, String no);

    abstract public void doRespondToChain() throws ResistToChooseCard; // todo check if this action is invalid for chain

    abstract public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard;

    abstract public Monster[] chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) throws ResistToChooseCard;


    public List<Action> listOfAvailableActionsInResponse() {
        int previousSpeed = Math.max(GameController.getInstance().getGame().getChain().peek().getEvent().getSpeed(), 2);
        List<Action> actions = new ArrayList<>();
        for (Card magic : player.getBoard().getAllCardsOnBoard()) {
            if (magic instanceof Magic) {
                if (((Magic) magic).canActivateEffect() && previousSpeed <= magic.getSpeed()) {
                    actions.add(new MagicActivationAction(
                            new MagicActivation((Magic) magic),
                            ((Magic) magic).activateEffect()
                    ));
                }
            }
        }
        return actions;
    }

    public void drawCard() throws LogicException {
        Card card = player.getBoard().getMainDeck().getTopCard();
        if (card == null)
            throw new LogicException("There is no card to draw");
        player.getBoard().drawCardFromDeck();
        CustomPrinter.println(String.format("new card added to the hand : <%s>", card.getName()), Color.Blue);
    }

    public void normalSummon(Monster monster) throws LogicException, ResistToChooseCard {
        SummonAction action = new SummonAction(
                new SummonEvent(player, monster, SummonType.NORMAL)
        );
        try {
            action.validateEffect();
            startChain(action);
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void specialSummon(Monster monster) throws LogicException, ResistToChooseCard {
        SummonAction action = monster.specialSummonAction();
        try {
            action.validateEffect();
            startChain(monster.specialSummonAction());
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        } catch (NullPointerException ignored) {
        }
    }

    public void setMonster(Monster monster) throws LogicException, ResistToChooseCard {
        SetMonsterAction action = new SetMonsterAction(
                new SetMonster(player, monster)
        );
        try {
            action.validateEffect();
            startChain(action);
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }


    public void flipSummon(Monster monster) throws LogicException, ResistToChooseCard {
        FlipSummonAction action = new FlipSummonAction(
                new FlipSummonEvent(player, monster)
        );
        try {
            action.validateEffect();
            startChain(action);
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void setMagic(Magic magic) throws LogicException, ResistToChooseCard {
        SetMagicAction action = new SetMagicAction(
                new SetMagic(magic),
                () -> {
                    player.getBoard().removeFromHand(magic);
                    player.getBoard().addMagic(magic);
                    magic.setMagicState(MagicState.HIDDEN);
                    CustomPrinter.println(String.format("<%s> set magic <%s> successfully", player.getUser().getUsername(), magic.getName()), Color.Green);
                }
        );
        try {
            action.validateEffect();
            startChain(action);
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void surrender() throws RoundOverExceptionEvent {
        Game game = GameController.instance.getGame();
        game.getCurrentPlayer().setLifePoint(0);
        throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, game.getCurrentPlayer(), game.getOpponentPlayer(), game.getOpponentPlayer().getLifePoint());
    }

    public void changeMonsterPosition(Monster monster, MonsterState monsterState) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (monster.getMonsterState().equals(monsterState))
            throw new LogicException("this card is already in the wanted position");
        if (monsterState.equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new LogicException("it's pointless to hide your card");
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new LogicException("you should flip summon it");
        monster.setMonsterState(monsterState);
        CustomPrinter.println(String.format("<%s>'s <%s>'s position changed to %s", getPlayer().getUser().getUsername(), monster.getName(), monsterState), Color.Green);
    }


    public void attack(Card attacker, Card defender) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        MonsterAttackAction action = new MonsterAttackAction(
                new MonsterAttackEvent(attacker, defender),
                ((Monster) defender).onBeingAttackedByMonster((Monster) attacker)
        );
        try {
            action.validateEffect();
            CustomPrinter.println(String.format("<%s> declares an attack with <%s> to <%s>'s <%s>", getPlayer().getUser().getUsername(), attacker.getName(), defender.getOwner().getUser().getUsername(), defender.getName()), Color.Blue);
            startChain(action);
            GameController.getInstance().checkBothLivesEndGame();
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void directAttack(Card card) throws RoundOverExceptionEvent, LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        DirectAttackAction action = new DirectAttackAction(
                new DirectAttackEvent(card, game.getOtherPlayer(player)),
                () -> {
                    Monster attacker = (Monster) card;
                    Player defender = game.getOtherPlayer(player);
                    GameController.getInstance().decreaseLifePoint(defender, attacker.getAttackDamage(), true);
                    attacker.setAllowAttack(false);
                }
        );
        try {
            action.validateEffect();
            CustomPrinter.println(String.format("<%s> declares an direct attack with <%s>", getPlayer().getUser().getUsername(), card.getName()), Color.Blue);
            startChain(action);
            GameController.getInstance().checkBothLivesEndGame();
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void activateMonsterEffect(Monster monster) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        MonsterActivationAction action = new MonsterActivationAction(
                new MagicActivation(monster),
                monster.activateEffect()
        );
        try {
            action.validateEffect();
            CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", player.getUser().getUsername(), monster.getName()), Color.Blue);
            startChain(action);
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void activateSpellEffect(Spell spell) throws LogicException, ResistToChooseCard {
        MagicActivationAction action = new MagicActivationAction(
                new MagicActivation(spell),
                () -> {
                    player.getBoard().removeFromHand(spell);
                    player.getBoard().addMagic(spell);
                    spell.setMagicState(MagicState.OCCUPIED);
                    spell.readyForBattle(player);
                    spell.activateEffect().run();
                }
        );
        try {
            action.validateEffect();
            CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", player.getUser().getUsername(), spell.getName()), Color.Blue);
            startChain(action);
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void startChain(Action action) throws RoundOverExceptionEvent, ResistToChooseCard {
        ChainController chainController = new ChainController(this, action);
        chainController.control();
    }

    protected void addActionToChain(Action action) {
        CustomPrinter.println(String.format("<%s>: I add an action to the chain. It's activation question was: <%s>", player.getUser().getNickname(), action.getEvent().getActivationQuestion()), Color.Purple);
        GameController.getInstance().getGame().getChain().add(action);
    }

    public void refresh() {
        player.setSummonedInLastTurn(false);
        for (Card card : player.getBoard().getAllCards())
            card.startOfNewTurn();
    }
}
