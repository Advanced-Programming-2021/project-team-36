package YuGiOh.controller;

import YuGiOh.controller.events.GameExceptionEvent;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.card.magicCards.spells.SpellAbsorption;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import lombok.Getter;

import java.util.Stack;

public class ChainController {
    @Getter
    private final ChainController instance;

    public ChainController(Action firstAction) {
        instance = this;
        GameController.getInstance().getGame().setChain(new Stack<>());
        GameController.getInstance().getGame().getChain().push(firstAction);
    }

    public void control() throws RoundOverExceptionEvent, ResistToChooseCard {
        Stack<Action> chain = GameController.getInstance().getGame().getChain();
        while (true) {
            GameController.getInstance().getGame().changeTurnInChain();
            PlayerController other = GameController.getInstance().getCurrentPlayerController();
            if(other.listOfAvailableActionsInResponse().size() <= 0 || !other.askRespondToChain())
                break;
            try {
                other.doRespondToChain();
            } catch (ResistToChooseCard e) {
                break;
            }
        }

        GameController.getInstance().getGame().resetCurrentPlayerAfterChain();

        while (!chain.isEmpty()) {
            Action action = chain.pop();
            try {
                action.runEffect();
                // todo remove this
                if (action.getEvent() instanceof MagicActivation && ((MagicActivation) action.getEvent()).getMagic() instanceof Spell)
                    for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard())
                        if (card instanceof SpellAbsorption && card.isFacedUp())
                            ((SpellAbsorption) card).onSpellResolve();
            } catch (Exception e) {
                if(!(e instanceof GameExceptionEvent))
                    e.printStackTrace();
                else
                    throw e;
            }
        }
    }
}
