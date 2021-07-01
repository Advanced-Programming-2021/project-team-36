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
import YuGiOh.view.cardSelector.Conditions;

public class MysticalSpaceTyphoon extends Spell {

    public MysticalSpaceTyphoon(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Magic magic = (Magic) playerController.chooseKCards("Destroy one spell or trap on field",
                    1,
                    Conditions.getMagicFromField())[0];
            PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(magic.owner);
            controller.moveCardToGraveYard(magic);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}