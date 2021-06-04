package edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector;

import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;

public interface SelectCondition {
    public boolean canSelect(Card card);
}
