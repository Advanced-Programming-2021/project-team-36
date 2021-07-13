package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class PotOfGreed extends Spell {

    public PotOfGreed(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            if (this.getOwner().getBoard().getMainDeck().getTopCard() != null)
                gameController.getPlayerControllerByPlayer(this.getOwner()).drawCard();
            if (this.getOwner().getBoard().getMainDeck().getTopCard() != null)
                gameController.getPlayerControllerByPlayer(this.getOwner()).drawCard();
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}