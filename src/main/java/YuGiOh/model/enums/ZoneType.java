package YuGiOh.model.enums;

public enum ZoneType {
    DECK,
    GRAVEYARD,
    MONSTER,
    MAGIC,
    FIELD,
    HAND;

    public static ZoneType getZoneByName(String zoneName){
        if (zoneName.equals("graveyard"))
            return GRAVEYARD;
        if(zoneName.equals("monster"))
            return MONSTER;
        if(zoneName.equals("spell"))
            return MAGIC;
        if(zoneName.equals("field"))
            return FIELD;
        if(zoneName.equals("hand"))
            return HAND;
        if(zoneName.equals("deck"))
            return DECK;
        return null;
    }
}
