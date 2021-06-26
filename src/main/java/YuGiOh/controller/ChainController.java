package YuGiOh.controller;

import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.card.magicCards.spells.SpellAbsorption;
import YuGiOh.model.card.magicCards.spells.SupplySquad;
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
            try {
                // action should be popped before activating it's effect! if not some traps will crash
                action.runEffect();

                if (action.getEvent() instanceof MagicActivation && ((MagicActivation) action.getEvent()).getCard() instanceof Spell)
                    for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard())
                        if (card instanceof SpellAbsorption)
                            ((SpellAbsorption) card).onSpellResolve();
            } catch (Exception exception) {
            }
        }
    }
}
