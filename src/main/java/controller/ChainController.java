package controller;

import controller.cardSelector.ResistToChooseCard;
import controller.events.GameOverEvent;
import controller.player.PlayerController;
import lombok.Getter;
import model.card.action.Action;

import java.util.Stack;

public class ChainController {
    @Getter
    ChainController instance;
    PlayerController active;

    public ChainController(PlayerController starter, Action firstAction) {
        instance = this;
        this.active = GameController.instance.getOtherPlayerController(starter);
        GameController.getInstance().getGame().setChain(new Stack<>());
        GameController.getInstance().getGame().getChain().push(firstAction);
    }

    public void control() throws GameOverEvent {
        Stack<Action> chain = GameController.getInstance().getGame().getChain();
        while (this.active.listOfAvailableActionsInResponse().size() > 0 && this.active.askRespondToChain()) {
            try {
                this.active.doRespondToChain();
            } catch (ResistToChooseCard e){
                break;
            }
            this.active = GameController.instance.getOtherPlayerController(this.active);
        }
        // now we run the actions!
        while (!chain.isEmpty()) {
            Action action = chain.pop();
            // action should be popped before activating it's effect! if not some traps will crash
            try {
                action.getEffect().run();
            } catch (ResistToChooseCard | LogicException ignored) {
            }
        }
    }
}
