package controller.player;

import utils.CustomPrinter;
import controller.CardSelector;
import controller.ChainController;
import controller.GameController;
import controller.LogicException;
import controller.events.GameOver;
import lombok.Getter;
import model.Board;
import model.CardAddress;
import model.Game;
import model.Player.Player;
import model.card.Card;
import model.card.Effect;
import model.card.Magic;
import model.card.Monster;
import model.enums.MonsterState;
import model.enums.Phase;
import model.enums.ZoneType;

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

    public void addMonsterToBoard(Monster monster, MonsterState monsterState) throws LogicException {
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
                board.addCardToBoard(monster, new CardAddress(ZoneType.HAND, i, false));
                board.getCardsOnHand().remove(monster);
                monster.setMonsterState(monsterState);
                break;
            }
        }
        CustomPrinter.println("summoned successfully");
    }

    public void summonCard(Monster monster) throws LogicException {
        addMonsterToBoard(monster, MonsterState.OFFENSIVE_OCCUPIED);
    }


    public void setMonster(Monster monster) throws LogicException {
        addMonsterToBoard(monster, MonsterState.DEFENSIVE_HIDDEN);
    }

    public void tributeMonster(int count) throws LogicException {
        if (player.getBoard().getMonsterCardZone().size() < count)
            throw new LogicException("there are not enough cards for tribute");
        // todo : after selector here should implement

    }

    public void setMagic(Magic magic) {
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

    public void flipSummon(Card card) {

        // todo
        // todo you can call startChain here if you want
    }

    public void ritualSummon(Card card) {
        // todo
        // todo you can call startChain here if you want
    }

    public void attack(Monster myMonster, Monster opponentMonster) throws LogicException, GameOver {
        Game game = GameController.getInstance().getGame();
        // TODO : check one card don't attack twice in a turn
        // error should be : this card already attacked
        startChain(opponentMonster.onBeingAttackedByMonster(myMonster));
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void directAttack(Monster monster) throws GameOver {
        Game game = GameController.getInstance().getGame();
        startChain(GameController.getInstance().onDirectAttack(this, monster));
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void activateEffect(Card card) {

        // todo
        // todo you can call startChain here if you want
    }

    public void startChain(Effect effect) {
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
