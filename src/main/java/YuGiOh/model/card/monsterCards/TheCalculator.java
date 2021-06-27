package YuGiOh.model.card.monsterCards;

import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;

public class TheCalculator extends Monster {
    public TheCalculator(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public int getAttackDamageOnCard() {
        if(!isInBattle())
            return attackDamage;
        int sum = 0;
        for (Card card : this.owner.getBoard().getAllCardsOnBoard()) {
            if (card instanceof Monster && card.isFacedUp() && card.owner.equals(this.owner)) {
                sum += ((Monster) card).getLevel();
            }
        }
        return sum * 300;
    }
}
