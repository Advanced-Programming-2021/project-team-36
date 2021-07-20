package YuGiOh.controller.player;

import YuGiOh.model.card.Magic;
import YuGiOh.controller.GameController;
import YuGiOh.model.card.action.NextPhaseAction;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.model.Game;
import YuGiOh.model.Player.AIPlayer;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.enums.MonsterState;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AIPlayerController extends PlayerController {
    public AIPlayerController(AIPlayer player) {
        super(player);
    }

    public CompletableFuture<Void> recurse(Stack<Action> actions) {
        CompletableFuture<Void> ret = CompletableFuture.completedFuture(null);
        for(Action action : actions) {
            ret = ret.thenCompose(res-> {
                if(!action.isValid())
                    return CompletableFuture.completedFuture(null);
                try {
                    return GameController.getInstance().startChain(action);
                } catch (GameException e) {
                    throw new Error(action.isValid() + " this must never happen!");
                }
            });
        }
        ret = ret.thenCompose(res -> new NextPhaseAction().runEffect());
        return ret;
    }

    public CompletableFuture<Void> doAllYouCan() {
        Stack<Action> actions = getAllActions().stream().filter(Action::isValid).collect(Collectors.toCollection(Stack<Action>::new));
        Collections.shuffle(actions);
        return recurse(actions);
    }
    public void mainPhase() {
        doAllYouCan();
    }

    @Override
    public void controlMainPhase1() {
        mainPhase();
    }

    @Override
    public void controlMainPhase2() {
        mainPhase();
    }

    protected List<Action> getAllActionsOfCard(Card card) {
        List<Action> actions = new ArrayList<>();
        if (card instanceof Monster) {
            actions.add(normalSummon((Monster) card));
            actions.add(flipSummon((Monster) card));
            actions.add(setMonster((Monster) card));
            actions.add(specialSummon((Monster) card));
            actions.add(changeMonsterPosition((Monster) card, MonsterState.OFFENSIVE_OCCUPIED));
            actions.add(changeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_HIDDEN));
            actions.add(changeMonsterPosition((Monster) card, MonsterState.DEFENSIVE_OCCUPIED));
            for (Card opponentCard : GameController.getInstance().getGame().getOtherPlayer(player).getBoard().getAllCardsOnBoard()) {
                if (opponentCard instanceof Monster)
                    actions.add(attack((Monster) card, (Monster) opponentCard));
            }
            actions.add(activateMonsterEffect((Monster) card));
            actions.add(directAttack((Monster) card));
        } else if (card instanceof Magic) {
            actions.add(setMagic((Magic) card));
        }
        if (card instanceof Spell) {
            actions.add(activateSpellEffect((Spell) card));
        }
        return actions;
    }

    protected List<Action> getAllActions() {
        List<Action> actions = new ArrayList<>();
        // todo after testing we can omit cards in graveyard and deck to make it faster
        player.getBoard().getAllCards().forEach(card->{
            actions.addAll(getAllActionsOfCard(card));
        });
        return actions;
    }

    @Override
    public void controlBattlePhase() {
        doAllYouCan();
    }

    @Override
    public CompletableFuture<Boolean> askRespondToChain() {
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Void> doRespondToChain() {
        List<Action> actions = listOfAvailableActionsInResponse();
        Random rnd = new Random();
        addActionToChain(actions.get(rnd.nextInt(actions.size())));
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Boolean> askRespondToQuestion(String question, String yes, String no) {
        Random rand = new Random();
        return CompletableFuture.completedFuture(rand.nextBoolean());
    }

    @Override
    public CompletableFuture<List<Card>> chooseKCards(String message, int numberOfCards, SelectCondition condition) {
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
            return CompletableFuture.failedFuture(new ResistToChooseCard());
        Random rnd = new Random();
        while (cards.size() > numberOfCards) {
            int id = rnd.nextInt(cards.size());
            cards.remove(id);
        }
        return CompletableFuture.completedFuture(cards);
    }

    @Override
    public CompletableFuture<List<Monster>> chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) {
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
            return CompletableFuture.failedFuture(new ResistToChooseCard());
        return CompletableFuture.completedFuture(monsters);
    }
}
