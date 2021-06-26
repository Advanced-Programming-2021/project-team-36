package YuGiOh.controller.player;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.action.*;
import YuGiOh.model.enums.*;
import YuGiOh.view.cardSelector.Conditions;
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
import java.util.Optional;

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
        Game game = GameController.getInstance().getGame();
        int previousSpeed = Math.max(game.getChain().peek().getEvent().getSpeed(), 2);
        List<Action> actions = new ArrayList<>();
        for (Card magic : player.getBoard().getAllCardsOnBoard()) {
            if (magic instanceof Magic) {
                boolean isItUsed = false;
                for (Action action : game.getChain())
                    if (action.getEvent() instanceof MagicActivation && ((MagicActivation)action.getEvent()).getCard().equals(magic)) {
                        isItUsed = true;
                        break;
                    }
                if (isItUsed)
                    break;
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
        if(!specialSummon)
            player.setSummonedInLastTurn(true);

        if (requiredTributes > 0)
            tributeMonster(requiredTributes, Conditions.myMonsterFromMyMonsterZone(player));
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

    public void addMagicToBoard(Magic magic, MagicState magicState) {
        Game game = GameController.getInstance().getGame();
        Board board = game.getCurrentPlayer().getBoard();
        if (magic.getIcon().equals(Icon.FIELD)) {
            board.addCardToBoard((Card) magic, new CardAddress(ZoneType.FIELD, 1, player));
        } else {
            for (int i = 1; i <= 5; i++) {
                if (board.getMagicCardZone().get(i) == null) {
                    board.addCardToBoard(magic, new CardAddress(ZoneType.MAGIC, i, player));
                    break;
                }
            }
        }

        magic.setMagicState(magicState);
        // this sets owner
        magic.readyForBattle(player);
    }

    public Action normalSummonAction(Monster monster, boolean specialSummon) throws ResistToChooseCard {
        return new Action(
                        new SummonEvent(monster, SummonType.NORMAL),
                        () -> {
                            summon(monster, specialSummon);
                            CustomPrinter.println(String.format("<%s> summoned <%s> in <%s> position successfully", player.getUser().getUsername(), monster.getName(), monster.getMonsterState()), Color.Green);
                        }
                );
    }

    public Action flipSummonAction(Monster monster) throws ResistToChooseCard {
        return new Action(
                        new SummonEvent(monster, SummonType.FLIP),
                        () -> {
                            monster.changeFromHiddenToOccupiedIfCanEffect().run();
                            player.setSummonedInLastTurn(true);
                            CustomPrinter.println(String.format("<%s> flip summoned successfully", monster.getName()), Color.Green);
                        }
        );
    }

    public Action setMonsterAction(Monster monster) throws ResistToChooseCard {
        return new Action(
                        new SetMonster(monster),
                        () -> {
                            summon(monster, monster.getNumberOfRequiredTribute(), MonsterState.DEFENSIVE_HIDDEN, false);
                            CustomPrinter.println(String.format("<%s> set monster <%s> successfully", player.getUser().getUsername(), monster.getName()), Color.Green);
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
        validateMainPhase();
        validateCurrentPlayersCard(monster);
        validateNotSummonedInThisTurn();
        validateHasInHand(monster);
        validateSummon(monster, monster.getNumberOfRequiredTribute(), Conditions.myMonsterFromMyMonsterZone(player));
        startChain(normalSummonAction(monster, false));
    }

    public void specialSummon(Monster monster) throws  LogicException, ResistToChooseCard {
        validateMainPhase();
        validateCurrentPlayersCard(monster);
        monster.validateSpecialSummon();
        startChain(monster.specialSummonAction());
    }

    public void setMonster(Monster monster) throws LogicException, ResistToChooseCard {
        validateMainPhase();
        validateSummon(monster, monster.getNumberOfRequiredTribute(), Conditions.myMonsterFromMyMonsterZone(player));
        startChain(setMonsterAction(monster));
    }


    public void flipSummon(Monster monster) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        validateCurrentPlayerMonsterZone(monster);
        validateMainPhase();
        if (!monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || game.getCurrentPlayer().isSummonedInLastTurn())
            throw new LogicException("you can't flip summon this card");
        startChain(flipSummonAction(monster));
    }

    public void tributeMonster(int count, SelectCondition condition) throws LogicException, ResistToChooseCard {

        validateTributeMonster(count, condition);
        Card[] tributeCards = chooseKCards(String.format("Choose %d cards to tribute", count), count, condition);
        for (Card card : tributeCards)
            moveCardToGraveYard(card);
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

    public void moveCardToGraveYard(Card card) {
        player.moveCardToGraveYard(card);
        if (card instanceof Monster) {
            for (int i = 1; i <= 5; i++) {
                Magic magic = player.getBoard().getMagicCardZone().get(i);
                if (magic != null && magic.isFacedUp() && magic.getIcon().equals(Icon.EQUIP) && magic.getEquippedMonster().equals(card))
                    moveCardToGraveYard(magic);
            }
        }
        if (card instanceof Spell)
            ((Spell) card).onMovingToGraveYard();
        CustomPrinter.println(String.format("<%s>'s Card <%s> moved to graveyard", player.getUser().getUsername(), card.getName()), Color.Blue);
        if (card instanceof Monster)
            for (int i = 1; i <= 5; i++)
                if (player.getBoard().getMagicCardZone().get(i) != null)
                    player.getBoard().getMagicCardZone().get(i).onDestroyMyMonster();
    }

    public void setMagic(Magic magic) throws LogicException, ResistToChooseCard {
        if (!player.hasInHand(magic))
            throw new LogicException("you can't set this card");
        if (!GameController.getInstance().getGame().getPhase().isMainPhase())
            throw new LogicException("you can't do this action in this phase");
        if (magic.getIcon().equals(Icon.FIELD))
            throw new LogicException("field cards can't be set");
        if (player.getBoard().getMagicCardZone().size() == 5)
            throw new LogicException("spell card zone is full");

        startChain(
                new Action(
                        new SetMagic(magic),
                        () -> {
                            addMagicToBoard(magic, MagicState.HIDDEN);
                            player.getBoard().getCardsOnHand().remove((Card) magic);
                            CustomPrinter.println(String.format("<%s> set magic <%s> successfully", player.getUser().getUsername(), magic.getName()), Color.Green);
                        }
                )
        );
    }

    public void surrender() throws RoundOverExceptionEvent {
        Game game = GameController.instance.getGame();
        player.setLifePoint(0);
        throw new RoundOverExceptionEvent(GameResult.NOT_DRAW, player, game.getOtherPlayer(player), game.getOtherPlayer(player).getLifePoint());
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

    public void canAttack(Monster monster) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!player.getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("you can’t attack with this monster");
        if (!game.getPhase().equals(Phase.BATTLE_PHASE))
            throw new LogicException("you can’t do this action in this phase");
        if (hasAttackedByCard(monster))
            throw new LogicException("this card already attacked");
        if (monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || monster.getMonsterState().equals(MonsterState.DEFENSIVE_OCCUPIED))
            throw new LogicException("monster is in defensive position");
    }

    public void attack(Monster monster, Monster opponentMonster) throws LogicException, RoundOverExceptionEvent, ResistToChooseCard {
        canAttack(monster);
        if (!monster.isAllowAttack())
            throw new LogicException("this card already attacked");
        if (!GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getMonsterCardZone().containsValue(opponentMonster))
            throw new LogicException("you can't attack that monster");
        CustomPrinter.println(String.format("<%s> declares an attack with <%s> to <%s>'s <%s>", getPlayer().getUser().getUsername(), monster.getName(), opponentMonster.owner.getUser().getUsername(), opponentMonster.getName()), Color.Blue);
        startChain(
                new Action(
                        new MonsterAttackEvent(monster, opponentMonster),
                        opponentMonster.onBeingAttackedByMonster(monster)
                )
        );
        GameController.getInstance().checkBothLivesEndGame();
    }

    public void directAttack(Monster monster) throws RoundOverExceptionEvent, LogicException, ResistToChooseCard {
        canAttack(monster);
        if (GameController.getInstance().getOtherPlayerController(this).getPlayer().getBoard().getMonsterCardZone().size() > 0)
            throw new LogicException("you can’t attack the opponent directly");
        if (!monster.isAllowAttack())
            throw new LogicException("this card already attacked");

        CustomPrinter.println(String.format("<%s> declares an direct attack with <%s>", getPlayer().getUser().getUsername(), monster.getName()), Color.Blue);

        Game game = GameController.getInstance().getGame();
        startChain(
                new Action(
                        new DirectAttackEvent(monster, game.getOpponentPlayer()),
                        monster.directAttack(this.player)
                )
        );
        GameController.getInstance().checkBothLivesEndGame();
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
                        ()->{
                            monster.activateEffect().run();
                        }
                )
        );
    }
    public void activateSpellEffect(Spell spell) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        if (!player.getBoard().getMagicCardZone().containsValue(spell) && !player.getBoard().getCardsOnHand().contains(spell))
            throw new LogicException("you can't activate this card!");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can't activate an effect on this turn");
        if (spell.isActivated())
            throw new LogicException("you have already activated this card on this turn");
        if (getPlayer().hasInHand(spell) && !spell.getIcon().equals(Icon.FIELD) && getPlayer().getBoard().getMagicCardZone().size() == 5)
            throw new LogicException("spell card zone is full");
        if (!spell.canActivateEffect())
            throw new LogicException("preparations of this spell are not done yet");

        Optional<Card> optionalBadCard = GameController.getInstance().getGame().getAllCardsOnBoard().stream().filter(
                other -> {
                    if(other instanceof Magic) {
                        Magic otherMagic = (Magic) other;
                        return other.isActivated() && !otherMagic.letMagicActivate(spell);
                    }
                    return false;
                }
        ).findAny();
        if (optionalBadCard.isPresent())
            throw new LogicException(optionalBadCard.get().getName() + " does not let you activate this");

        CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", player.getUser().getUsername(), spell.getName()), Color.Blue);
        startChain(
                new Action(
                        new MagicActivation(spell),
                        ()->{
                            if(player.getBoard().getCardsOnHand().contains(spell)) {
                                player.getBoard().removeFromHand(spell);
                                addMagicToBoard(spell, MagicState.OCCUPIED);
                            }
                            spell.activateEffect().run();
                        }
                )
        );
    }

    public void startChain(Action action) throws RoundOverExceptionEvent, ResistToChooseCard {
        ChainController chainController = new ChainController(this, action);
        chainController.control();
    }

    protected void addActionToChain(Action action) {
        CustomPrinter.println(String.format("<%s>: I add an action to the chain. It's activation question was: <%s>", player.getUser().getNickname(), action.getEvent().getActivationQuestion()), Color.Purple);
        GameController.getInstance().getGame().getChain().add(action);
    }

    public boolean hasAttackedByCard(Monster monster) {
        return !monster.isAllowAttack();
    }

    public void refresh() {
        player.setSummonedInLastTurn(false);
        for (Card card : player.getBoard().getAllCards())
            card.startOfNewTurn();
    }
}
