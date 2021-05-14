package controller;

import controller.player.PlayerController;
import lombok.Getter;
import model.card.Effect;

import java.util.Stack;

public class ChainController {
    @Getter
    ChainController instance;
    PlayerController active;

    public ChainController(PlayerController starter){
        instance = this;
        this.active = GameController.instance.getOtherPlayerController(starter);
        GameController.getInstance().getGame().setChain(new Stack<>());
    }

    public void control(){
        while(this.active.askRespondToChain()){
            this.active.doRespondToChain();
            this.active = GameController.instance.getOtherPlayerController(this.active);
        }
        // now we run the effects!
        Stack<Effect> chain = GameController.getInstance().getGame().getChain();
        while(!chain.isEmpty()){
            Effect effect = chain.pop();
            effect.run();
        }
    }
}
