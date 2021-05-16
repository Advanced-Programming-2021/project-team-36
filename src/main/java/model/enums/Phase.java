package model.enums;

public enum Phase {
    DRAW_PHASE("draw phase"),
    STANDBY_PHASE("standby phase"),
    MAIN_PHASE1("main phase 1"),
    BATTLE_PHASE("battle phase"),
    MAIN_PHASE2("main phase 2"),
    END_PHASE("end phase");

    public String verboseName;
    Phase(String verboseName){
        this.verboseName = verboseName;
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
