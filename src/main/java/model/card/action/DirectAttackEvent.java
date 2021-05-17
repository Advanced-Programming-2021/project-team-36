package model.card.action;

import lombok.Getter;
import model.Player.Player;
import model.card.Monster;

public class DirectAttackEvent extends AttackEvent {
    @Getter
    private final Player player;

    public DirectAttackEvent(Monster attacker, Player player){
        super(attacker);
        this.player = player;
    }
}
