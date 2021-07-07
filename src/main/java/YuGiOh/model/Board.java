package YuGiOh.model;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.deck.MainDeck;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.Getter;

import java.util.*;

public class Board {
    @Getter
    private final MainDeck mainDeck;
    @Getter
    private final ObservableList<Card> graveYard;
    @Getter
    private final ObservableMap<Integer, Monster> monsterCardZone;
    @Getter
    private final ObservableMap<Integer, Magic> magicCardZone;
    @Getter
    private final ObservableList<Card> cardsOnHand;
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

    private void removeCardFromBoardIfHas(Card card) throws ModelException {
        CardAddress cardAddress = getCardAddress(card);
        if(cardAddress == null)
            throw new ModelException("this card is not in this board");
        if(cardAddress.isInDeck())
            mainDeck.getCards().remove(card);
        if(cardAddress.isInFieldZone())
            setFieldZoneCard(null);
        if(cardAddress.isInMonsterZone())
            monsterCardZone.remove(cardAddress.getId());
        if(cardAddress.isInHand())
            cardsOnHand.remove(card);
        if(cardAddress.isInGraveYard())
            graveYard.remove(card);
        if(cardAddress.isInMagicZone())
            magicCardZone.remove(cardAddress.getId());
    }

    public void moveToFieldZone(Spell spell) throws ModelException {
        CardAddress cardAddress = getCardAddress(spell);
        if(cardAddress.getZone().equals(ZoneType.FIELD))
            return;
        removeCardFromBoardIfHas(spell);
        if(getFieldZoneCard() != null)
            moveToGraveyard(getFieldZoneCard());
        setFieldZoneCard(spell);
    }
    public void moveToMonsterZone(Monster monster) throws ModelException {
        CardAddress cardAddress = getCardAddress(monster);
        if(cardAddress.getZone().equals(ZoneType.MONSTER))
            return;
        for (int i = 1; i <= 5; i++) {
            if (getMonsterCardZone().get(i) == null) {
                removeCardFromBoardIfHas(monster);
                monsterCardZone.put(i, monster);
                return;
            }
        }
        throw new ModelException("there was not enough space to put this monster");
    }
    public void moveToMagicZone(Magic magic) throws ModelException {
        CardAddress cardAddress = getCardAddress(magic);
        if(cardAddress.getZone().equals(ZoneType.MAGIC))
            return;
        if(magic instanceof Spell && ((Spell) magic).getIcon().equals(Icon.FIELD))
            throw new ModelException("this card is field and cannot be moved to magic zone");
        for (int i = 1; i <= 5; i++) {
            if (getMagicCardZone().get(i) == null) {
                removeCardFromBoardIfHas(magic);
                magicCardZone.put(i, magic);
                return;
            }
        }
        throw new ModelException("there was not enough space to put this monster");
    }
    public void moveToGraveyard(Card card) throws ModelException {
        CardAddress cardAddress = getCardAddress(card);
        if(cardAddress.getZone().equals(ZoneType.GRAVEYARD))
            return;
        removeCardFromBoardIfHas(card);
        graveYard.add(card);
        card.onMovingToGraveyard();
        CustomPrinter.println(String.format("<%s>'s Card <%s> moved to graveyard", owner.getUser().getUsername(), card.getName()), Color.Blue);
    }
    public void moveToHand(Card card) throws ModelException {
        CardAddress cardAddress = getCardAddress(card);
        if(cardAddress.getZone().equals(ZoneType.HAND))
            return;
        removeCardFromBoardIfHas(card);
        cardsOnHand.add(card);
    }

    public void moveCardNoError(Card card, ZoneType zoneType) {
        // also you can't move to deck :))
        try {
            if(zoneType.equals(ZoneType.FIELD))
                moveToFieldZone((Spell) card);
            if(zoneType.equals(ZoneType.HAND))
                moveToHand(card);
            if(zoneType.equals(ZoneType.MONSTER))
                moveToMonsterZone((Monster) card);
            if(zoneType.equals(ZoneType.MAGIC))
                moveToMagicZone((Magic) card);
            if(zoneType.equals(ZoneType.GRAVEYARD))
                moveToGraveyard(card);
        } catch (ClassCastException | ModelException ignored) {
        }
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
                return graveYard.get(cardAddress.getId()-1);
            else
                return null;
        } else if (cardAddress.isInDeck()) {
            if (1 <= cardAddress.getId() && cardAddress.getId() <= mainDeck.getCards().size())
                return mainDeck.getCards().get(cardAddress.getId()-1);
        }
        return null;
    }

    public CardAddress getCardAddress(Card card){
        final CardAddress[] ret = {null};
        monsterCardZone.forEach((id, monster)->{
            if(monster.equals(card))
                ret[0] = new CardAddress(ZoneType.MONSTER, id, owner);
        });
        magicCardZone.forEach((id, magic)->{
            if(magic.equals(card))
                ret[0] = new CardAddress(ZoneType.MAGIC, id, owner);
        });
        if(graveYard.contains(card))
            ret[0] = new CardAddress(ZoneType.GRAVEYARD, graveYard.indexOf(card) + 1, owner);
        if(card.equals(getFieldZoneCard()))
            ret[0] = new CardAddress(ZoneType.FIELD, 1, owner);
        if(cardsOnHand.contains(card))
            ret[0] = new CardAddress(ZoneType.HAND, cardsOnHand.indexOf(card) + 1, owner);
        if(mainDeck.getCards().contains(card))
            ret[0] = new CardAddress(ZoneType.DECK, mainDeck.getCards().indexOf(card) + 1, owner);
        return ret[0];
    }

    public ZoneType getCardZoneType(Card card){
        CardAddress address = getCardAddress(card);
        if(address == null)
            return null;
        return address.getZone();
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
        allCards.addAll(mainDeck.getCards());
        return allCards;
    }

    public boolean isMonsterCardZoneFull() {
        return monsterCardZone.size() == 5;
    }

    private void setFieldZoneCard(Magic fieldZoneCard) {
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
