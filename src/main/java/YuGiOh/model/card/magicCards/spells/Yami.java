package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.Status;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;

public class Yami extends Spell {

    public Yami(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        MonsterType type = monster.getMonsterType();
        if (type.equals(MonsterType.FIEND) || type.equals(MonsterType.SPELLCASTER))
            return 200;
        if (type.equals(MonsterType.FAIRY))
            return -200;
        return 0;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        return affectionOnAttackingMonster(monster);
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
