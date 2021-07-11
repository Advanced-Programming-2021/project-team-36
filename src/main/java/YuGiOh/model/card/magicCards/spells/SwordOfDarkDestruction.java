package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;

public class SwordOfDarkDestruction extends Spell {

    public SwordOfDarkDestruction(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        if (getEquippedMonster().equals(monster) && monster.getAttribute().equals(MonsterAttribute.DARK))
            return 400;
        return 0;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        if (getEquippedMonster().equals(monster) && monster.getAttribute().equals(MonsterAttribute.DARK))
            return 200;
        return 0;
    }

    @Override
    public Effect getEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
            Monster monster = (Monster) playerController.chooseKCards("Equip this <SwordofDarkDestruction> to a monster on your field",
                    1,
                    SelectConditions.getPlayerMonsterFromMonsterZone(this.getOwner()))[0];
            setEquippedMonster(monster);
            CustomPrinter.println(String.format("<%s> equipped <%s> to monster <%s>", this.getOwner().getUser().getUsername(), this.getName(), monster.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
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