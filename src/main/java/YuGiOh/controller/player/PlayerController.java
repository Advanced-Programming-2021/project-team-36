package YuGiOh.controller.player;

import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.action.*;
import YuGiOh.model.card.event.*;
import YuGiOh.model.card.event.MonsterAttackEvent;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Spell;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.controller.ChainController;
import YuGiOh.controller.GameController;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import lombok.Getter;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class PlayerController {
    @Getter
    Player player;

    PlayerController(Player player) {
        this.player = player;
    }

    abstract public void controlMainPhase1();

    abstract public void controlMainPhase2();

    abstract public void controlBattlePhase();

    abstract public CompletableFuture<Boolean> askRespondToChain();

    abstract public CompletableFuture<Boolean> askRespondToQuestion(String question, String yes, String no);

    abstract public CompletableFuture<Void> doRespondToChain();

    abstract public CompletableFuture<List<Card>> chooseKCards(String message, int numberOfCards, SelectCondition condition);

    abstract public CompletableFuture<List<Monster>> chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition);

    public List<Action> listOfAvailableActionsInResponse() {
        int previousSpeed = Math.max(GameController.getInstance().getGame().getChain().peek().getEvent().getSpeed(), 2);
        List<Action> actions = new ArrayList<>();
        for (Card card : player.getBoard().getAllCardsOnBoard()) {
            if (card instanceof Magic) {
                if (card.canActivateEffect() && previousSpeed <= card.getSpeed()) {
                    Action action = new MagicActivationAction((Magic) card);
                    if(action.isValid())
                        actions.add(action);
                }
            }
        }
        return actions;
    }

    public CompletableFuture<Void> drawCard() throws RoundOverExceptionEvent {
        return new DrawCardAction(player).runEffect();
    }
    public CompletableFuture<Void> surrender() throws RoundOverExceptionEvent {
        return new SurrenderAction().runEffect();
    }

    public NormalSummonAction normalSummon(Monster monster) {
        return new NormalSummonAction(player, monster);
    }
    public SpecialSummonAction specialSummon(Monster monster) {
        return monster.specialSummonAction();
    }
    public SetMonsterAction setMonster(Monster monster) {
        return new SetMonsterAction(new SetMonster(player, monster));
    }
    public FlipSummonAction flipSummon(Monster monster) {
        return new FlipSummonAction(new FlipSummonEvent(player, monster));
    }
    public SetMagicAction setMagic(Magic magic) {
        return new SetMagicAction(new SetMagic(player, magic));
    }
    public ChangeMonsterPositionAction changeMonsterPosition(Monster monster, MonsterState monsterState){
        return new ChangeMonsterPositionAction(player, monster, monsterState);
    }
    public MonsterAttackAction attack(Monster attacker, Monster defender) {
        return new MonsterAttackAction(new MonsterAttackEvent(attacker, defender));
    }
    public DirectAttackAction directAttack(Monster monster) {
        return new DirectAttackAction(new DirectAttackEvent(monster, GameController.getInstance().getGame().getOtherPlayer(player)));
    }
    public MonsterActivationAction activateMonsterEffect(Monster monster) {
        return new MonsterActivationAction(new MonsterActivation(monster));
    }
    public SpellActivationAction activateSpellEffect(Spell spell) {
        return new SpellActivationAction(spell);
    }

    public CompletableFuture<Void> startChain(Action action) throws RoundOverExceptionEvent, GameException {
        action.validateEffect();
        ChainController chainController = new ChainController();
        addActionToChain(action);
        return chainController.control();
    }

    protected void addActionToChain(Action action) {
        // todo. maybe we can handle this better. no null in event!
        CustomPrinter.println(String.format("<%s>: I add an action to the chain. It's activation question was: <%s>", player.getUser().getNickname(), action.getEvent().getActivationQuestion()), Color.Purple);
        GameController.getInstance().getGame().getChain().add(action);
    }

    public void refresh() {
        player.setSummonedInLastTurn(false);
        for (Card card : player.getBoard().getAllCards())
            card.startOfNewTurn();
    }
}
