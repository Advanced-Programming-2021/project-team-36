package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;

public class MysticalSpaceTyphoon extends Spell {

    public MysticalSpaceTyphoon(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController playerController = gameController.getPlayerControllerByPlayer(this.getOwner());
            Magic magic = (Magic) playerController.chooseKCards("Destroy one spell or trap on field",
                    1,
                    SelectConditions.getMagicFromField(this))[0];
            gameController.moveCardToGraveYard(magic);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard())
            if (card instanceof Magic && card != this)
                return true;
        return false;
    }
}