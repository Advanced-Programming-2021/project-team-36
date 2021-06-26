package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;

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
            for (Card card : GameController.getInstance().getGame().getAllCards()) {
                if (card instanceof Monster) {
                    Monster monster = (Monster) card;
                    if (monster.getAttribute().equals(MonsterAttribute.WATER)) {
                        monster.setAttackDamage(monster.getAttackDamage() + 500);
                        monster.setDefenseRate(monster.getDefenseRate() - 400);
                    }
                }
            }
            this.setMagicState(MagicState.OCCUPIED);
            CustomPrinter.println(String.format("<%s> activated <Umiiruka> successfully.", this.owner.getUser().getUsername()), Color.Green);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}