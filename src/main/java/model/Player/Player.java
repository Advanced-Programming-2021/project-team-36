package model.Player;

import lombok.Getter;
import lombok.Setter;
import model.Board;
import model.ModelException;
import model.User;
import model.card.Card;
import model.card.Monster;
import model.deck.*;
import model.enums.Constants;
import model.enums.MonsterCardType;

abstract public class Player {
    private final User user;
    private final Deck deck;
    private final Board board;

    @Getter
    @Setter
    private int lifePoint;

    public Player(User user) throws ModelException {
        if (user.getActiveDeck() == null)
            throw new ModelException(String.format("%s has no active deck", user.getUsername()));
        // todo remove this comments and add some tests that are compatible with this condition
//        if (!user.getActiveDeck().getMainDeck().isValid())
//            throw new ModelException(String.format("%s's active deck is not valid", user.getUsername()));
        this.user = user;
        this.deck = user.getActiveDeck().clone().readyForBattle(this);
        this.board = new Board(deck.getMainDeck());
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

    public void increaseLifePoint(int value) {
        this.lifePoint += value;
    }
    
    public void decreaseLifePoint(int value) {
        this.lifePoint = Math.max(0, lifePoint - value);
    }

    public boolean hasInHand(Card card){
        // todo implement this function with the help of POINTERS not EQUAL function
        return true;
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
}
