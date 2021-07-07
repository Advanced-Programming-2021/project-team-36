package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;

public class HeraldOfCreation extends Monster {
    public HeraldOfCreation(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    @Override
    public boolean canActivateEffect() {
        return lastTurnActivated != GameController.instance.getGame().getTurn();
    }

    @Override
    public Effect activateEffect() throws LogicException {
        if (lastTurnActivated == GameController.instance.getGame().getTurn())
            throw new LogicException("you can only activate this once in a turn");

        return () -> {
            PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
            Card discarded;
            Monster monster;
            try {
                discarded = controller.chooseKCards(
                        "choose 1 card to discard from your hand",
                        1,
                        SelectConditions.getInPlayersHandCondition(this.getOwner())
                )[0];
            } catch (ResistToChooseCard e) {
                CustomPrinter.println("canceled", Color.Default);
                return;
            }
            try {
                monster = (Monster) controller.chooseKCards(
                        "choose 1 level 7 or higher monster from your graveyard",
                        1,
                        SelectConditions.getInPlayerGraveYardMonster(this.getOwner(), 7)
                )[0];
            } catch (ResistToChooseCard e) {
                CustomPrinter.println("canceled", Color.Default);
                return;
            }
            this.getOwner().getBoard().moveCardNoError(discarded, ZoneType.GRAVEYARD);
            this.getOwner().getBoard().moveCardNoError(monster, ZoneType.HAND);
            lastTurnActivated = GameController.instance.getGame().getTurn();
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this.asEffect(), Color.Gray);
        };
    }

    @Override
    public Monster clone() {
        HeraldOfCreation cloned = (HeraldOfCreation) super.clone();
        cloned.lastTurnActivated = -1;
        return cloned;
    }
}
