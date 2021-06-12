package edu.sharif.nameless.in.seattle.yugioh.controller.player;

import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverExceptionEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.*;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.*;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.Conditions;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Spell;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.controller.ChainController;
import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.SelectCondition;
import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.model.Board;
import edu.sharif.nameless.in.seattle.yugioh.model.CardAddress;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerController {
    @Getter
    Player player;

    PlayerController(Player player) {
        this.player = player;
    }

    abstract public void controlStandbyPhase();

    abstract public void controlMainPhase1();

    abstract public void controlMainPhase2();

    abstract public void controlBattlePhase();

    abstract public boolean askRespondToChain();

    abstract public boolean askRespondToQuestion(String question, String yes, String no);

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

    public void summon(Monster monster, int requiredTributes, MonsterState monsterState) throws LogicException, ResistToChooseCard {
        Game game = GameController.instance.getGame();
        if (requiredTributes > 0)
            tributeMonster(requiredTributes);
        Board board = game.getCurrentPlayer().getBoard();
        for (int i = 1; i <= 5; i++) {
            if (board.getMonsterCardZone().get(i) == null) {
                board.addCardToBoard(monster, new CardAddress(ZoneType.MONSTER, i, player));
                monster.setMonsterState(monsterState);
                break;
            }
        }
    }

    public void addMagicToBoard(Magic magic, MagicState magicState) {
        Game game = GameController.getInstance().getGame();
        Board board = game.getCurrentPlayer().getBoard();
        if (magic.getIcon().equals(Icon.FIELD))
            board.addCardToBoard((Card) magic, new CardAddress(ZoneType.FIELD, 1, player));
        else {
            for (int i = 1; i <= 5; i++) {
                if (board.getMagicCardZone().get(i) == null) {
                    board.addCardToBoard(magic, new CardAddress(ZoneType.MAGIC, i, player));
                    magic.setMagicState(magicState);
                    break;
                }
            }
        }
    }

    public void normalSummon(Card card) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        if (card instanceof Magic)
            throw new LogicException("this card is magic and you can't summon it");
        Monster monster = (Monster) card;
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (game.getCurrentPlayer().isSummonedInLastTurn())
            throw new LogicException("you already summoned/set on this turn");
        if (!player.hasInHand(card))
            throw new LogicException("you can only summon from your hand");
        startChain(
                new Action(
                        new SummonEvent(monster, SummonType.NORMAL),
                        () -> {
                                boolean AttackingState = askRespondToQuestion("which position you want to summon?", "attacking", "defending");
                                MonsterState monsterState = (AttackingState ? MonsterState.OFFENSIVE_OCCUPIED : MonsterState.DEFENSIVE_OCCUPIED);
                                summon(monster, monster.getNumberOfRequiredTribute(), monsterState);
                                player.getBoard().getCardsOnHand().remove((Card) monster);
                                player.setSummonedInLastTurn(true);
                                CustomPrinter.println(String.format("<%s> summoned <%s> in <%s> position successfully", player.getUser().getUsername(), monster.getName(), monsterState), Color.Green);
                        }
                )
        );
    }

    public void flipSummon(Monster monster) throws LogicException {
        Game game = GameController.getInstance().getGame();
        if (!GameController.getInstance().getCurrentPlayerController().getPlayer().getBoard().getMonsterCardZone().containsValue(monster))
            throw new LogicException("this card is not in your field");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can’t do this action in this phase");
        if (!monster.getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN) || game.getCurrentPlayer().isSummonedInLastTurn())
            throw new LogicException("you can't flip summon this card");
        startChain(
                new Action(
                        new SummonEvent(monster, SummonType.FLIP),
                        () -> {
                            monster.setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
                            GameController.getInstance().getGame().getCurrentPlayer().setSummonedInLastTurn(true);
                            CustomPrinter.println(String.format("<%s> flip summoned successfully", monster.getName()), Color.Green);
                        }
                )
        );
    }


    public void setMonster(Monster monster) throws LogicException, ResistToChooseCard {
        Game game = GameController.getInstance().getGame();
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("action not allowed in this phase");
        if (game.getCurrentPlayer().isSummonedInLastTurn())
            throw new LogicException("you already summoned/set on this turn");
        if (!player.hasInHand((Card) monster))
            throw new LogicException("you can only summon from your hand");
        startChain(
                new Action(
                        new SetMonster(monster),
                        () -> {
                            summon(monster, monster.getNumberOfRequiredTribute(), MonsterState.DEFENSIVE_HIDDEN);
                            player.getBoard().getCardsOnHand().remove((Card) monster);
                            player.setSummonedInLastTurn(true);
                            CustomPrinter.println(String.format("<%s> set monster <%s> successfully", player.getUser().getUsername(), monster.getName()), Color.Green);
                        }
                )
        );
    }

    public void tributeMonster(int count) throws LogicException, ResistToChooseCard {
        if (player.getBoard().getMonsterCardZone().size() < count)
            throw new LogicException("there are not enough cards for tribute");
        Card[] tributeCards = chooseKCards(String.format("Choose %d cards to tribute", count), count, Conditions.myMonsterFromMyMonsterZone(player));
        for (Card card : tributeCards)
            moveCardToGraveYard(card);
//        CustomPrinter.println(String.format("<%s> tribute this monsters: %s", Arrays.toString(Arrays.stream(tributeCards).toArray())), Color.Default);
    }

    public void moveCardToGraveYard(Card card) {
        CustomPrinter.println(String.format("<%s>' Card <%s> moved to graveyard", player.getUser().getUsername(), card.getName()), Color.Blue);
        player.moveCardToGraveYard(card);
    }

    public void setMagic(Magic magic) throws LogicException {
        if (!player.hasInHand(magic))
            throw new LogicException("you can't set this card");
        if (!GameController.getInstance().getGame().getPhase().isMainPhase())
            throw new LogicException("you can't do this action in this phase");
        if (!magic.getIcon().equals(Icon.FIELD) && player.getBoard().getMagicCardZone().size() == 5)
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

    public void attack(Monster monster, Monster opponentMonster) throws LogicException, RoundOverExceptionEvent {
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

    public void directAttack(Monster monster) throws RoundOverExceptionEvent, LogicException {
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

    public void activateEffect(Spell spell) throws LogicException, RoundOverExceptionEvent {
        Game game = GameController.getInstance().getGame();
        if (!player.getBoard().getMagicCardZone().containsValue(spell) && !player.getBoard().getCardsOnHand().contains((Card) spell))
            throw new LogicException("you can't activate this card!");
        if (!game.getPhase().equals(Phase.MAIN_PHASE1) && !game.getPhase().equals(Phase.MAIN_PHASE2))
            throw new LogicException("you can't activate an effect on this turn");
        if (spell.getMagicState() != null && spell.getMagicState().equals(MagicState.OCCUPIED))
            throw new LogicException("you have already activated this card"); // todo : check correctness of this
        if (getPlayer().hasInHand(spell) && !spell.getIcon().equals(Icon.FIELD) && getPlayer().getBoard().getMagicCardZone().size() == 5)
            throw new LogicException("spell card zone is full");
        if (!spell.canActivateEffect())
            throw new LogicException("preparations of this spell are not done yet");

        CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", player.getUser().getUsername(), spell.getName()), Color.Blue);

        startChain(
                new Action(
                        new MagicActivation(spell),
                        spell.activateEffect()
                )
        );
    }

    public void startChain(Action action) throws RoundOverExceptionEvent {
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
}
