package model;

import model.deck.*;
import model.enums.Constants;

public class Player {
    private final User user;
    private final Deck deck;
    private final Board board;
    private int lifePoint;

    public Player(User user){
        this.user = user;
        this.deck = user.getActiveDeck();
        this.board = new Board(user.getActiveDeck().getMainDeck());
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
        this.lifePoint -= value;
    }
}
