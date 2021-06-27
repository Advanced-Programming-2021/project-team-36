package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

import java.util.Arrays;

public class TwinTwisters extends Spell {

    public TwinTwisters(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Card card = playerController.chooseKCards("Discard one card from your hand",
                    1,
                    Conditions.getCardFromPlayerHand(this.owner, this))[0];
            playerController.moveCardToGraveYard(card);
            boolean askUser = playerController.askRespondToQuestion("How many spell and trap card you want to destroy?", "1", "2");
            int number;
            if (askUser)
                number = 1;
            else
                number = 2;
            Arrays.stream(playerController.chooseKCards(String.format("Destroy %s spell and magic on field", number),
                    number,
                    Conditions.getMagicFromField())
            ).forEach(magicCard -> {
                GameController.getInstance().getPlayerControllerByPlayer(magicCard.owner).moveCardToGraveYard(magicCard);
            });
            CustomPrinter.println(String.format("<%s> activated <%s> successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}