package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.event.MonsterAttackEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.game.component.AttackingSword;
import javafx.beans.binding.DoubleBinding;

import java.util.concurrent.CompletableFuture;

public class MonsterAttackAction extends Action {
    public MonsterAttackAction(MonsterAttackEvent event) {
        super(event, ()->{
            CustomPrinter.println(String.format("<%s> declares an attack with <%s> to <%s>'s <%s>", event.getAttacker().getOwner().getUser().getUsername(), event.getAttacker().getName(), event.getDefender().getOwner().getUser().getUsername(), event.getDefender().getName()), Color.Blue);
            return event.getDefender().onBeingAttackedByMonster(event.getAttacker()).run().thenRun(()-> {
                GameController.getInstance().checkBothLivesEndGame();
                event.getAttacker().setAllowAttack(false);
            });
        });
    }

    public void validateEffect() throws ValidateResult {
        MonsterAttackEvent event = (MonsterAttackEvent) getEvent();
        ValidateTree.checkMonsterAttack(event.getAttacker(), event.getDefender());
        Card attacker = event.getAttacker();
    }

    public MonsterAttackEvent getEvent() {
        return (MonsterAttackEvent) super.getEvent();
    }

    @Override
    protected CompletableFuture<Void> gui() {
        return super.gui().thenCompose(res->{
            DoubleBinding x = getGameField().widthProperty().multiply(getGameField().getGameMapLocation().getLocationByCardAddress(getGameField().getCardFrameManager().getCardAddressByCard(getEvent().getDefender())).xRatio);
            DoubleBinding y = getGameField().heightProperty().multiply(getGameField().getGameMapLocation().getLocationByCardAddress(getGameField().getCardFrameManager().getCardAddressByCard(getEvent().getDefender())).yRatio);
            return AttackingSword.getOrCreateSwordForCard(getGameField().getCardFrameManager().getCardFrameByCard(getEvent().getAttacker())).shoot(x, y);
        });
    }
}
