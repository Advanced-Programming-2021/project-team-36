package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

public class MagnumShield extends Spell {

    public MagnumShield(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        return monster.getDefenseRateOnCard();
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        return monster.getAttackDamageOnCard();
    }

    @Override
    public Effect getEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Monster monster = (Monster) playerController.chooseKCards("Equip this <MagnumShield> to a monster on your field",
                    1,
                    Conditions.getPlayerMonsterFromMonsterZone(this.owner))[0];
            setEquippedMonster(monster);
            CustomPrinter.println(String.format("<%s> equipped <%s> to monster <%s>", this.owner.getUser().getUsername(), this.getName(), monster.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        for (int i = 1; i <= 5; i++) {
            CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, this.owner);
            if (GameController.getInstance().getGame().getCardByCardAddress(cardAddress) != null)
                return true;
        }
        return !isFacedUp();
    }
}