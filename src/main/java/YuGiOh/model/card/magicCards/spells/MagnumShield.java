package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;

public class MagnumShield extends Spell {

    public MagnumShield(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        if (monster.equals(equippedMonster))
            return monster.getDefenseRateOnCard();
        return 0;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        if (monster.equals(equippedMonster))
            return monster.getAttackDamageOnCard();
        return 0;
    }

    @Override
    public Effect getEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
            return playerController.chooseKCards("Equip this <MagnumShield> to a monster on your field",
                    1,
                    SelectConditions.getPlayerMonsterFromMonsterZone(this.getOwner()))
                    .thenApply(cards -> (Monster) cards.get(0))
                    .thenAccept(monster -> {
                        setEquippedMonster(monster);
                        CustomPrinter.println(String.format("<%s> equipped <%s> to monster <%s>", this.getOwner().getUser().getUsername(), this.getName(), monster.getName()), Color.Yellow);
                        CustomPrinter.println(this, Color.Gray);
                    });
        };
    }

    @Override
    public boolean canActivateEffect() {
        for (int i = 1; i <= 5; i++) {
            CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, this.getOwner());
            if (GameController.getInstance().getGame().getCardByCardAddress(cardAddress) != null)
                return !isActivated();
        }
        return false;
    }
}