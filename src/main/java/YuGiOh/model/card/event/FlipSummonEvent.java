package YuGiOh.model.card.event;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.SummonType;
import YuGiOh.view.cardSelector.SelectCondition;
import YuGiOh.view.cardSelector.SelectConditions;
import lombok.Getter;
import YuGiOh.model.card.Monster;
import lombok.Setter;

import java.util.List;

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
