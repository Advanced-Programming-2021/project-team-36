package edu.sharif.nameless.in.seattle.yugioh.controller.player;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.SelectCondition;
import edu.sharif.nameless.in.seattle.yugioh.model.Game;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.AIPlayer;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Spell;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterState;

import java.util.*;

public class AIPlayerController extends PlayerController {
    public AIPlayerController(AIPlayer player) {
        super(player);
    }

    // todo complete this

    @Override
    public void controlStandbyPhase() {
        GameController.getInstance().goNextPhaseAndNotify();
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
        GameController.getInstance().goNextPhaseAndNotify();
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
        GameController.getInstance().goNextPhaseAndNotify();
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
        if (spell.getIcon().equals(Icon.RITUAL)) {
            noErrorRitualSummon(spell);
            return;
        }
        try {
            activateEffect(spell);
        } catch (LogicException ignored) {
        }
    }

    private void noErrorRitualSummon(Spell spell) {
        while (true) {
            try {
                activateEffect(spell);
            } catch (LogicException logicException) {
                break;
            }
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
        int sum = 0;
        for (Card card : player.getBoard().getAllCards()) {
            if (condition.canSelect(card)) {
                monsters.add((Monster) card);
                sum += ((Monster) card).getLevel();
            }
        }
        Collections.shuffle(monsters);
        while (sum >= sumOfLevelsOfCards) {
            boolean removed = false;
            for (Monster monster : monsters) {
                if (sum - monster.getLevel() >= sumOfLevelsOfCards) {
                    monsters.remove(monster);
                    sum -= monster.getLevel();
                    removed = true;
                    break;
                }
            }
            if (!removed)
                break;
        }
        if (sum < sumOfLevelsOfCards)
            throw new ResistToChooseCard();
        return monsters.toArray(Monster[]::new);
    }
}