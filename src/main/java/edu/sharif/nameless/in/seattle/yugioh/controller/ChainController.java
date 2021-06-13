package edu.sharif.nameless.in.seattle.yugioh.controller;

import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverExceptionEvent;
import edu.sharif.nameless.in.seattle.yugioh.controller.player.PlayerController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import lombok.Getter;

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

    public void control() throws RoundOverExceptionEvent, ResistToChooseCard, LogicException {
        // todo ye moshkeli darim inja. momkene ye chizi ro bendazim tooye chaain ke exception bokhore va nashe anjamesh dad!
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
            action.getEffect().run();
            // todo in ticke to az try catch dar ovordam. okeye?
        }
    }
}
