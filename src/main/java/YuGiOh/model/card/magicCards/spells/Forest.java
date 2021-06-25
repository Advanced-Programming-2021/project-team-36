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
    protected Effect getEffect() {
        return () -> {
            for (Card card : GameController.getInstance().getGame().getAllCards()) {
                if (card instanceof Monster) {
                    Monster monster = (Monster) card;
                    MonsterType monsterType = monster.getMonsterType();
                    if (monsterType.equals(MonsterType.INSECT) || monsterType.equals(MonsterType.BEAST) ||
                        monsterType.equals(MonsterType.PLANT) || monsterType.equals(MonsterType.BEASTWARRIOR)) {
                        monster.setAttackDamage(monster.getAttackDamage() + 200);
                        monster.setDefenseRate(monster.getDefenseRate() + 200);
                    }
                }
            }
            CustomPrinter.println("Forest activated successfully.", Color.Green);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return true;
    }
}