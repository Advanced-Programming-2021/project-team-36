package model;

import model.deck.*;

public class Player {
    private User user;
    private Deck deck;
    private Board board;
    private Integer lifePoint;

    public Player(User user){
        // to something
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

    public Integer getLifePoint() {
        return lifePoint;
    }

    public void increaseLifePoint(Integer value) {
        this.lifePoint += value;
    }
    
    public void decreaseLifePoint(Integer value) {
        this.lifePoint -= value;
    }
}
