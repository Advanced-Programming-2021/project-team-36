package YuGiOh.controller.player;

import YuGiOh.controller.GameController;
import YuGiOh.controller.MainGameThread;
import YuGiOh.view.DuelMenuView;
import YuGiOh.archive.view.cardSelector.FinishSelectingCondition;
import YuGiOh.archive.view.cardSelector.FinishSelectingConditions;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;
import YuGiOh.archive.view.cardSelector.SelectCondition;
import YuGiOh.controller.menus.DuelMenuController;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HumanPlayerController extends PlayerController {

    public HumanPlayerController(HumanPlayer player) {
        super(player);
    }

    private void runUntilEndOfPhase() {
        MainGameThread.getInstance().runQueuedTasks();
    }

    @Override
    public void controlMainPhase1() {
        runUntilEndOfPhase();
    }

    @Override
    public void controlMainPhase2() {
        runUntilEndOfPhase();
    }

    @Override
    public void controlBattlePhase() {
        runUntilEndOfPhase();
    }

    @Override
    public boolean askRespondToChain() {
        return getView().askUser(
                "Do you want to add a card to chain?", "yes", "no");
    }

    @Override
    public boolean askRespondToQuestion(String question, String yes, String no) {
        return getView().askUser(question, yes, no);
    }

    @Override
    public void doRespondToChain() throws ResistToChooseCard {
        List<Action> actions = listOfAvailableActionsInResponse();
        List<String> choices = new ArrayList<>();
        for(int i = 0; i < actions.size(); i++)
            choices.add(actions.get(i).getEvent().getActivationQuestion());
        int choice = getView().askUserToChoose(
                "choose one of this options", choices
        );
        addActionToChain(actions.get(choice));
    }

    @Override
    public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        if(GameController.getInstance().getGame().getAllCards().stream().filter(condition::canSelect).count() < numberOfCards)
            throw new ResistToChooseCard();
        return getView().askUserToChooseCard(
                        message,
                        condition,
                        FinishSelectingConditions.getCount(numberOfCards)
                ).toArray(Card[]::new);
    }

    @Override
    public Monster[] chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) throws ResistToChooseCard {
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
        ).stream().map(card -> (Monster) card).toArray(Monster[]::new);
    }

    private DuelMenuView getView() {
        return DuelMenuController.getInstance().getView();
    }
}
