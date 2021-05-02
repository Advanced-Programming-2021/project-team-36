package model;

import model.card.Card;
import model.enums.*;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private boolean turn = false;
    private Phase phase;
    private Card selectedCard;

//    public Phase getPhase() {
//        return phase;
//    }
//
//    public Card getCardByCardAddress(CardAddress cardAddress) {
//
//    }
//
//    public Card getSelectedCard() {
//        return selectedCard;
//    }
//
//    public Player getCurrentPlayer() {
//        if (!turn)
//            return firstPlayer;
//        else
//            return secondPlayer;
//    }
//
//    public Player getOpponentPlayer() {
//        if (!turn)
//            return secondPlayer;
//        else
//            return firstPlayer;
//    }
//
//    public void selectCard(CardAddress cardAddress) {
//
//    }
//
//    public void unselectCard(CardAddress cardAddress) {
//
//    }
//
//    public boolean isCardSelected() {
//        return selectedCard != null;
//    }
//
//    void changeTurn() {
//        turn = !turn;
//    }
}
