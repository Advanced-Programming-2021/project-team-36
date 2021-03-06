package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class Marshmallon extends Monster {
    boolean checkIfFaceIsDown = false;

    public Marshmallon(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public void tryToSendToGraveYardOfMe() {
        CustomPrinter.println("Marshmallon is still alive! ha ha ha", Color.Cyan);
    }

    @Override
    protected void startOfBeingAttackedByMonster() {
        checkIfFaceIsDown = !isFacedUp();
    }

    @Override
    public CompletableFuture<Void> specialEffectWhenBeingAttacked(Monster attacker) {
        if (checkIfFaceIsDown) {
            GameController.getInstance().decreaseLifePoint(
                    GameController.getInstance().getGame().getOtherPlayer(this.getOwner()),
                    1000,
                    false
            );
        }
        CustomPrinter.println(String.format("<%s> activated <%s> successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
        CustomPrinter.println(this.asEffect(), Color.Gray);
        damageStep(attacker);
        return CompletableFuture.completedFuture(null);
    }
}
