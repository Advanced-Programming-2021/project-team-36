package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.card.action.SummonEvent;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.view.cardSelector.Conditions;
import YuGiOh.view.cardSelector.SelectCondition;

public class MagicJammer extends Trap {
    public MagicJammer(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            playerController.moveCardToGraveYard(this);
            Card card = playerController.chooseKCards("Discard 1 card to activate Magic Jamamer",
                    1,
                    Conditions.getCardFromPlayerHand(this.owner, this))[0];
            playerController.moveCardToGraveYard(card);
            Action action = getChain().pop();
            Card card1 = ((MagicActivation) action.getEvent()).getCard();
            GameController.getInstance().getOtherPlayerController(playerController).moveCardToGraveYard(card1);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        return action.getEvent() instanceof MagicActivation && this.owner.getBoard().getCardsOnHand().size() > 0;
    }
}