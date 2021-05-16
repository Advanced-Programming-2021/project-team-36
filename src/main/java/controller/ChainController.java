package controller;

import controller.events.GameOverEvent;
import controller.player.PlayerController;
import lombok.Getter;
import model.card.Effect;

import java.util.Stack;

public class ChainController {
    @Getter
    ChainController instance;
    PlayerController active;

    public ChainController(PlayerController starter, Effect effect) {
        instance = this;
        this.active = GameController.instance.getOtherPlayerController(starter);
        GameController.getInstance().getGame().setChain(new Stack<>());
        // todo : effect should push
    }

    public void control(Effect startingEffect) throws GameOverEvent {
        // TODO : it's not complete
        GameController.getInstance().getGame().getChain().push(startingEffect);
        while (this.active.askRespondToChain()) {
            this.active.doRespondToChain();
            this.active = GameController.instance.getOtherPlayerController(this.active);
        }
        // now we run the effects!
        Stack<Effect> chain = GameController.getInstance().getGame().getChain();
        while (!chain.isEmpty()) {
            Effect effect = chain.pop();
            effect.run();
        }
    }
}
