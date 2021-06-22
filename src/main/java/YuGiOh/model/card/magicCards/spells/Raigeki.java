package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

public class Raigeki extends Spell {

    public Raigeki(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public Effect activateEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController current = gameController.getPlayerControllerByPlayer(this.owner);
            PlayerController opponent = gameController.getOtherPlayerController(current);
            for (int i = 1; i <= 5; i++) {
                CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, opponent.getPlayer());
                Monster monster = (Monster) gameController.getGame().getCardByCardAddress(cardAddress);
                if (monster != null)
                    opponent.moveCardToGraveYard(monster);
            }
            CustomPrinter.println("Raigeki activated successfully.", Color.Green);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}