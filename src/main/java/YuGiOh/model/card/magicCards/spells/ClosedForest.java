package YuGiOh.model.card.magicCards.spells;

import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.Status;

public class ClosedForest extends Spell {

    public ClosedForest(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        MonsterType monsterType = monster.getMonsterType();
        if (monster.owner.equals(this.owner) && monsterType.equals(MonsterType.BEAST))
            return 100 * this.owner.getBoard().getGraveYard().size();
        return 0;
    }

    @Override
    protected Effect getEffect() {
        return () -> {
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}