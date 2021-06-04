package edu.sharif.nameless.in.seattle.yugioh.model;

import edu.sharif.nameless.in.seattle.yugioh.model.enums.ZoneType;

public class CardAddress {
    private ZoneType zone;
    private int id;
    private boolean opponent;

    public CardAddress(ZoneType zone, int id, boolean opponent) {
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
    public boolean isInMagicZone() {
        return zone.equals(ZoneType.MAGIC);
    }
    public boolean isOpponentAddress() {
        return opponent;
    }
    public boolean isInFieldZone() { return zone.equals(ZoneType.FIELD); }
    public boolean isInGraveYard() { return zone.equals(ZoneType.GRAVEYARD); }
    public int getId() {
        return id;
    }
}
