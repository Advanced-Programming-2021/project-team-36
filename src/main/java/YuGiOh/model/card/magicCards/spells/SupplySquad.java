package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class SupplySquad extends Spell {

    public SupplySquad(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    private boolean isActivated = false;

    @Override
    public void onDestroyMyMonster() {
        if (!isActivated) {
            isActivated = true;
            GameController gameController = GameController.getInstance();
            gameController.getPlayerControllerByPlayer(this.getOwner()).drawCard();
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully. ", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        }
    }

    @Override
    public void startOfNewTurn() {
        isActivated = false;
    }

    @Override
    protected Effect getEffect() {
        return () -> CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean canActivateEffect() {
        return false;
    }
}