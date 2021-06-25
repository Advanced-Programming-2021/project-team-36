package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.SummonEvent;

public class TorrentialTribute extends Trap {
    public TorrentialTribute(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard()) {
                if (card instanceof Monster)
                    GameController.getInstance().getPlayerControllerByPlayer(card.owner).moveCardToGraveYard(card);
            }
            GameController.getInstance().getPlayerControllerByPlayer(this.owner).moveCardToGraveYard(this);
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