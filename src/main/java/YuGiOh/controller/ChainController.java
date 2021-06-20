package YuGiOh.controller;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.action.Action;
import YuGiOh.view.cardSelector.ResistToChooseCard;
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

    public void control() throws RoundOverExceptionEvent, ResistToChooseCard {
        // todo ye moshkeli darim inja. momkene ye chizi ro bendazim tooye chaain ke exception bokhore va nashe anjamesh dad!
        Stack<Action> chain = GameController.getInstance().getGame().getChain();
        while (this.active.listOfAvailableActionsInResponse().size() > 0 && this.active.askRespondToChain()) {
            try {
                this.active.doRespondToChain();
            } catch (ResistToChooseCard e){
                // todo baraye karhaii mesle summmon aval bayad cart ha ro entekhab konim baad berizim to chain
                break;
            }
            this.active = GameController.instance.getOtherPlayerController(this.active);
        }
        // now we run the actions!
        while (!chain.isEmpty()) {
            Action action = chain.pop();
            // action should be popped before activating it's effect! if not some traps will crash
            action.runEffect();
            // todo in ticke to az try catch dar ovordam. okeye?
        }
    }
}
