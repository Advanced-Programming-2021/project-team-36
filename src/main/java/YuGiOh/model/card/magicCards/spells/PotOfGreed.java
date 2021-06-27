package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

public class PotOfGreed extends Spell {

    public PotOfGreed(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            if (this.owner.getBoard().getMainDeck().getTopCard() != null)
                gameController.getPlayerControllerByPlayer(this.owner).drawCard();
            if (this.owner.getBoard().getMainDeck().getTopCard() != null)
                gameController.getPlayerControllerByPlayer(this.owner).drawCard();
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}