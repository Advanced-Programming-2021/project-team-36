package YuGiOh.model.card.magicCards.spells;

import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

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
            CustomPrinter.println(String.format("<%s> activated field spell <%s>", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}