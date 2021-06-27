package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;

public class DarkHole extends Spell {

    public DarkHole(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController current = gameController.getPlayerControllerByPlayer(this.owner);
            for (int i = 1; i <= 5; i++) {
                CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, current.getPlayer());
                Monster monster = (Monster) gameController.getGame().getCardByCardAddress(cardAddress);
                if (monster != null)
                    current.moveCardToGraveYard(monster);
            }
            PlayerController opponent = gameController.getOtherPlayerController(current);
            for (int i = 1; i <= 5; i++) {
                CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, opponent.getPlayer());
                Monster monster = (Monster) gameController.getGame().getCardByCardAddress(cardAddress);
                if (monster != null)
                    opponent.moveCardToGraveYard(monster);
            }
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}