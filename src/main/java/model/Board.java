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
    private Card fieldZoneCard;

    public List<Card> getGraveYard() {
        return graveYard;
    }

    public Map<Integer, Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public Map<Integer, Card> getMonsterCardZone() {
        return monsterCardZone;
    }

    public Map<Integer, Card> getSpellAndTrapCardZone() {
        return spellAndTrapCardZone;
    }

    public Card getCardByCardAddress(CardAddress cardAddress) {
        if (cardAddress.isInHand())
            return cardsOnHand.get(cardAddress.getId());
        else if (cardAddress.isInMonsterZone())
            return monsterCardZone.get(cardAddress.getId());
        else if (cardAddress.isInSpellZone())
            return spellAndTrapCardZone.get(cardAddress.getId());
        else if (cardAddress.isInFieldZone())
            return fieldZoneCard;
        else
            return graveYard.get(cardAddress.getId());
    }
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
    public boolean isMonsterCardZoneFull() {
        return monsterCardZone.size() == 5;
    }
//
//    public void finishTurn() {
//
//    }
}
