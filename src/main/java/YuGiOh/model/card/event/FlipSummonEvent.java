package YuGiOh.model.card.event;

import YuGiOh.model.Player.Player;
import YuGiOh.model.enums.SummonType;
import YuGiOh.model.card.Monster;

public class FlipSummonEvent extends SummonEvent {
    public FlipSummonEvent(Player player, Monster monster){
        super(player, monster, SummonType.FLIP);
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to flip summon %s?", getMonster().getName());
    }

    @Override
    public String getDescription(){
        return "flip summon monster";
    }
}
