package model.enums;

public enum MonsterState {
    DEFENSIVE_HIDDEN,
    DEFENSIVE_OCCUPIED,
    OFFENSIVE_OCCUPIED;

    public static MonsterState getOccupiedStateByName(String stateString){
        if (stateString.equals("attack"))
            return OFFENSIVE_OCCUPIED;
        if (stateString.equals("defense"))
            return DEFENSIVE_OCCUPIED;
        return null;
    }
}
