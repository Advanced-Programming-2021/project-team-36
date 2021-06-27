package YuGiOh.controller.player;

import YuGiOh.controller.GameController;
import YuGiOh.model.Game;
import YuGiOh.model.enums.Phase;
import YuGiOh.view.DuelMenuView;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.Player.HumanPlayer;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.action.Action;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayerController extends PlayerController {

    public HumanPlayerController(HumanPlayer player) {
        super(player);
    }

    private void runUntilEndOfPhase() {
        Game game = GameController.instance.getGame();
        Phase phase = game.getPhase();
        while (game.getPhase().equals(phase)) {
            DuelMenuController.getInstance().getView().runNextCommand();
        }
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

    private DuelMenuView getView() {
        return (DuelMenuView) DuelMenuController.getInstance().getView();
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
        DuelMenuView view = (DuelMenuView) DuelMenuController.getInstance().getView();
        List<Action> actions = listOfAvailableActionsInResponse();
        List<String> choices = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++)
            choices.add(actions.get(i).getEvent().getActivationQuestion());
        try {
            int choice = getView().askUserToChoose(
                    "choose one of this options", choices
            );
            addActionToChain(actions.get(choice));
        } catch (ResistToChooseCard e) {
            boolean retry = view.askUser("Do you want to choose another one?", "yes", "no");
            if (retry) {
                doRespondToChain();
                return;
            }
            throw new ResistToChooseCard();
        }
    }

    @Override
    public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Card> cards = new ArrayList<>();
        if (GameController.getInstance().getGame().getAllCards().stream().filter(condition::canSelect).count() < numberOfCards)
            throw new ResistToChooseCard();

        while (cards.size() < numberOfCards) {
            Card card = getView().askUserToChooseCard(message + " select twice to deselect!", condition);
            if (cards.contains(card))
                cards.remove(card);
            else
                cards.add(card);
        }
        return cards.toArray(Card[]::new);
    }

    @Override
    public Monster[] chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Monster> monsters = new ArrayList<>();
        int sumLevels = 0;
        while (sumLevels < sumOfLevelsOfCards) {
            Monster monster = (Monster) getView().askUserToChooseCard(message, condition);
            if (monsters.contains(monster)) {
                monsters.remove(monster);
                sumLevels -= ((Monster) monster).getLevel();
            } else {
                monsters.add(monster);
                sumLevels += ((Monster) monster).getLevel();
            }
        }
        return monsters.toArray(Monster[]::new);
    }
}
