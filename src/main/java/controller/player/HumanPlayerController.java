package controller.player;

import controller.GameController;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import controller.menu.DuelMenuController;
import model.Game;
import model.Player.HumanPlayer;
import model.card.Card;
import model.card.Monster;
import model.card.action.Action;
import model.enums.Phase;
import view.DuelMenuView;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayerController extends PlayerController {

    public HumanPlayerController(HumanPlayer player) {
        super(player);
    }

    private void runUntilEndOfPhase(){
        DuelMenuView view = new DuelMenuView();
        Game game = GameController.instance.getGame();
        Phase phase = game.getPhase();
        while(game.getPhase().equals(phase)) {
            view.runNextCommand();
        }
    }

    @Override
    public void controlStandbyPhase() {
        runUntilEndOfPhase();
    }

    @Override
    public void controlMainPhase1(){
        runUntilEndOfPhase();
    }

    @Override
    public void controlMainPhase2(){
        runUntilEndOfPhase();
    }

    @Override
    public void controlBattlePhase(){
        runUntilEndOfPhase();
    }

    @Override
    public boolean askRespondToChain() {
        return ((DuelMenuView) DuelMenuController.getInstance().getView()).askUser(
                "Do you want to add a card to chain?", "yes", "no");
    }

    @Override
    public boolean askRespondToQuestion(String question, String yes, String no) {
        return ((DuelMenuView) DuelMenuController.getInstance().getView()).askUser(question, yes, no);
    }

    @Override
    public void doRespondToChain() throws ResistToChooseCard {
        DuelMenuView view = (DuelMenuView) DuelMenuController.getInstance().getView();
        List<Action> actions = listOfAvailableActionsInResponse();
        StringBuilder question = new StringBuilder("You have these choices. which one do you choose? (0 to quit)\n");
        for(int i = 0; i < actions.size(); i++){
            question.append(i + 1).append(". ").append(actions.get(i).getEvent().getActivationQuestion()).append("\n");
        }
        int choice = view.askUserToChooseNumber(
                question.toString(), 0, actions.size()
        );
        if(choice == 0) {
            boolean retry = view.askUser("Do you want to choose another one?", "yes", "no");
            if(retry){
                doRespondToChain();
                return;
            }
            throw new ResistToChooseCard();
        }
        addActionToChain(actions.get(choice));
    }

    @Override
    public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Card> cards = new ArrayList<>();
        while (cards.size() < numberOfCards) {
            Card card = ((DuelMenuView) DuelMenuController.getInstance().getView()).askUserToChooseCard(message, condition);
            if (cards.contains(card))
                cards.remove(card);
            else
                cards.add(card);
        }
        return (Card[]) cards.toArray();
    }

    @Override
    public Monster[] chooseKSumLevelMonsters(String message, int sumOfLevelsOfCards, SelectCondition condition) throws ResistToChooseCard {
        ArrayList<Monster> monsters = new ArrayList<>();
        int sumLevels = 0;
        while (sumLevels < sumOfLevelsOfCards) {
            Monster monster = (Monster) ((DuelMenuView) DuelMenuController.getInstance().getView()).askUserToChooseCard(message, condition);
            if (monsters.contains(monster)) {
                monsters.remove(monster);
                sumLevels -= ((Monster) monster).getLevel();
            }
            else {
                monsters.add(monster);
                sumLevels += ((Monster) monster).getLevel();
            }
        }
        return (Monster[]) monsters.toArray();
    }
}
