package model;

import model.card.Card;
import model.card.Magic;
import model.card.Monster;
import model.enums.*;

public class Game {
    private final Player firstPlayer;
    private final Player secondPlayer;
    private boolean turn;
    private Phase phase;
    private boolean summonedInThisTurn;
    private CardAddress selectedCardAddress;

    public Game(Player firstPlayer, Player secondPlayer){
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.turn = false;
        this.phase = Phase.DRAWPHASE;
        this.summonedInThisTurn = false;
        this.selectedCardAddress = null;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
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
        if (!turn)
            return firstPlayer;
        else
            return secondPlayer;
    }

    public Player getOpponentPlayer() {
        if (!turn)
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
        turn = !turn;
    }
}
