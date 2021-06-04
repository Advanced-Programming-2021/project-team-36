package edu.sharif.nameless.in.seattle.yugioh.model.card.magicCards.traps;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Trap;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Action;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.SummonEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Status;

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
