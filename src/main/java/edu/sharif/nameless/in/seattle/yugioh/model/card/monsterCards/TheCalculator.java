package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

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
