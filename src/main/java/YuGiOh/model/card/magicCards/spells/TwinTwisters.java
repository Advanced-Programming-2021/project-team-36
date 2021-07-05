package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;

import java.util.Arrays;

public class TwinTwisters extends Spell {

    public TwinTwisters(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            PlayerController playerController = gameController.getPlayerControllerByPlayer(this.getOwner());
            Card card = playerController.chooseKCards("Discard one card from your hand",
                    1,
                    SelectConditions.getCardFromPlayerHand(this.getOwner(), this))[0];
            gameController.moveCardToGraveYard(card);
            boolean askUser = playerController.askRespondToQuestion("How many spell and trap card you want to destroy?", "1", "2");
            int number;
            if (askUser)
                number = 1;
            else
                number = 2;
            Arrays.stream(playerController.chooseKCards(String.format("Destroy %s spell and magic on field", number),
                    number,
                    SelectConditions.getMagicFromField())
            ).forEach(gameController::moveCardToGraveYard);
            CustomPrinter.println(String.format("<%s> activated <%s> successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}