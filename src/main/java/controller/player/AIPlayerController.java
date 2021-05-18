package controller.player;

import controller.GameController;
import controller.LogicException;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import model.Game;
import model.Player.AIPlayer;
import model.Player.Player;
import model.card.*;
import model.card.action.Action;
import model.enums.MonsterState;

import java.util.*;

public class AIPlayerController extends PlayerController {
    public AIPlayerController(AIPlayer player) {
        super(player);
    }

    // todo complete this

    @Override
    public void controlStandbyPhase() {
        GameController.getInstance().goNextPhase();
    }

    public void mainPhase() {
        Random rnd = new Random();
        List<Card> allCards = new ArrayList<>(player.getBoard().getAllCards());
        while (!allCards.isEmpty()) {
            Card card = allCards.get(rnd.nextInt(allCards.size()));
            allCards.remove(card);
            if (card instanceof Monster) {
                int r = rnd.nextInt(3);
                if (r == 0)
                    noErrorSummonCard((Monster) card);
                if (r == 1)
                    noErrorFlipSummon((Monster) card);
                if (r == 2)
                    noErrorSetMonster((Monster) card);
                r = rnd.nextInt(3);
                if (r == 0)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.OFFENSIVE_OCCUPIED);
                if (r == 1)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_OCCUPIED);
                if (r == 2)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_HIDDEN); // todo remove this
            } else if (card instanceof Magic) {
                noErrorSetMagic((Magic) card);
            }
            if (card instanceof Spell) {
                noErrorActivateEffect((Spell) card);
            }
        }
        GameController.getInstance().goNextPhase();
    }

    @Override
    public void controlMainPhase1() {
        mainPhase();
    }

    @Override
    public void controlMainPhase2() {
        mainPhase();
    }

    @Override
    public void controlBattlePhase() {
        Random rnd = new Random();
        List<Card> allCards = new ArrayList<>(player.getBoard().getAllCards()); // or cards on board
        while (!allCards.isEmpty()) {
            Card card = allCards.get(rnd.nextInt(allCards.size()));
            allCards.remove(card);
            if (card instanceof Monster) {
                int r = rnd.nextInt(3);
                if (r == 0)
                    noErrorSummonCard((Monster) card);
                if (r == 1)
                    noErrorFlipSummon((Monster) card);
                if (r == 2)
                    noErrorSetMonster((Monster) card);
                r = rnd.nextInt(3);
                if (r == 0)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.OFFENSIVE_OCCUPIED);
                if (r == 1)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_OCCUPIED);
                if (r == 2)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_HIDDEN); // todo remove this
                for (Card opponentCard : GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getAllCardsOnBoard()) {
                    if (opponentCard instanceof Monster)
                        noErrorAttack((Monster) card, (Monster) opponentCard);
                    noErrorDirectAttack((Monster) card);
                }
            } else if (card instanceof Magic) {
                noErrorSetMagic((Magic) card);
            }
            if (card instanceof Spell) {
                noErrorActivateEffect((Spell) card);
            }
        }
        GameController.getInstance().goNextPhase();
    }

    private void noErrorSummonCard(Monster monster) {
        try {
            normalSummon(monster);
        } catch (ResistToChooseCard | LogicException ignored) {
        }
    }

    private void noErrorFlipSummon(Monster monster) {
        try {
            flipSummon(monster);
        } catch (LogicException ignored) {
        }
    }

    private void noErrorChangeMonsterPosition(Monster monster, MonsterState monsterState) {
        try {
            changeMonsterPosition(monster, monsterState);
        } catch (LogicException ignored) {
        }
    }

    private void noErrorSetMonster(Monster monster) {
        try {
            setMonster(monster);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    private void noErrorSetMagic(Magic magic) {
        try {
            setMagic(magic);
        } catch (LogicException ignored) {
        }
    }

    private void noErrorActivateEffect(Spell spell) {
        try {
            activateEffect(spell);
        } catch (LogicException ignored) {
        }
    }

    private void noErrorAttack(Monster attacker, Monster defender) {
        try {
            attack(attacker, defender);
        } catch (LogicException ignored) {
        }
    }

    private void noErrorDirectAttack(Monster monster) {
        try {
            directAttack(monster);
        } catch (LogicException ignored) {
        }
    }

    @Override
    public boolean askRespondToChain() {
        return true;
    }

    @Override
    public void doRespondToChain() {
        List<Action> actions = listOfAvailableActionsInResponse();
        Random rnd = new Random();
        addActionToChain(actions.get(rnd.nextInt(actions.size())));
    }

    @Override
    public boolean askRespondToQuestion(String question, String yes, String no) {
        Random rand = new Random();
        return rand.nextBoolean();
    }

    @Override
    public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : player.getBoard().getAllCards()) {
            if (condition.canSelect(card)) {
                cards.add(card);
            }
        }
        Game game = GameController.getInstance().getGame();
        Player otherPlayer = player.equals(game.getFirstPlayer()) ? game.getSecondPlayer() : game.getFirstPlayer();
        for (Card card : otherPlayer.getBoard().getAllCards()) {
            if (condition.canSelect(card)) {
                cards.add(card);
            }
        }

        if (cards.size() < numberOfCards)
            throw new ResistToChooseCard();
        Random rnd = new Random();
        while (cards.size() > numberOfCards) {
            int id = rnd.nextInt(cards.size());
            cards.remove(id);
        }
        return cards.toArray(Card[]::new);
    }

    @Override
    public Monster[] chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Monster> monsters = new ArrayList<>();
        for (Card card : player.getBoard().getAllCards()) {
            if (condition.canSelect(card)) {
                monsters.add((Monster) card);
            }
        }
        Collections.shuffle(monsters);
        int sum = 0;
        for (Monster monster : monsters) {
            if (sum >= sumOfLevelsOfCards) {
                monsters.remove(monster);
                continue;
            }
            sum += monster.getLevel();
        }
        if (sum < sumOfLevelsOfCards)
            throw new ResistToChooseCard();
        return (Monster[]) monsters.toArray();
    }
}
