package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class SolemnWarning extends Trap {
    public SolemnWarning(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            GameController gameController = GameController.getInstance();
            gameController.moveCardToGraveYard(this);
            Action action = getChain().pop();
            GameController.getInstance().decreaseLifePoint(getOwner(), 2000, false);
            Monster monster = ((SummonEvent) action.getEvent()).getMonster();
            gameController.moveCardToGraveYard(monster);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
            GameController.getInstance().checkBothLivesEndGame();
            return CompletableFuture.completedFuture(null);
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
