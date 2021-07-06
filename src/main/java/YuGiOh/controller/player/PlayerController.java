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
                    actions.add(new Action(
                            new MagicActivation((Magic) magic),
                            ((Magic) magic).activateEffect()
                    ));
                }
            }
        }
        return actions;
    }

    public void summon(Monster monster, int requiredTributes, MonsterState monsterState, boolean specialSummon) throws LogicException, ResistToChooseCard {
        player.getBoard().removeCardIfHas(monster);
        GameController.getInstance().getGame().getOtherPlayer(player).getBoard().removeCardIfHas(monster);
        if (!specialSummon)
            player.setSummonedInLastTurn(true);
        if (requiredTributes > 0)
            tributeMonster(requiredTributes, SelectConditions.myMonsterFromMyMonsterZone(player));
        Board board = player.getBoard();
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null) {
                board.addCardToBoard(monster, new CardAddress(ZoneType.MONSTER, i, player));
                break;
            }
        }

        monster.setMonsterState(monsterState);
        // this sets owner
        monster.readyForBattle(player);
    }

    public void summon(Monster monster, int requiredTributes, boolean specialSummon) throws ResistToChooseCard, LogicException {
        boolean AttackingState = askRespondToQuestion("which position you want to summon?", "defending", "attacking");
        MonsterState monsterState = (AttackingState ? MonsterState.DEFENSIVE_OCCUPIED : MonsterState.OFFENSIVE_OCCUPIED);
        summon(monster, requiredTributes, monsterState, specialSummon);
    }

    public void summon(Monster monster, boolean specialSummon) throws ResistToChooseCard, LogicException {
        summon(monster, monster.getNumberOfRequiredTribute(), specialSummon);
    }

    public void drawCard() throws LogicException {
        Card card = player.getBoard().getMainDeck().getTopCard();
        if (card == null)
            throw new LogicException("There is no card to draw");
        player.getBoard().drawCardFromDeck();
        CustomPrinter.println(String.format("new card added to the hand : <%s>", card.getName()), Color.Blue);
    }

    public void validateSummon(Monster monster, int requiredTributes, SelectCondition condition) throws LogicException {
        if (requiredTributes > 0)
            validateTributeMonster(requiredTributes, condition);
        Board board = player.getBoard();
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null)
                return;
        }
        throw new LogicException("we found no empty position to spawn the monster");
    }

    public Action normalSummonAction(Monster monster, boolean specialSummon) throws ResistToChooseCard {
        return new Action(
                new SummonEvent(player, monster, SummonType.NORMAL),
                () -> {
                    summon(monster, specialSummon);
                    CustomPrinter.println(String.format("<%s> summoned <%s> in <%s> position successfully", player.getUser().getUsername(), monster.getName(), monster.getMonsterState()), Color.Green);
                }
        );
    }

    public Action flipSummonAction(Monster monster) throws ResistToChooseCard {
        return new Action(
                new SummonEvent(player, monster, SummonType.FLIP),
                () -> {
                    monster.changeFromHiddenToOccupiedIfCanEffect().run();
                    player.setSummonedInLastTurn(true);
                    CustomPrinter.println(String.format("<%s> flip summoned successfully", monster.getName()), Color.Green);
                }
        );
    }

    private void validateMainPhase() throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
    }

    private void validateCurrentPlayersCard(Card card) throws LogicException {
        if (!GameController.getInstance().getGame().getCurrentPlayer().getBoard().getAllCards().contains(card))
            throw new LogicException("this card is not yours");
    }

    private void validateCurrentPlayerMonsterZone(Monster monster) throws LogicException {
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("this card is not in your field");
    }

    private void validateNotSummonedInThisTurn() throws LogicException {
        if (player.isSummonedInLastTurn())
            throw new LogicException("you already summoned/set on this turn");
    }

    private void validateHasInHand(Monster monster) throws LogicException {
        if (!player.hasInHand(monster))
            throw new LogicException("you can only summon from your hand");
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
        SummonAction action = new SummonAction(
                new SummonEvent(player, monster, SummonType.NORMAL, MonsterState.DEFENSIVE_HIDDEN)
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

    public void tributeMonster(int count, SelectCondition condition) throws LogicException, ResistToChooseCard {
        GameController gameController = GameController.getInstance();
        validateTributeMonster(count, condition);
        Card[] tributeCards = chooseKCards(String.format("Choose %d cards to tribute", count), count, condition);
        for (Card card : tributeCards)
            gameController.moveCardToGraveYard(card);
//        CustomPrinter.println(String.format("<%s> tribute this monsters: %s", Arrays.toString(Arrays.stream(tributeCards).toArray())), Color.Default);
    }

    public void validateTributeMonster(int count, SelectCondition condition) throws LogicException {
        int goodCards = 0;
        for (Card card : player.getBoard().getAllCards())
            if (condition.canSelect(card))
                goodCards++;
        if (goodCards < count)
            throw new LogicException("there are not enough cards for tribute");
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


    public void attack(Monster attacker, Monster defender) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        MonsterAttackAction action = new MonsterAttackAction(
                new MonsterAttackEvent(attacker, defender),
                (defender).onBeingAttackedByMonster(attacker)
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

    public void directAttack(Monster monster) throws RoundOverExceptionEvent, LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        DirectAttackAction action = new DirectAttackAction(
                new DirectAttackEvent(monster, game.getOtherPlayer(player)),
                () -> {
                    Player defender = game.getOtherPlayer(player);
                    GameController.getInstance().decreaseLifePoint(defender, monster.getAttackDamage(), true);
                    monster.setAllowAttack(false);
                }
        );
        try {
            action.validateEffect();
            CustomPrinter.println(String.format("<%s> declares an direct attack with <%s>", getPlayer().getUser().getUsername(), monster.getName()), Color.Blue);
            startChain(action);
            GameController.getInstance().checkBothLivesEndGame();
        } catch (ValidateResult result) {
            throw new LogicException(result.getMessage());
        }
    }

    public void activateMonsterEffect(Monster monster) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can't activate an effect on this turn");
        if (!player.getBoard().getMonsterCardZone().containsValue(monster) || !monster.getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
            throw new LogicException("only faced up monsters can activate their effect");

        //  throw new LogicException("you have already activated this card on this turn");
        // todo is this handeled?
        if (!monster.canActivateEffect())
            throw new LogicException("you cannot activate this monster now");

        CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", player.getUser().getUsername(), monster.getName()), Color.Blue);
        startChain(
                new Action(
                        new MagicActivation(monster),
                        () -> {
                            monster.activateEffect().run();
                        }
                )
        );
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
