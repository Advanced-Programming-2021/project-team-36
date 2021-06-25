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
import YuGiOh.view.cardSelector.Conditions;

public class UnitedWeStand extends Spell {

    public UnitedWeStand(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster1) {
        int affect = 0;
        for (int i = 1; i <= 5; i++) {
            Monster monster = this.owner.getBoard().getMonsterCardZone().get(i);
            if (monster != null && monster.isFacedUp())
                affect += 800;
        }
        return affect;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        return affectionOnAttackingMonster(monster);
    }

    @Override
    public Effect getEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Monster monster = (Monster) playerController.chooseKCards("Equip this <United We Stand> to a monster on your field",
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