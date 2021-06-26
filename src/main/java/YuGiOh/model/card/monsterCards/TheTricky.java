package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;

public class TheTricky extends Monster {

    public TheTricky(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public void validateSummon() throws LogicException {
        if (!owner.hasInHand(this))
            throw new LogicException("you can only summon from your hand");
        CustomPrinter.println("You Are special summoning the tricky ", Color.Cyan);
    }
}
