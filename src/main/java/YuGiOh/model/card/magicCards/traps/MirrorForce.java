package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.event.AttackEvent;
import YuGiOh.model.card.action.Effect;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class MirrorForce extends Trap {

    public MirrorForce(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            for (Card card : GameController.getInstance().getGame().getOtherPlayer(this.getOwner()).getBoard().getAllCardsOnBoard()) {
                if (card instanceof Monster) {
                    Monster monster = (Monster) card;
                    if (monster.getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
                        GameController.getInstance().moveCardToGraveYard(card);
                }
            }
            GameController.getInstance().moveCardToGraveYard(this);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        if (action.getEvent() instanceof AttackEvent) {
            AttackEvent event = (AttackEvent) action.getEvent();
            return event.getAttacker().getOwner().equals(GameController.getInstance().getGame().getOtherPlayer(this.getOwner()));
        }
        return false;
    }
}
