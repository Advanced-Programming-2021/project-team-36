package controller.player;

import controller.cardSelector.Conditions;
import controller.menu.DuelMenuController;
import model.card.action.Action;
import model.card.action.MonsterAttackEvent;
import model.card.action.DirectAttackEvent;
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

public abstract class PlayerController {
    @Getter
    Player player;
    Phase previousPhase;

    PlayerController(Player player) {
        this.player = player;
    }

    // in one control cycle this must run until one phase!
    abstract public void controlStandbyPhase();

    abstract public void controlMainPhase1();

    abstract public void controlMainPhase2();

    abstract public void controlBattlePhase();

    abstract public boolean askRespondToChain();

    abstract public void doRespondToChain(); // todo check if this action is invalid for chain
    abstract public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard;

    public void canSummonOrSetMonster(Card card) throws LogicException {
        Game game = GameController.instance.getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (!game.canCardBeSummoned((Monster) card))
            throw new LogicException("you can't summon this card");
        if (game.getCurrentPlayer().getBoard().isMonsterCardZoneFull())
            throw new LogicException("monster card zone is full");
        if (game.isSummonedInThisTurn())
            throw new LogicException("you already summoned/set on this turn");
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
        CustomPrinter.println("summoned successfully");
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
    }


    public void setMonster(Monster monster) throws LogicException, ResistToChooseCard {
        canSummonOrSetMonster(monster);
        addMonsterToBoard(monster, MonsterState.DEFENSIVE_HIDDEN);
    }

    public void tributeMonster(int count) throws LogicException, ResistToChooseCard {
        if (player.getBoard().getMonsterCardZone().size() < count)
            throw new LogicException("there are not enough cards for tribute");
        Card[] tributeCards = chooseKCards(String.format("Choose %d cards to tribute", count), count, Conditions.myMonsterFromMonsterZone);
        for (Card card : tributeCards)
            GameController.getInstance().moveCardToGraveYard(card);
    }

    public void setMagic(Card card) throws LogicException {
        if (!player.hasInHand(card))
            throw new LogicException("you can't set this card");
        if (!GameController.getInstance().getGame().getPhase().isMainPhase())
            throw new LogicException("you can't do this action in this phase");
        if (!((Magic) card).getIcon().equals(Icon.FIELD) && player.getBoard().getMagicCardZone().size() == 5)
            throw new LogicException("spell card zone is full");
        Magic magic = (Magic) card;
        addMagicToBoard(magic, MagicState.HIDDEN);
        // todo
        // todo you can call startChain here if you want
    }

    public void surrender() {
        // todo
        // todo you can call startChain here if you want
    }

    public void changeMonsterPosition(Monster monster, MonsterState monsterState) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || monster.getMonsterState().equals(monsterState))
            throw new LogicException("this card is already in the wanted position (maybe it's defensive hidden)");

        monster.setMonsterState(monsterState);
        CustomPrinter.println("monster card position changed successfully");
    }

    public void flipSummon(Monster monster) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can't change this card position");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (!monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || game.isSummonedInThisTurn())
            throw new LogicException("you can't flip summon this card");

        monster.setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
        GameController.getInstance().getGame().setSummonedInThisTurn(true);
        CustomPrinter.println("flip summoned successfully");
        // todo
        // todo you can call startChain here if you want
    }

    public void ritualSummon(Card card) throws LogicException {
        // todo
        // todo you can call startChain here if you want
    }

    public void canAttack(Monster monster) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!player.getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can’t attack with this monster");
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        if (hasAttackedByCard(monster))
            throw new LogicException("this card already attacked");
    }

    public void attack(Monster myMonster, Monster opponentMonster) throws LogicException, GameOverEvent {
        canAttack(myMonster);

        Game game = GameController.getInstance().getGame();
        // TODO : check one card don't attack twice in a turn
        // error should be : this card already attacked
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

        Game game = GameController.getInstance().getGame();
        startChain(
                new Action(
                        new DirectAttackEvent(monster, game.getOpponentPlayer()),
                        monster.directAttack(this.player)
                )
        );
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void activateEffect(Card card) {

        // todo
        // todo you can call startChain here if you want
    }

    public void startChain(Action action) throws GameOverEvent {
        ChainController chainController = new ChainController(this, action);
        chainController.control(action);
    }

    protected void addActionToChain(Action action) {
        GameController.getInstance().getGame().getChain().add(action);
    }

    public boolean hasAttackedByCard(Monster monster) {
        return !monster.isAllowAttack();
    }
}
