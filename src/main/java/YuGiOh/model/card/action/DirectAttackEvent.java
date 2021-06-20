package YuGiOh.model.card.action;

import YuGiOh.model.Player.Player;
import lombok.Getter;
import YuGiOh.model.card.Monster;

public class DirectAttackEvent extends AttackEvent {
    @Getter
    private final Player player;

    public DirectAttackEvent(Monster attacker, Player player){
        super(attacker);
        this.player = player;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to direct attack %s with %s", player.getUser().getNickname(), getAttacker().getName());
    }

    @Override
    public String getDescription(){
        return "direct attack";
    }
}
