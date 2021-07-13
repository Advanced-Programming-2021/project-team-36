package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class HarpiesFeatherDuster extends Spell {

    public HarpiesFeatherDuster(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController current = gameController.getPlayerControllerByPlayer(this.getOwner());
            PlayerController opponent = gameController.getOtherPlayerController(current);
            for (int i = 1; i <= 5; i++) {
                CardAddress cardAddress = new CardAddress(ZoneType.MAGIC, i, opponent.getPlayer());
                Magic magic = (Magic) gameController.getGame().getCardByCardAddress(cardAddress);
                if (magic != null)
                    gameController.moveCardToGraveYard(magic);
            }
            if (opponent.getPlayer().getBoard().getFieldZoneCard() != null)
                gameController.moveCardToGraveYard(opponent.getPlayer().getBoard().getFieldZoneCard());
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