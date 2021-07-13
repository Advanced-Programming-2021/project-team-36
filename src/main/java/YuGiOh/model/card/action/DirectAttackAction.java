package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.event.DirectAttackEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.game.component.AttackingSword;
import javafx.beans.binding.DoubleBinding;

import java.util.concurrent.CompletableFuture;

public class DirectAttackAction extends Action {
    public DirectAttackAction(DirectAttackEvent event) {
        super(event, () -> {
            GameController.getInstance().decreaseLifePoint(event.getDefender(), event.getAttacker().getAttackDamage(), true);
            event.getAttacker().setAllowAttack(false);
            GameController.getInstance().checkBothLivesEndGame();
            CustomPrinter.println(String.format("<%s> declares an direct attack with <%s>", event.getDefender().getUser().getUsername(), event.getAttacker().getName()), Color.Blue);
            return CompletableFuture.completedFuture(null);
        });
    }

    public void validateEffect() throws ValidateResult {
        ValidateTree.checkDirectAttack(getEvent().getAttacker(), getEvent().getDefender());
    }

    public DirectAttackEvent getEvent() {
        return (DirectAttackEvent) super.getEvent();
    }

    @Override
    protected CompletableFuture<Void> gui() {
        return super.gui().thenCompose(res->{
            DoubleBinding x = getGameField().widthProperty().multiply(getGameField().getGameMapLocation().getDirectPlayerLocation(getEvent().getDefender()).xRatio);
            DoubleBinding y = getGameField().heightProperty().multiply(getGameField().getGameMapLocation().getDirectPlayerLocation(getEvent().getDefender()).yRatio);
            return AttackingSword.getOrCreateSwordForCard(getGameField().getCardFrameManager().getCardFrameByCard(getEvent().getAttacker())).shoot(x, y);
        });
    }
}
