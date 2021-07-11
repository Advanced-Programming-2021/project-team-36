package YuGiOh.view.cardSelector;

import YuGiOh.model.card.Card;

public interface SelectCondition {
    public boolean canSelect(Card card);
}
