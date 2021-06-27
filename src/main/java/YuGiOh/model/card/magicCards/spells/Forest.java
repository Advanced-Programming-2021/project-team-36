package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

public class Forest extends Spell {

    public Forest(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        MonsterType monsterType = monster.getMonsterType();
        if (monsterType.equals(MonsterType.INSECT) || monsterType.equals(MonsterType.BEAST) ||
                monsterType.equals(MonsterType.PLANT) || monsterType.equals(MonsterType.BEASTWARRIOR)) {
            return 200;
        }
        return 0;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        return affectionOnAttackingMonster(monster);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            CustomPrinter.println(String.format("<%s> activated field spell <%s>", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}