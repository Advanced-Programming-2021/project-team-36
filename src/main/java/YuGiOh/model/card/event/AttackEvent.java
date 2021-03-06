package YuGiOh.model.card.event;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import lombok.Getter;

public abstract class AttackEvent extends Event {
    @Getter
    private final Monster attacker;

    public AttackEvent(Monster attacker){
        this.attacker = attacker;
    }

    @Override
    public int getSpeed() {
        return 1;
    }
}
