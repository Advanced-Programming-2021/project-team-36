package edu.sharif.nameless.in.seattle.yugioh.view.cardSelector;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;

public interface SelectCondition {
    public boolean canSelect(Card card);
}
