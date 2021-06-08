package edu.sharif.nameless.in.seattle.yugioh.model;

import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Magic;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.ZoneType;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Getter;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.deck.MainDeck;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Icon;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MagicState;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterState;

import java.util.*;

public class Board {
    private final MainDeck mainDeck;
    @Getter
    private final ObservableList<Card> graveYard;
    @Getter
    private final ObservableMap<Integer, Monster> monsterCardZone;
    @Getter
    private final ObservableMap<Integer, Magic> magicCardZone;
    @Getter
    private final ObservableList<Card> cardsOnHand;

    // todo handle this without need of list
    @Getter
    private final ObservableList<Magic> fieldZoneCard;

    @Getter
    private final Player owner;


    public Board(MainDeck mainDeck, Player owner){
        this.mainDeck = mainDeck;
        graveYard = FXCollections.observableArrayList();
        monsterCardZone = FXCollections.observableHashMap();
        magicCardZone = FXCollections.observableHashMap();
        cardsOnHand = FXCollections.observableArrayList();
        fieldZoneCard = FXCollections.observableArrayList();
        fieldZoneCard.add(null);
        this.owner = owner;
    }

    public void drawCardFromDeck() {
        assert mainDeck.getTopCard() != null;
        Card card = mainDeck.getTopCard();
        mainDeck.removeCard(card);
        cardsOnHand.add(card);
    }

    public Card getCardByCardAddress(CardAddress cardAddress) {
        if (cardAddress.isInHand()) {
            if (1 <= cardAddress.getId() && cardAddress.getId() <= cardsOnHand.size())
                return cardsOnHand.get(cardAddress.getId() - 1);
            else
                return null;
        } else if (cardAddress.isInMonsterZone()) {
            return monsterCardZone.getOrDefault(cardAddress.getId(), null);
        } else if (cardAddress.isInMagicZone()) {
            return magicCardZone.getOrDefault(cardAddress.getId(), null);
        } else if (cardAddress.isInFieldZone()) {
            return getFieldZoneCard();
        } else if (cardAddress.isInGraveYard()) {
            if (1 <= cardAddress.getId() && cardAddress.getId() <= graveYard.size())
                return graveYard.get(cardAddress.getId());
            else
                return null;
        }
        return null;
    }

    public ZoneType getCardZoneType(Card card){
        if(monsterCardZone.containsValue(card))
            return ZoneType.MONSTER;
        if(magicCardZone.containsValue(card))
            return ZoneType.MAGIC;
        if(graveYard.contains(card))
            return ZoneType.GRAVEYARD;
        if(card.equals(getFieldZoneCard()))
            return ZoneType.FIELD;
        if(cardsOnHand.contains(card))
            return ZoneType.HAND;
        return null;
    }

    public List<Card> getAllCardsOnBoard() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(monsterCardZone.values());
        allCards.addAll(magicCardZone.values());
        if(getFieldZoneCard() != null)
            allCards.add(getFieldZoneCard());
        return allCards;
    }

    public List<Card> getAllCards(){
        List<Card> allCards = getAllCardsOnBoard();
        allCards.addAll(graveYard);
        allCards.addAll(cardsOnHand);
        return allCards;
    }

    public void addCardToBoard(Card card, CardAddress cardAddress) {
        if (cardAddress.isInFieldZone()) {
            if (getFieldZoneCard() != null)
                moveCardToGraveYard(getFieldZoneCard());
            setFieldZoneCard((Magic) card);
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

    public void addMagic(Magic magic) {
        if (magic.getIcon().equals(Icon.FIELD))
            addCardToBoard((Card) magic, new CardAddress(ZoneType.FIELD, 1, owner));
        else {
            for (int i = 1; i <= 5; i++) {
                if (getMagicCardZone().get(i) == null) {
                    addCardToBoard(magic, new CardAddress(ZoneType.MAGIC, i, owner));
                    break;
                }
            }
        }
    }

    public void addCardToHand(Card card){
        cardsOnHand.add(card);
    }
    public void removeFromHand(Card card){
        cardsOnHand.remove(card);
    }

    public void moveCardToGraveYard(Card card) {
        // todo this choosing should be based on pointer value not equals method!
        if (getFieldZoneCard() == card) {
            graveYard.add(card);
            setFieldZoneCard(null);
        }
        else if (magicCardZone.containsValue(card)) {
            graveYard.add(card);
            magicCardZone.values().remove(card);
        }
        else if (monsterCardZone.containsValue(card)) {
            graveYard.add(card);
            monsterCardZone.values().remove(card);
        }
        else if (cardsOnHand.contains(card)) {
            graveYard.add(card);
            cardsOnHand.remove(card);
        }
    }

    public boolean isMonsterCardZoneFull() {
        return monsterCardZone.size() == 5;
    }

    public void setFieldZoneCard(Magic fieldZoneCard) {
        this.fieldZoneCard.set(0, fieldZoneCard);
    }
    public Magic getFieldZoneCard() {
        return this.fieldZoneCard.get(0);
    }
    public ObservableList<Magic> getFieldZoneCardObservableList(){
        return fieldZoneCard;
    }

    public String toRotatedString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(("\tc").repeat(cardsOnHand.size()));
        stringBuilder.append("\n");
        stringBuilder.append(mainDeck.getNumberOfCards());
        stringBuilder.append("\n");
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(4, 2, 1, 3, 5));
        for (int id : arrayList) {
            if (magicCardZone.get(id) == null)
                stringBuilder.append("\tE");
            else if (magicCardZone.get(id).getState() == MagicState.HIDDEN)
                stringBuilder.append("\tH");
            else
                stringBuilder.append("\tO");
        }
        stringBuilder.append("\n");
        for (int id : arrayList) {
            if (monsterCardZone.get(id) == null)
                stringBuilder.append("\tE");
            else if (monsterCardZone.get(id).getMonsterState() == MonsterState.DEFENSIVE_HIDDEN)
                stringBuilder.append("\tDH");
            else if (monsterCardZone.get(id).getMonsterState() == MonsterState.DEFENSIVE_OCCUPIED)
                stringBuilder.append("\tDO");
            else
                stringBuilder.append("\tOO");
        }
        stringBuilder.append("\n");
        stringBuilder.append(graveYard.size()).append("\t".repeat(6));
        if (fieldZoneCard == null)
            stringBuilder.append("E");
        else
            stringBuilder.append("O");
        return stringBuilder.toString();
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (getFieldZoneCard() == null)
            stringBuilder.append("E");
        else
            stringBuilder.append("O");
        stringBuilder.append("\t".repeat(6));
        stringBuilder.append(graveYard.size()).append("\n");
        stringBuilder.append("\t");
        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(5, 3, 1, 2, 4));
        for (int id : arrayList) {
            if (monsterCardZone.get(id) == null)
                stringBuilder.append("E\t");
            else if (monsterCardZone.get(id).getMonsterState() == MonsterState.DEFENSIVE_HIDDEN)
                stringBuilder.append("DH\t");
            else if (monsterCardZone.get(id).getMonsterState() == MonsterState.DEFENSIVE_OCCUPIED)
                stringBuilder.append("DO\t");
            else
                stringBuilder.append("OO\t");
        }
        stringBuilder.append("\n\t");
        for (int id : arrayList) {
            if (magicCardZone.get(id) == null)
                stringBuilder.append("E\t");
            else if (magicCardZone.get(id).getState() == MagicState.HIDDEN)
                stringBuilder.append("H\t");
            else
                stringBuilder.append("O\t");
        }
        stringBuilder.append("\n");
        stringBuilder.append("\t".repeat(6));
        stringBuilder.append(mainDeck.getNumberOfCards());
        stringBuilder.append("\n");
        stringBuilder.append(("\tc").repeat(cardsOnHand.size()));
        return stringBuilder.toString();
    }
}
