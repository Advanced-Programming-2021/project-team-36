package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Game;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.view.cardSelector.Conditions;

public class BlackPendant extends Spell {

    public BlackPendant(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        if (getEquippedMonster().equals(monster))
            return 500;
        return 0;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        if (getEquippedMonster().equals(monster))
            return 500;
        return 0;
    }

    @Override
    public Effect activateEffect() {
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Monster monster = (Monster) playerController.chooseKCards("Equip this <Black Pendant> to a monster on your field",
                    1,
                    Conditions.getPlayerMonsterFromMonsterZone(this.owner))[0];
            setEquippedMonster(monster);
        };
    }

    @Override
    public void deactivate() {
        Player opponent = GameController.getInstance().getGame().getOtherPlayer(this.owner);
        GameController.getInstance().decreaseLifePoint(opponent, 500);
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