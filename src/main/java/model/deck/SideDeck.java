package model.deck;

import model.deck.BaseDeck;

public class SideDeck extends BaseDeck {
    private final Integer minimumNumberOfCards = 0;
    private final Integer maximumNumberOfCards = 15;

    public boolean isValid() {
        return minimumNumberOfCards <= cards.size() && cards.size() <= maximumNumberOfCards;
    }
}
