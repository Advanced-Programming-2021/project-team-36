package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.SummonEvent;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;

public class TheTricky extends Monster {

    public TheTricky(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public void validateSpecialSummon() throws LogicException {
        if(!owner.hasInHand(this))
            throw new LogicException("you can only summon the tricky from your hand");
    }

    @Override
    public Action specialSummonAction() {
        PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(owner);
        return new Action(
                new SummonEvent(this, SummonType.SPECIAL),
                () -> {
                    // put discarding here
                    controller.summon(this, 0, MonsterState.OFFENSIVE_OCCUPIED, true);
                    CustomPrinter.println(String.format("<%s> special summoned <%s> successfully", owner.getUser().getUsername(), getName(), getMonsterState()), Color.Green);
                }
        );
    }

}
