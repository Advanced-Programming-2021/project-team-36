package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class ExploderDragon extends Monster {
    public ExploderDragon(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public CompletableFuture<Void> specialEffectWhenBeingAttacked(Monster attacker) {
        int myLifePoint = this.getOwner().getLifePoint();
        int opponentLifePoint = attacker.getOwner().getLifePoint();
        try {
            damageStep(attacker);
        } catch (RoundOverExceptionEvent ignored) {
        }
        if (GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD)) {
            attacker.tryToSendToGraveYardOfMe();
        }
        this.getOwner().setLifePoint(myLifePoint);
        attacker.getOwner().setLifePoint(opponentLifePoint);
        CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
        CustomPrinter.println(this.asEffect(), Color.Gray);
        return CompletableFuture.completedFuture(null);
    }
}
