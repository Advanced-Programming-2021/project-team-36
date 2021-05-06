package model;

import model.card.Card;
import model.card.Magic;
import model.card.Monster;
import model.deck.MainDeck;
import model.enums.MagicState;
import model.enums.MonsterState;

import java.util.*;

public class Board {
    private MainDeck mainDeck;
    private List<Card> graveYard = new ArrayList<>();
    private Map<Integer, Monster> monsterCardZone = new HashMap<>();
    private Map<Integer, Magic> spellAndTrapCardZone = new HashMap<>();
    private Map<Integer, Card> cardsOnHand = new HashMap<>();
    private Card fieldZoneCard;

    public List<Card> getGraveYard() {
        return graveYard;
    }

    public Map<Integer, Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public Map<Integer, Monster> getMonsterCardZone() {
        return monsterCardZone;
    }

    public Map<Integer, Magic> getSpellAndTrapCardZone() {
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (fieldZoneCard == null)
            stringBuilder.append("E");
        else
            stringBuilder.append("O");
        stringBuilder.append("\t".repeat(11));
        stringBuilder.append(graveYard.size()).append("\n");
        stringBuilder.append("\t".repeat(2));
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(5, 3, 1, 2, 4));
        for (int id : arrayList) {
            if (monsterCardZone.get(id) == null)
                stringBuilder.append("E");
            else if (monsterCardZone.get(id).getState() == MonsterState.DEFENSIVE_HIDDEN)
                stringBuilder.append("DH");
            else if (monsterCardZone.get(id).getState() == MonsterState.DEFENSIVE_OCCUPIED)
                stringBuilder.append("DO");
            else
                stringBuilder.append("OO");
        }
        stringBuilder.append("\n");
        for (int id : arrayList) {
            if (spellAndTrapCardZone.get(id) == null)
                stringBuilder.append("E");
            else if (spellAndTrapCardZone.get(id).getState() == MagicState.HIDDEN)
                stringBuilder.append("H");
            else
                stringBuilder.append("O");
        }
        stringBuilder.append("\n");
        stringBuilder.append("\t".repeat(12));
        stringBuilder.append(mainDeck.getNumberOfCards());
        stringBuilder.append("\n");
        stringBuilder.append("c\t".repeat(cardsOnHand.size()));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
