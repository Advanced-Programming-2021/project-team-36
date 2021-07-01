package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;

public class ExploderDragon extends Monster {
    public ExploderDragon(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public void specialEffectWhenBeingAttacked(Monster attacker) {
        int myLifePoint = this.owner.getLifePoint();
        int opponentLifePoint = attacker.owner.getLifePoint();
        try {
            damageStep(attacker);
        } catch (RoundOverExceptionEvent ignored) {
        }
        if (GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD)) {
            attacker.tryToSendToGraveYardOfMe();
        }
        this.owner.setLifePoint(myLifePoint);
        attacker.owner.setLifePoint(opponentLifePoint);
        CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
        CustomPrinter.println(this.asEffect(), Color.Gray);
    }
}
