package model;

import model.enums.ZoneType;

public class CardAddress {
    private ZoneType zone;
    private int id;
    private boolean opponent;

    public CardAddress(ZoneType zone, Integer id, boolean opponent) {
        this.zone = zone;
        this.id = id;
        this.opponent = opponent;
    }

    public boolean isInHand() {
        return zone.equals(ZoneType.HAND);
    }
    public boolean isInMonsterZone() {
        return zone.equals(ZoneType.MONSTER);
    }
    public boolean isInSpellZone() {
        return zone.equals(ZoneType.MAGIC);
    }
    public boolean isOpponentAddress() {
        return opponent;
    }
    public int getId() {
        return id;
    }
}
