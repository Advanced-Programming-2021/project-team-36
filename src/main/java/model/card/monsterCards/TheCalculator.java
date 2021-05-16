package model.card.monsterCards;

import controller.GameController;
import model.card.Card;
import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterState;
import model.enums.MonsterType;

public class TheCalculator extends Monster {
    public TheCalculator(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    // todo check if this is correct. sum of levels? really?

    @Override
    public int getAttackDamage() {
        int sum = 0;
        for (Card card : this.owner.getBoard().getAllCardsOnBoard()) {
            if (card instanceof Monster && ((Monster) card).isFacedUp()) {
                sum += ((Monster) card).getLevel();
            }
        }
        return sum * 300;
    }
}
