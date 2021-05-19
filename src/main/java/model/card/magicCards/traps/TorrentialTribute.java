package model.card.magicCards.traps;

import controller.GameController;
import model.card.Card;
import model.card.Monster;
import model.card.Trap;
import model.card.action.Action;
import model.card.action.Effect;
import model.card.action.SummonEvent;
import model.enums.Icon;
import model.enums.Status;

public class TorrentialTribute extends Trap {
    public TorrentialTribute(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        assert canActivateEffect();
        return () -> {
            for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard()) {
                if (card instanceof Monster)
                    GameController.getInstance().getPlayerControllerByPlayer(card.owner).moveCardToGraveYard(card);
            }
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        return action.getEvent() instanceof SummonEvent;
    }
}
