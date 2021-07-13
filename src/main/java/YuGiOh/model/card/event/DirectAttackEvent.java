package YuGiOh.model.card.event;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import lombok.Getter;

public class DirectAttackEvent extends AttackEvent {
    @Getter
    private final Player defender;

    public DirectAttackEvent(Monster attacker, Player defender){
        super(attacker);
        this.defender = defender;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to direct attack %s with %s", defender.getUser().getNickname(), getAttacker().getName());
    }

    @Override
    public String getDescription(){
        return "direct attack";
    }
}
