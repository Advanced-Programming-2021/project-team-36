package model;

import model.card.Card;
import model.card.Magic;
import model.card.Monster;
import model.enums.*;
import java.util.Random;

public class Game {
    private final Player firstPlayer;
    private final Player secondPlayer;
    private int turn;
    private Phase phase;
    private boolean summonedInThisTurn;
    private CardAddress selectedCardAddress;

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
        this.phase = Phase.STANDBYPHASE;
        this.summonedInThisTurn = false;
        this.selectedCardAddress = null;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public boolean isFirstTurn() {
        return turn == 0;
    }

    public void setSummonedInThisTurn(boolean status) {
        summonedInThisTurn = status;
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

    public CardAddress getSelectedCardAddress() {
        return selectedCardAddress;
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

    public void selectCard(CardAddress cardAddress) {
        selectedCardAddress = cardAddress;
    }

    public void unselectCard() {
        selectedCardAddress = null;
    }

    public boolean isAnyCardSelected() {
        return selectedCardAddress != null;
    }

    public boolean canSelectedCardSummon() {
        if (!selectedCardAddress.isInHand())
            return false;
        Card card = getCardByCardAddress(selectedCardAddress);
        if (card instanceof Magic)
            return false;
        Monster monster = (Monster) card;
        return monster.canSummonNormally();
    }

    public void changeTurn() {
        turn++;
    }
}
