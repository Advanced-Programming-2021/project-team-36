package YuGiOh.controller.player;

import YuGiOh.controller.GameController;
import YuGiOh.view.menu.DuelMenuView;
import YuGiOh.view.cardSelector.FinishSelectingCondition;
import YuGiOh.view.cardSelector.FinishSelectingConditions;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HumanPlayerController extends PlayerController {

    public HumanPlayerController(HumanPlayer player) {
        super(player);
    }

    @Override
    public void controlMainPhase1() {
    }

    @Override
    public void controlMainPhase2() {
    }

    @Override
    public void controlBattlePhase() {
    }

    @Override
    public CompletableFuture<Boolean> askRespondToChain() {
        return getView().askUser(
                "Do you want to add a card to chain?", "yes", "no");
    }

    @Override
    public CompletableFuture<Boolean> askRespondToQuestion(String question, String yes, String no) {
        return getView().askUser(question, yes, no);
    }

    @Override
    public CompletableFuture<Void> doRespondToChain() {
        List<Action> actions = listOfAvailableActionsInResponse();
        List<String> choices = new ArrayList<>();
        for(int i = 0; i < actions.size(); i++)
            choices.add(actions.get(i).getEvent().getActivationQuestion());
        return getView().askUserToChoose(
                "choose one of this options", choices
        ).thenAccept(res -> addActionToChain(actions.get(res)));
    }

    @Override
    public CompletableFuture<List<Card>> chooseKCards(String message, int numberOfCards, SelectCondition condition) {
        if(GameController.getInstance().getGame().getAllCards().stream().filter(condition::canSelect).count() < numberOfCards) {
            System.out.println("throwing failed!");
            return CompletableFuture.failedFuture(new ResistToChooseCard());
        }
        return getView().askUserToChooseCard(
                        message,
                        condition,
                        FinishSelectingConditions.getCount(numberOfCards)
                );
    }

    @Override
    public CompletableFuture<List<Monster>> chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) {
        FinishSelectingCondition finishCondition = (cards) -> {
            Optional<Integer> optional = cards.stream().map(card -> ((Monster) card).getLevel()).reduce(Integer::sum);
            if(optional.isPresent())
                return optional.get() >= sumOfLevelsOfCards;
            return sumOfLevelsOfCards <= 0;
        };
        return getView().askUserToChooseCard(
                message,
                condition,
                finishCondition
        ).thenApply(res-> res.stream().map(card -> (Monster) card).collect(Collectors.toList()));
    }

    private DuelMenuView getView() {
        return DuelMenuController.getInstance().getView();
    }
}
