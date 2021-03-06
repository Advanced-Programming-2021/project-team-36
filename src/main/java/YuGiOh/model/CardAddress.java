package YuGiOh.model;

import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.ZoneType;
import lombok.Generated;
import lombok.Getter;

import java.util.Objects;

public class CardAddress {
    @Getter
    private final ZoneType zone;
    @Getter
    private final int id;
    @Getter
    private final Player owner;

    public CardAddress(ZoneType zone, int id, Player owner) {
        this.zone = zone;
        this.id = id;
        this.owner = owner;
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
    public boolean isInFieldZone() { return zone.equals(ZoneType.FIELD); }
    public boolean isInGraveYard() { return zone.equals(ZoneType.GRAVEYARD); }
    public boolean isInDeck() { return zone.equals(ZoneType.DECK); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardAddress that = (CardAddress) o;
        return id == that.id && owner.equals(that.owner) && zone == that.zone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(zone, id, owner);
    }
}
