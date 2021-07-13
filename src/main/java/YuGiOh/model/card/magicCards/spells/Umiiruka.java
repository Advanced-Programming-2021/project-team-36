package YuGiOh.model.card.magicCards.spells;

import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class Umiiruka extends Spell {

    public Umiiruka(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    public int affectionOnAttackingMonster(Monster monster) {
        if (monster.getAttribute().equals(MonsterAttribute.WATER))
            return 500;
        return 0;
    }

    @Override
    public int affectionOnDefensiveMonster(Monster monster) {
        if (monster.getAttribute().equals(MonsterAttribute.WATER))
            return -400;
        return 0;
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            CustomPrinter.println(String.format("<%s> activated field spell <%s>", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}