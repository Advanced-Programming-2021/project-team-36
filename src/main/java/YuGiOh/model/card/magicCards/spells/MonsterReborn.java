package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.SummonAction;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;

import java.util.ArrayList;
import java.util.List;

public class MonsterReborn extends Spell {

    public MonsterReborn(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            assert canActivateEffect();
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
            return playerController.chooseKCards("Choose 1 monster in GraveYard to special summon it",
                    1,
                    SelectConditions.getMonsterFromGraveYard()
            ).thenApply(cards ->
                    (Monster) cards.get(0)
            ).thenCompose(monster -> {
                SummonAction action = new SummonAction(
                        new SummonEvent(this.getOwner(), monster, SummonType.SPECIAL, 0, SelectConditions.noCondition)
                );
                return action.runEffect(()-> {
                    CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                    CustomPrinter.println(this, Color.Gray);
                }, ()->{});
            });
        };
    }

    @Override
    public boolean canActivateEffect() {
        Player current = this.getOwner();
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
        Player opponent = GameController.getInstance().getOtherPlayerController(playerController).getPlayer();
        List<Card> cards = new ArrayList<>();
        cards.addAll(current.getBoard().getGraveYard());
        cards.addAll(opponent.getBoard().getGraveYard());
        for (Card card : cards) {
            if (card instanceof Monster) {
                return true;
            }
        }
        return false;
    }
}