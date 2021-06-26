package YuGiOh.model.Player;

import YuGiOh.model.deck.Deck;
import YuGiOh.model.deck.MainDeck;
import YuGiOh.model.deck.SideDeck;
import lombok.Getter;
import lombok.Setter;
import YuGiOh.model.Board;
import YuGiOh.model.ModelException;
import YuGiOh.model.User;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import model.deck.*;
import YuGiOh.model.enums.Constants;
import YuGiOh.model.enums.MonsterCardType;

abstract public class Player {
    private final User user;
    private Deck deck;
    private Board board;

    @Getter
    @Setter
    private int lifePoint;

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
        this.board = new Board(deck.getMainDeck());
        this.lifePoint = Constants.InitialLifePoint.val;
        summonedInLastTurn = false;
    }

    public void refresh() {
        this.deck = user.getActiveDeck().clone().readyForBattle(this);
        this.board = new Board(deck.getMainDeck());
        this.lifePoint = Constants.InitialLifePoint.val;
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
        this.lifePoint += value;
    }
    
    public void decreaseLifePoint(int value) {
        this.lifePoint = Math.max(0, lifePoint - value);
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
}
