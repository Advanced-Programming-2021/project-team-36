package model.deck;

public class MainDeck extends BaseDeck {
    private final Integer minimumNumberOfCards = 15;
    private final Integer maximumNumberOfCards = 45;

    public boolean isValid() {
        return minimumNumberOfCards <= cards.size() && cards.size() <= maximumNumberOfCards;
    }
}
