package model;

import lombok.Getter;
import lombok.Setter;
import model.Player.Player;
import model.card.Card;
import model.card.Effect;
import model.card.Magic;
import model.card.Monster;
import model.enums.*;

import java.util.Random;
import java.util.Stack;

public class Game {
    @Getter
    private final Player firstPlayer;
    @Getter
    private final Player secondPlayer;
    private int turn;

    @Getter
    @Setter
    private Phase phase;

    @Setter
    private boolean summonedInThisTurn;

    @Getter
    @Setter
    private Stack<Effect> chain;

    public Game(Player firstPlayer, Player secondPlayer) throws ModelException {
        if (firstPlayer.getUser().getUsername().equals(secondPlayer.getUser().getUsername()))
            throw new ModelException("you can't play with yourself");
        Random random = new Random();
        if (random.nextInt(2) == 0) {
            this.firstPlayer = firstPlayer;
            this.secondPlayer = secondPlayer;
        }
        else {
            this.firstPlayer = secondPlayer;
            this.secondPlayer = firstPlayer;
        }
        firstPlayer.getMainDeck().shuffleCards();
        secondPlayer.getMainDeck().shuffleCards();
        for (int i = 0; i < 5; i++) {
            firstPlayer.getBoard().drawCardFromDeck();
            secondPlayer.getBoard().drawCardFromDeck();
        }
        this.turn = 0;
        this.phase = Phase.MAIN_PHASE2;
        // todo in nabayad draw phase bashe?
        this.summonedInThisTurn = false;
    }

    public boolean isSummonedInThisTurn() {
        return summonedInThisTurn;
    }

    public Card getCardByCardAddress(CardAddress cardAddress) {
        if (cardAddress.isOpponentAddress())
            return getOpponentPlayer().getBoard().getCardByCardAddress(cardAddress);
        else
            return getCurrentPlayer().getBoard().getCardByCardAddress(cardAddress);
    }

    public Player getCurrentPlayer() {
        if (turn % 2 == 0)
            return firstPlayer;
        else
            return secondPlayer;
    }

    public Player getOpponentPlayer() {
        if (turn % 2 == 0)
            return secondPlayer;
        else
            return firstPlayer;
    }

    public boolean canCardBeSummoned(Card card) {
        if(!getCurrentPlayer().hasInHand(card))
            return false;
        if (card instanceof Magic)
            return false;
        Monster monster = (Monster) card;
        return monster.canSummonNormally();
    }

    public void changeTurn() {
        turn++;
    }

    public void moveCardToGraveYard(Card card){
        // exactly one of them contain this card
        firstPlayer.getBoard().moveCardToGraveYard(card);
        secondPlayer.getBoard().moveCardToGraveYard(card);
    }
}
