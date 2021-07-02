package YuGiOh.model.enums;

import lombok.Getter;

public enum Phase {
    DRAW_PHASE("draw phase", "DP"),
    STANDBY_PHASE("standby phase", "SP"),
    MAIN_PHASE1("main phase 1", "MP1"),
    BATTLE_PHASE("battle phase", "BP"),
    MAIN_PHASE2("main phase 2", "MP2"),
    END_PHASE("end phase", "EP");

    @Getter
    private String verboseName;
    @Getter
    private String shortName;

    Phase(String verboseName, String shortName){
        this.verboseName = verboseName;
        this.shortName = shortName;
    }

    public Phase nextPhase(){
        if (this.equals(DRAW_PHASE))
            return STANDBY_PHASE;
        if (this.equals(STANDBY_PHASE))
            return MAIN_PHASE1;
        if (this.equals(MAIN_PHASE1))
            return BATTLE_PHASE;
        if (this.equals(BATTLE_PHASE))
            return MAIN_PHASE2;
        if (this.equals(MAIN_PHASE2))
            return END_PHASE;
        return DRAW_PHASE;
    }

    public boolean isMainPhase() {
        return this.equals(MAIN_PHASE1) || this.equals(MAIN_PHASE2);
    }
}
