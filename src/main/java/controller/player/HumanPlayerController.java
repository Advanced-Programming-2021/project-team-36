package controller.player;

import controller.GameController;
import controller.cardSelector.ResistToChooseCard;
import controller.cardSelector.SelectCondition;
import controller.menu.DuelMenuController;
import model.Game;
import model.Player.HumanPlayer;
import model.card.Card;
import model.enums.Phase;
import view.DuelMenuView;

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
                "Do you want to add a card to chain?");
    }

    @Override
    public void doRespondToChain() {
        // todo
    }

    @Override
    public Card[] chooseKCards(String message, int numberOfCards, SelectCondition condition) throws ResistToChooseCard {
        return ((DuelMenuView) DuelMenuController.getInstance().getView()).askUserToChooseKCards(message, numberOfCards, condition);
    }
}
