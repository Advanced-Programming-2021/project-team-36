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
import model.card.Monster;
import model.enums.MonsterState;
import model.enums.Phase;
import model.enums.ZoneType;

public abstract class PlayerController {
    @Getter
    Player player;
    Phase previousPhase;

    PlayerController(Player player){
        this.player = player;
    }

    // in one control cycle this must run until one phase!
    abstract public void controlStandbyPhase();
    abstract public void controlMainPhase1();
    abstract public void controlMainPhase2();
    abstract public void controlBattlePhase();
    abstract public boolean askRespondToChain();
    abstract public void doRespondToChain(); // todo check if this action is invalid for chain

    public void summonCard(Monster monster) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (!game.canCardBeSummoned(monster))
            throw new LogicException("you can't summon this card");
        if (game.getCurrentPlayer().getBoard().isMonsterCardZoneFull())
            throw new LogicException("monster card zone is full");

        // todo tell shayan what is this
        if (game.isSummonedInThisTurn())
            throw new LogicException("you already summoned/set on this turn");

        // TODO : monster with higher level than 4
        game.setSummonedInThisTurn(true);
        CustomPrinter.println("summoned successfully");
        Board board = game.getCurrentPlayer().getBoard();
        // todo is 5 hardcoded?
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null) {
                board.addCardToBoard(monster, new CardAddress(ZoneType.HAND, i, false));
                board.getCardsOnHand().remove(monster);
                monster.setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
                new CardSelector(game);
                break;
            }
        }
    }

    public void startChain(){
        ChainController chainController = new ChainController(this);
        chainController.control();
    }

    protected void addEffectToChain(Effect effect){
        GameController.getInstance().getGame().getChain().add(effect);
    }

    public void setCard(Card card) {
        // todo
        // todo you can call startChain here if you want
    }

    public void surrender(){
        // todo
        // todo you can call startChain here if you want
    }

    public void changeCardPosition(Card card, MonsterState monsterState) {
        // todo
        // todo you can call startChain here if you want
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
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        // TODO : check one card don't attack twice in a turn
        // error should be : this card already attacked
        assert myMonster != null && opponentMonster != null;
        opponentMonster.onBeingAttackedByMonster(myMonster).run();
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void directAttack(Card card) {
        // todo
        // todo you can call startChain here if you want
    }

    public void activateEffect(Card card) {
        // todo
        // todo you can call startChain here if you want
    }
}
