package YuGiOh.model.enums;

import lombok.Getter;

public enum MonsterState {
    DEFENSIVE_HIDDEN("defensive hidden"),
    DEFENSIVE_OCCUPIED("defensive occupied"),
    OFFENSIVE_OCCUPIED("offensive occupied");

    @Getter
    String name;
    MonsterState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static MonsterState getOccupiedStateByName(String stateString){
        if (stateString.equals("attack"))
            return OFFENSIVE_OCCUPIED;
        if (stateString.equals("defense"))
            return DEFENSIVE_OCCUPIED;
        return null;
    }
}
