package model;

import model.deck.*;
import model.enums.Constants;

public class Player {
    private final User user;
    private final Deck deck;
    private final Board board;
    private int lifePoint;

    public Player(User user) throws ModelException {
        if (user.getActiveDeck() == null)
            throw new ModelException(String.format("%s has no active deck", user.getUsername()));
//        if (!user.getActiveDeck().getMainDeck().isValid())
//            throw new ModelException(String.format("%s's active deck is not valid", user.getUsername()));
        this.user = user;
        this.deck = user.getActiveDeck().clone();
        this.board = new Board(user.getActiveDeck().getMainDeck().clone());
        // the deck in board and this.deck are cloned and different objects
        this.lifePoint = Constants.InitialLifePoint.val;
    }

    public User getUser() {
        return user;
    }

    public MainDeck getMainDeck() {
        return deck.getMainDeck();
    }

    public SideDeck getSideDeck() {
        return deck.getSideDeck();
    }

    public Board getBoard() {
        return board;
    }

    public int getLifePoint() {
        return lifePoint;
    }

    public void increaseLifePoint(int value) {
        this.lifePoint += value;
    }
    
    public void decreaseLifePoint(int value) {
        this.lifePoint = Math.max(0, value);
    }
}
