package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.model.card.Monster;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class YomiShip extends Monster {
    public YomiShip(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public CompletableFuture<Void> specialEffectWhenBeingAttacked(Monster attacker) {
        damageStep(attacker);
        if (GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD)) {
            GameController.getInstance().moveCardToGraveYard(attacker);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this.asEffect(), Color.Gray);
        }
        return CompletableFuture.completedFuture(null);
    }
}
