package YuGiOh.controller;

import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.card.magicCards.spells.SpellAbsorption;
import YuGiOh.model.exception.ResistToChooseCard;
import lombok.Getter;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;

public class ChainController {
    @Getter
    private final ChainController instance;

    public ChainController() {
        instance = this;
        GameController.getInstance().getGame().resetChain();
    }

    public CompletableFuture<Void> askToAddToChain() {
        System.out.println("TRYING TO ADD ACTION TO CHAIN");
        GameController.getInstance().getGame().changeTurnInChain();
        PlayerController other = GameController.getInstance().getCurrentPlayerController();
        if(other.listOfAvailableActionsInResponse().size() <= 0) {
            System.out.println("FINISHED ADDING TO CHAIN");
            return CompletableFuture.completedFuture(null);
        }
        return other.askRespondToChain().thenCompose(res ->{
            if(res) {
                return other.doRespondToChain().thenCompose(dum -> askToAddToChain());
            } else {
                System.out.println("FINISHED ADDING TO CHAIN");
                return CompletableFuture.completedFuture(null);
            }
        });
    }

    public CompletableFuture<Void> control() throws RoundOverExceptionEvent, GameException {
        return askToAddToChain()
                .thenRun(()-> GameController.getInstance().getGame().resetCurrentPlayerAfterChain())
                .thenCompose(res -> recurse());
    }

    public CompletableFuture<Void> recurse() {
        Stack<Action> chain = GameController.getInstance().getGame().getChain();
        if(chain.isEmpty()) {
            System.out.println("FINISHED RECURSING");
            return CompletableFuture.completedFuture(null);
        }
        Action action = chain.pop();
        return action.runEffect().thenCompose(res->{
            // todo remove magic absorption
            if (action.getEvent() instanceof MagicActivation && ((MagicActivation) action.getEvent()).getMagic() instanceof Spell)
                for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard()) {
                    if (card instanceof SpellAbsorption && card.isFacedUp())
                        ((SpellAbsorption) card).onSpellResolve();
                }
            return recurse();
        });
    }
}
