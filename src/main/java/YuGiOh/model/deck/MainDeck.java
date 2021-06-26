package YuGiOh.model.deck;

public class MainDeck extends BaseDeck {
    private final static Integer minimumNumberOfCards = 5;
    private final static Integer maximumNumberOfCards = 45;

    public boolean isValid() {
        return minimumNumberOfCards <= cards.size() && cards.size() <= maximumNumberOfCards;
    }

    public boolean isFull() { return cards.size() == maximumNumberOfCards; }

    @Override
    public MainDeck clone() {
        return (MainDeck) super.clone();
    }
}
