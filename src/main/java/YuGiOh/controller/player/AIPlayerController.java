package YuGiOh.controller.player;

import YuGiOh.model.card.Magic;
import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;
import YuGiOh.archive.view.cardSelector.SelectCondition;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterState;

import java.util.*;

public class AIPlayerController extends PlayerController {
    public AIPlayerController(AIPlayer player) {
        super(player);
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
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_HIDDEN);
                noErrorMonsterActivateEffect((Monster) card);
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
        List<Card> allCards = new ArrayList<>(player.getBoard().getAllCards());
        while (!allCards.isEmpty()) {
            Card card = allCards.get(rnd.nextInt(allCards.size()));
            allCards.remove(card);
            if (card instanceof Monster) {
                int r = rnd.nextInt(4);
                if (r == 0)
                    noErrorSummonCard((Monster) card);
                if (r == 1)
                    noErrorFlipSummon((Monster) card);
                if (r == 2)
                    noErrorSetMonster((Monster) card);
                if (r == 3)
                    noErrorSpecialSummonCard((Monster) card);
                r = rnd.nextInt(3);
                if (r == 0)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.OFFENSIVE_OCCUPIED);
                if (r == 1)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_OCCUPIED);
                if (r == 2)
                    noErrorChangeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_HIDDEN);
                for (Card opponentCard : GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getAllCardsOnBoard()) {
                    if (opponentCard instanceof Monster)
                        noErrorAttack((Monster) card, (Monster) opponentCard);
                }
                noErrorMonsterActivateEffect((Monster) card);
                noErrorDirectAttack((Monster) card);
            } else if (card instanceof Magic) {
                noErrorSetMagic((Magic) card);
            }
            if (card instanceof Spell) {
                noErrorActivateEffect((Spell) card);
            }
        }
        GameController.getInstance().goNextPhaseAndNotify();
    }

    protected void noErrorSummonCard(Monster monster) {
        try {
            normalSummon(monster, false);
        } catch (ResistToChooseCard | LogicException ignored) {
        }
    }

    protected void noErrorSpecialSummonCard(Monster monster) {
        try {
            specialSummon(monster, false);
        } catch (ResistToChooseCard | LogicException ignored) {
        }
    }

    protected void noErrorFlipSummon(Monster monster) {
        try {
            flipSummon(monster, false);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    protected void noErrorChangeMonsterPosition(Monster monster, MonsterState monsterState) {
        try {
            changeMonsterPosition(monster, monsterState, false);
        } catch (LogicException ignored) {
        }
    }

    protected void noErrorSetMonster(Monster monster) {
        try {
            setMonster(monster, false);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    protected void noErrorSetMagic(Magic magic) {
        try {
            setMagic(magic, false);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    protected void noErrorActivateEffect(Spell spell) {
        if (spell.getIcon().equals(Icon.RITUAL)) {
            noErrorRitualSummon(spell);
            return;
        }
        try {
            activateSpellEffect(spell, false);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    protected void noErrorRitualSummon(Spell spell) {
        while (true) {
            try {
                activateSpellEffect(spell, false);
            } catch (LogicException | ResistToChooseCard logicException) {
                break;
            }
        }
    }

    protected void noErrorAttack(Monster attacker, Monster defender) {
        try {
            attack(attacker, defender);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    protected void noErrorDirectAttack(Monster monster) {
        try {
            directAttack(monster, false);
        } catch (LogicException | ResistToChooseCard ignored) {
        }
    }

    protected void noErrorMonsterActivateEffect(Monster monster) {
        try {
            activateMonsterEffect(monster, false);
        } catch (LogicException | ResistToChooseCard ignored) {
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
        GameController.getInstance().getGame().getAllCards().stream()
                .filter(condition::canSelect).forEach(cards::add);
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
