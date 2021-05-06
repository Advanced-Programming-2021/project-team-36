package model;

import model.card.Card;
import model.card.Magic;
import model.card.Monster;
import model.deck.MainDeck;
import model.enums.MagicState;
import model.enums.MonsterState;

import java.util.*;

public class Board {
    private final MainDeck mainDeck;
    private final List<Card> graveYard;
    private final Map<Integer, Monster> monsterCardZone;
    private final Map<Integer, Magic> magicCardZone;
    private final List<Card> cardsOnHand;
    private Magic fieldZoneCard;

    public Board(MainDeck mainDeck){
        this.mainDeck = mainDeck;
        graveYard = new ArrayList<>();
        monsterCardZone = new HashMap<>();
        magicCardZone = new HashMap<>();
        cardsOnHand = new ArrayList<>();
        fieldZoneCard = null;
    }

    public List<Card> getGraveYard() {
        return graveYard;
    }

    public List<Card> getCardsOnHand() {
        return cardsOnHand;
    }

    public Map<Integer, Monster> getMonsterCardZone() {
        return monsterCardZone;
    }

    public Map<Integer, Magic> getMagicCardZoneZone() {
        return magicCardZone;
    }

    public Card getCardByCardAddress(CardAddress cardAddress) {
        if (cardAddress.isInHand())
            return cardsOnHand.get(cardAddress.getId());
        else if (cardAddress.isInMonsterZone())
            return monsterCardZone.get(cardAddress.getId());
        else if (cardAddress.isInMagicZone())
            return magicCardZone.get(cardAddress.getId());
        else if (cardAddress.isInFieldZone())
            return fieldZoneCard;
        else
            return graveYard.get(cardAddress.getId());
    }

    public List<Card> getAllCardsOnBoard() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(monsterCardZone.values());
        allCards.addAll(magicCardZone.values());
        allCards.add(fieldZoneCard);
        return allCards;
    }

    public void addCardToBoard(Card card, CardAddress cardAddress) {
        if (cardAddress.isInFieldZone()) {
            if (fieldZoneCard != null)
                moveCardToGraveYard(cardAddress);
            fieldZoneCard = (Magic) card;
        }
        else if (cardAddress.isInMagicZone()) {
            assert magicCardZone.get(cardAddress.getId()) == null;
            magicCardZone.put(cardAddress.getId(), (Magic) card);
        }
        else {
            assert monsterCardZone.get(cardAddress.getId()) == null;
            monsterCardZone.put(cardAddress.getId(), (Monster) card);
        }
    }

    public void moveCardToGraveYard(CardAddress cardAddress) {
        if (cardAddress.isInFieldZone()) {
            assert fieldZoneCard != null;
            graveYard.add(fieldZoneCard);
            fieldZoneCard = null;
        }
        else if (cardAddress.isInMagicZone()) {
            assert magicCardZone.get(cardAddress.getId()) != null;
            graveYard.add(magicCardZone.get(cardAddress.getId()));
            magicCardZone.remove(cardAddress.getId());
        }
        else if (cardAddress.isInMonsterZone()) {
            assert monsterCardZone.get(cardAddress.getId()) != null;
            graveYard.add(monsterCardZone.get(cardAddress.getId()));
            monsterCardZone.remove(cardAddress.getId());
        }
        else if (cardAddress.isInHand()) {
            assert cardsOnHand.size() >= cardAddress.getId();
            graveYard.add(cardsOnHand.get(cardAddress.getId() - 1));
            cardsOnHand.remove(cardAddress.getId() - 1);
        }
        else
            assert false;
    }

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
            if (magicCardZone.get(id) == null)
                stringBuilder.append("E");
            else if (magicCardZone.get(id).getState() == MagicState.HIDDEN)
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
