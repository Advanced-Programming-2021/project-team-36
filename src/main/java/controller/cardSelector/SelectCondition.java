package controller.cardSelector;

import model.card.Card;

public interface SelectCondition {
    public boolean canSelect(Card card);
}
