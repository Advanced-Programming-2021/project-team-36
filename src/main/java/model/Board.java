package model;

import model.card.Card;
import model.card.Monster;
import model.deck.MainDeck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private MainDeck mainDeck;
    private List<Card> graveYard = new ArrayList<>();
    private Map<Integer, Card> monsterCardZone = new HashMap<>();
    private Map<Integer, Card> spellAndTrapCardZone = new HashMap<>();
    private Map<Integer, Card> cardsOnHand = new HashMap<>();

//    public CardAddress getCardAddressByCard(Card card) {
//
//    }
//
//    public List<Card> getAllCardsOnField() {
//
//    }
//
//    public void addCardToBoard(Card card, CardAddress cardAddress) {
//
//    }
//
//    public void moveCardToGraveYard(CardAddress cardAddress) {
//
//    }
//
//    public void summonMonster(Monster monsterCard) {
//
//    }
//
//    public boolean isMonsterCardZoneFull() {
//        return monsterCardZone.size() == 5;
//    }
//
//    public void finishTurn() {
//
//    }

}
