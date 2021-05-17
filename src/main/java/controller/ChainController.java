package controller;

import controller.events.GameOverEvent;
import controller.player.PlayerController;
import lombok.Getter;
import model.card.action.Action;
import model.card.action.Effect;

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
        // todo : effect should push
    }

    public void control() throws GameOverEvent {
        // TODO : it's not complete
        while (this.active.askRespondToChain()) {
            this.active.doRespondToChain();
            this.active = GameController.instance.getOtherPlayerController(this.active);
        }
        // now we run the actions!
        Stack<Action> chain = GameController.getInstance().getGame().getChain();
        while (!chain.isEmpty()) {
            Action action = chain.pop();
            // action should be popped before activating it's effect! if not some traps will crash
            action.getEffect().run();
        }
    }
}
