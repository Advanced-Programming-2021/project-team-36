package YuGiOh.model.card.event;

import YuGiOh.model.card.Monster;
import YuGiOh.model.card.action.ChangeMonsterPositionAction;
import YuGiOh.model.enums.MonsterState;

public class ChangeMonsterPositionEvent extends Event {
    private final Monster monster;
    private final MonsterState monsterState;

    public ChangeMonsterPositionEvent(Monster monster, MonsterState monsterState) {
        this.monster = monster;
        this.monsterState = monsterState;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return "changing position of " + monster.getName() + " to " + monsterState.toString() + "?";
    }

    @Override
    public String getDescription() {
        return "changing position of monster";
    }
}
