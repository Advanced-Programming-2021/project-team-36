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
    public Effect activateEffect() {
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
            CustomPrinter.println("Umiiruka activated successfully.", Color.Green);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}