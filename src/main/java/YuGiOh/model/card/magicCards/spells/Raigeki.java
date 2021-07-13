package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class Raigeki extends Spell {

    public Raigeki(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController current = gameController.getPlayerControllerByPlayer(this.getOwner());
            PlayerController opponent = gameController.getOtherPlayerController(current);
            for (int i = 1; i <= 5; i++) {
                CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, opponent.getPlayer());
                Monster monster = (Monster) gameController.getGame().getCardByCardAddress(cardAddress);
                if (monster != null)
                    gameController.moveCardToGraveYard(monster);
            }
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