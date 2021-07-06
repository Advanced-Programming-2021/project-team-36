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

    public ChainController(PlayerController starter, Action firstAction) {
        instance = this;
        GameController.getInstance().getGame().setChain(new Stack<>());
        GameController.getInstance().getGame().getChain().push(firstAction);
    }

    public void control() throws RoundOverExceptionEvent, ResistToChooseCard {
        Stack<Action> chain = GameController.getInstance().getGame().getChain();
        while (true) {
            GameController.getInstance().getGame().changeTurnInChain();
            PlayerController other = GameController.getInstance().getOpponentPlayerController();
            if(other.listOfAvailableActionsInResponse().size() <= 0 || !other.askRespondToChain())
                break;
            try {
                other.doRespondToChain();
            } catch (ResistToChooseCard e) {
                break;
            }
        }
        CustomPrinter.println(chain.size(), Color.Blue);
        while (!chain.isEmpty()) {
            GameController.getInstance().getGame().changeTurnInChain();

            Action action = chain.pop();
            try {
                action.runEffect();
                // todo remove this
                if (action.getEvent() instanceof MagicActivation && ((MagicActivation) action.getEvent()).getCard() instanceof Spell)
                    for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard())
                        if (card instanceof SpellAbsorption && card.isFacedUp())
                            ((SpellAbsorption) card).onSpellResolve();
            } catch (Exception e) {
                // todo inja game exception event ro nabayad befrestim bala?
                if(!(e instanceof GameExceptionEvent))
                    e.printStackTrace();
            }
        }

        // todo remove this last part for production
        Player beforePlayer = GameController.getInstance().getGame().getCurrentPlayer();
        GameController.getInstance().getGame().resetCurrentPlayerAfterChain();
        Player afterPlayer =  GameController.getInstance().getGame().getCurrentPlayer();
        if(!beforePlayer.equals(afterPlayer))
            throw new Error("chain error. this must never happen");
    }
}
