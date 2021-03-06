package YuGiOh.model.deck;

public class SideDeck extends BaseDeck {
    private final static Integer minimumNumberOfCards = 0;
    private final static Integer maximumNumberOfCards = 15;

    public boolean isValid() {
        return minimumNumberOfCards <= cards.size() && cards.size() <= maximumNumberOfCards;
    }

    public boolean isFull() {
        return cards.size() == maximumNumberOfCards;
    }

    @Override
    public SideDeck clone() {
        return (SideDeck) super.clone();
    }
}
