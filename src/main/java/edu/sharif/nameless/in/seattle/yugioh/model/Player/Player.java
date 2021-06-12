package edu.sharif.nameless.in.seattle.yugioh.model.Player;

import edu.sharif.nameless.in.seattle.yugioh.model.ModelException;
import edu.sharif.nameless.in.seattle.yugioh.model.User;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.deck.Deck;
import edu.sharif.nameless.in.seattle.yugioh.model.deck.MainDeck;
import edu.sharif.nameless.in.seattle.yugioh.model.deck.SideDeck;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;
import edu.sharif.nameless.in.seattle.yugioh.model.Board;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Constants;

abstract public class Player {
    private final User user;
    private Deck deck;
    private Board board;

    private IntegerProperty lifePoint;

    @Getter
    @Setter
    private boolean summonedInLastTurn;

    public Player(User user) throws ModelException {
        if (user.getActiveDeck() == null)
            throw new ModelException(String.format("%s has no active deck", user.getUsername()));
        if (!user.getActiveDeck().getMainDeck().isValid())
            throw new ModelException(String.format("%s's active deck is not valid", user.getUsername()));
        this.user = user;
        this.deck = user.getActiveDeck().clone().readyForBattle(this);
        this.board = new Board(deck.getMainDeck(), this);
        this.lifePoint = new SimpleIntegerProperty(Constants.InitialLifePoint.val);
        summonedInLastTurn = false;
    }

    public void refresh() {
        this.deck = user.getActiveDeck().clone().readyForBattle(this);
        this.board = new Board(deck.getMainDeck(), this);
        setLifePoint(Constants.InitialLifePoint.val);
        summonedInLastTurn = false;

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

    public void increaseLifePoint(int value) {
        setLifePoint(getLifePoint() + value);
    }
    
    public void decreaseLifePoint(int value) {
        setLifePoint(Math.max(0, getLifePoint() - value));
    }

    public boolean hasInHand(Card card){
        return this.board.getCardsOnHand().contains(card);
    }

    public void moveCardToGraveYard(Card card) {
        board.moveCardToGraveYard(card);
    }

    public boolean hasAnyRitualMonsterInHand() {
        for (Card card : board.getCardsOnHand())
            if (card instanceof Monster && ((Monster) card).getMonsterCardType().equals(MonsterCardType.RITUAL))
                return true;
        return false;
    }

    public void setLifePoint(int lifePoint) {
        this.lifePoint.set(lifePoint);
    }

    public int getLifePoint() {
        return lifePoint.get();
    }

    public IntegerProperty lifePointProperty() {
        return lifePoint;
    }
}
