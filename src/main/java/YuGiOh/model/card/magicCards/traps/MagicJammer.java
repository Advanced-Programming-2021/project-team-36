package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;

public class MagicJammer extends Trap {
    public MagicJammer(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController playerController = gameController.getPlayerControllerByPlayer(this.getOwner());
            gameController.moveCardToGraveYard(this);
            Card card = playerController.chooseKCards("Discard 1 card to activate Magic Jamamer",
                    1,
                    SelectConditions.getCardFromPlayerHand(this.getOwner(), this))[0];
            gameController.moveCardToGraveYard(card);
            Action action = getChain().pop();
            Card card1 = ((MagicActivation) action.getEvent()).getMagic();
            gameController.moveCardToGraveYard(card1);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        return action.getEvent() instanceof MagicActivation && this.getOwner().getBoard().getCardsOnHand().size() > 0;
    }
}
