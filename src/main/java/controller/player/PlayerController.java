package controller.player;

import controller.cardSelector.Conditions;
import model.card.Spell;
import model.card.action.*;
import model.enums.*;
import utils.CustomPrinter;
import controller.ChainController;
import controller.GameController;
import controller.LogicException;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import controller.events.GameOverEvent;
import lombok.Getter;
import model.Board;
import model.CardAddress;
import model.Game;
import model.Player.Player;
import model.card.Card;
import model.card.Magic;
import model.card.Monster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class PlayerController {
    @Getter
    Player player;

    PlayerController(Player player) {
        this.player = player;
    }

    // in one control cycle this must run until one phase!
    abstract public void controlStandbyPhase();

    abstract public void controlMainPhase1();

    abstract public void controlMainPhase2();

    abstract public void controlBattlePhase();

    abstract public boolean askRespondToChain();

    abstract public boolean askYesNoQuestion(String question);

    abstract public int askIntegerQuestion(String question, int l, int r);

    abstract public void doRespondToChain() throws ResistToChooseCard; // todo check if this action is invalid for chain
    abstract public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard;
    abstract public Monster[] chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) throws ResistToChooseCard;


    public List<Action> listOfAvailableActionsInResponse(){
        int previousSpeed = Math.max(GameController.getInstance().getGame().getChain().peek().getEvent().getSpeed(), 2);
        List<Action> actions = new ArrayList<>();
        for (Card magic : player.getBoard().getAllCardsOnBoard()) {
            if (magic instanceof Magic) {
                if(((Magic) magic).canActivateEffect() && previousSpeed <= magic.getSpeed()){
                    actions.add(new Action(
                            new MagicActivation((Magic) magic),
                            ((Magic) magic).activateEffect()
                    ));
                }
            }
        }
        return actions;
    }

    public void canSummonOrSetMonster(Card card) throws LogicException {
        Game game = GameController.instance.getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (!game.canCardBeSummoned(card))
            throw new LogicException("you can't summon this card");
        if (game.getCurrentPlayer().getBoard().isMonsterCardZoneFull())
            throw new LogicException("monster card zone is full");
        if (game.isSummonedInThisTurn())
            throw new LogicException("you already summoned/set on this turn");
        if (!player.hasInHand(card))
            throw new LogicException("you can only summon from your hand");
    }

    public void addMonsterToBoard(Monster monster, MonsterState monsterState) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        if (monster.getLevel() >= 5 && monster.getLevel() <= 6)
            tributeMonster(1);
        else if (monster.getLevel() >= 7 && monster.getLevel() <= 8)
            tributeMonster(2);
        game.setSummonedInThisTurn(true);
        Board board = game.getCurrentPlayer().getBoard();
        // todo is 5 hardcoded?
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null) {
                board.addCardToBoard(monster, new CardAddress(ZoneType.MONSTER, i, false));
                board.getCardsOnHand().remove(monster);
                monster.setMonsterState(monsterState);
                break;
            }
        }
        if (monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED))
            CustomPrinter.println("summoned successfully");
        else
            CustomPrinter.println("set successfully");
    }

    public void addMagicToBoard(Magic magic, MagicState magicState) {
        Game game = GameController.getInstance().getGame();
        Board board = game.getCurrentPlayer().getBoard();
        for (int i = 1; i <= 5; i++) {
            if (board.getMagicCardZone().get(i) == null) {
                board.addCardToBoard(magic, new CardAddress(ZoneType.MAGIC, i, false));
                board.getCardsOnHand().remove(magic);
                magic.setMagicState(magicState);
                break;
            }
        }
        CustomPrinter.println("set successfully");
    }

    public void summonCard(Monster monster) throws LogicException, ResistToChooseCard {
        canSummonOrSetMonster(monster);
        addMonsterToBoard(monster, MonsterState.OFFENSIVE_OCCUPIED);
        CustomPrinter.println(String.format("I summon monster %s ", monster.getName()));
    }


    public void setMonster(Monster monster) throws LogicException, ResistToChooseCard {
        canSummonOrSetMonster(monster);
        addMonsterToBoard(monster, MonsterState.DEFENSIVE_HIDDEN);
        CustomPrinter.println(String.format("I set monster %s ", monster.getName()));
    }

    public void tributeMonster(int count) throws LogicException, ResistToChooseCard {
        if (player.getBoard().getMonsterCardZone().size() < count)
            throw new LogicException("there are not enough cards for tribute");
        Card[] tributeCards = chooseKCards(String.format("Choose %d cards to tribute", count), count, Conditions.myMonsterFromMyMonsterZone(player));
        for (Card card : tributeCards)
            GameController.getInstance().moveCardToGraveYard(card);
        CustomPrinter.println(String.format("I tribute this monsters: %s", Arrays.toString(Arrays.stream(tributeCards).toArray())));
    }

    public void setMagic(Magic magic) throws LogicException {
        if (!player.hasInHand(magic))
            throw new LogicException("you can't set this card");
        if (!GameController.getInstance().getGame().getPhase().isMainPhase())
            throw new LogicException("you can't do this action in this phase");
        if (!magic.getIcon().equals(Icon.FIELD) && player.getBoard().getMagicCardZone().size() == 5)
            throw new LogicException("spell card zone is full");
        // todo field spell
        addMagicToBoard(magic, MagicState.HIDDEN);
        CustomPrinter.println(String.format("I set magic %s", magic.getName()));
    }

    public void surrender() throws GameOverEvent {
        Game game = GameController.instance.getGame();
        throw new GameOverEvent(GameResult.NOT_DRAW, game.getCurrentPlayer(), game.getOpponentPlayer());
    }

    public void changeMonsterPosition(Monster monster, MonsterState monsterState) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || monster.getMonsterState().equals(monsterState))
            throw new LogicException("this card is already in the wanted position (maybe it's defensive hidden)");
        if (monsterState.equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new LogicException("You can't change position to defensive hidden!");
        monster.setMonsterState(monsterState);
        CustomPrinter.println("monster card position changed successfully");
        CustomPrinter.println(String.format("I change my %s position to %s", monster.getName(), monsterState));
    }

    public void flipSummon(Monster monster) throws LogicException {
        // todo : should startChain
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (!monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || game.isSummonedInThisTurn())
            throw new LogicException("you can't flip summon this card");
        monster.setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
        GameController.getInstance().getGame().setSummonedInThisTurn(true);
        CustomPrinter.println(String.format("I flip summon my %s", monster.getName()));
        CustomPrinter.println("flip summoned successfully");
    }

    public void canAttack(Monster monster) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!player.getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can’t attack with this monster");
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        if (hasAttackedByCard(monster))
            throw new LogicException("this card already attacked");
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || monster.getMonsterType().equals(MonsterState.DEFENSIVE_OCCUPIED))
            throw new LogicException("monster is in defensive position");
    }

    public void attack(Monster myMonster, Monster opponentMonster) throws LogicException, GameOverEvent {
        canAttack(myMonster);
        if (!myMonster.isAllowAttack())
            throw new LogicException("this card already attacked");
        if (!GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getMonsterCardZone().containsValue(opponentMonster))
            throw new LogicException("you can't attack that monster");
        CustomPrinter.println(String.format("I declare an attack with my %s to your %s", myMonster.getName(), opponentMonster.getName()));
        startChain(
                new Action(
                        new MonsterAttackEvent(myMonster, opponentMonster),
                        opponentMonster.onBeingAttackedByMonster(myMonster)
                )
        );
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void directAttack(Monster monster) throws GameOverEvent, LogicException {
        canAttack(monster);
        if (GameController.getInstance().getOtherPlayerController(this).getPlayer().getBoard().getMonsterCardZone().size() > 0)
            throw new LogicException("you can’t attack the opponent directly");
        if (!monster.isAllowAttack())
            throw new LogicException("this card already attacked");

        CustomPrinter.println(String.format("I declare a direct attack to you with my %s to you", monster.getName()));

        Game game = GameController.getInstance().getGame();
        startChain(
                new Action(
                        new DirectAttackEvent(monster, game.getOpponentPlayer()),
                        monster.directAttack(this.player)
                )
        );
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void activateEffect(Spell spell) throws LogicException, GameOverEvent {
        Game game = GameController.getInstance().getGame();
        if(!player.getBoard().getMagicCardZone().containsValue(spell))
            throw new LogicException("you can't activate this card!");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can't activate an effect on this turn");
        if (spell.getMagicState().equals(MagicState.OCCUPIED))
            throw new LogicException("you have already activated this card");
        if (getPlayer().hasInHand(spell) && !spell.getIcon().equals(Icon.FIELD))
            throw new LogicException("spell card zone is full");
        if (!spell.canActivateEffect())
            throw new LogicException("preparations of this spell are not done yet");

        CustomPrinter.println(String.format("I want to activate the effect of %s", spell.getName()));

        startChain(
                new Action(
                        new MagicActivation(spell),
                        spell.activateEffect()
                )
        );
    }

    public void startChain(Action action) throws GameOverEvent {
        ChainController chainController = new ChainController(this, action);
        chainController.control();
    }

    protected void addActionToChain(Action action) {
        CustomPrinter.println(String.format("%s: I add an action to the chain. It's activation question was: %s", player.getUser().getNickname(), action.getEvent().getActivationQuestion()));
        GameController.getInstance().getGame().getChain().add(action);
    }

    public boolean hasAttackedByCard(Monster monster) {
        return !monster.isAllowAttack();
    }
}
