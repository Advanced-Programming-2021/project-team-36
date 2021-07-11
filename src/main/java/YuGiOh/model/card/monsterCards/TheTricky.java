package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.action.SummonAction;
import YuGiOh.model.card.action.ValidateResult;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Monster;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.cardSelector.SelectConditions;

public class TheTricky extends Monster {

    public TheTricky(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public void validateSpecialSummon() throws ValidateResult {
        if (!getOwner().hasInHand(this))
            throw new ValidateResult("you can only summon the tricky from your hand");
    }

    @Override
    public SummonAction specialSummonAction() {
        PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(getOwner());
        SelectCondition condition = SelectConditions.and(
                SelectConditions.or(
                        SelectConditions.getOnPlayersBoard(getOwner()),
                        SelectConditions.getInPlayersHandCondition(getOwner())
                ),
                SelectConditions.getNotThisCard(this)
        );
        return new SummonAction(
                new SummonEvent(this.getOwner(), this, SummonType.SPECIAL, 1, condition)
        );
    }
}
