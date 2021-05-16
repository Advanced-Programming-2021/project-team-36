package controller.player;

import controller.cardSelector.Conditions;
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
import model.card.Effect;
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
        addMonsterToBoard(monster, MonsterState.OFFENSIVE_OCCUPIED);
    }


    public void setMonster(Monster monster) throws LogicException, ResistToChooseCard {
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

    public void changeMonsterPosition(Monster monster, MonsterState monsterState) {
        monster.setMonsterState(monsterState);
        CustomPrinter.println("monster card position changed successfully");
    }

    public void flipSummon(Monster monster) {
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

    public void attack(Monster myMonster, Monster opponentMonster) throws LogicException, GameOverEvent {
        Game game = GameController.getInstance().getGame();
        // TODO : check one card don't attack twice in a turn
        // error should be : this card already attacked
        startChain(opponentMonster.onBeingAttackedByMonster(myMonster));
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void directAttack(Monster monster) throws GameOverEvent {
        Game game = GameController.getInstance().getGame();
        startChain(GameController.getInstance().onDirectAttack(this, monster));
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void activateEffect(Card card) {

        // todo
        // todo you can call startChain here if you want
    }

    public void startChain(Effect effect) throws GameOverEvent {
        ChainController chainController = new ChainController(this, effect);
        chainController.control();
    }

    protected void addEffectToChain(Effect effect) {
        GameController.getInstance().getGame().getChain().add(effect);
    }

    public boolean hasAttackedByCard(Monster monster) {
        return !monster.isAllowAttack();
    }
}
