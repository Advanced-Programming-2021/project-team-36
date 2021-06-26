package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

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
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Monster monster = (Monster) playerController.chooseKCards("Equip this <Sword of Dark Destruction> to a monster on your field",
                    1,
                    Conditions.getPlayerMonsterFromMonsterZone(this.owner))[0];
            setEquippedMonster(monster);
        };
    }

    @Override
    public boolean canActivateEffect() {
        for (int i = 1; i <= 5; i++) {
            CardAddress cardAddress = new CardAddress(ZoneType.MONSTER, i, this.owner);
            if (GameController.getInstance().getGame().getCardByCardAddress(cardAddress) != null)
                return true;
        }
        return false;
    }
}