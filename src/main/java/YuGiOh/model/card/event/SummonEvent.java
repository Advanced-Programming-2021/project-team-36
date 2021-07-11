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

public class SummonEvent extends Event {
    @Getter
    private final Player player;
    @Getter
    private final Monster monster;
    @Getter
    private final SummonType summonType;
    @Getter
    @Setter
    private MonsterState monsterState;
    @Getter
    private final int requiredTributes;
    @Getter
    private final SelectCondition tributeCondition;
    @Getter
    @Setter
    private List<Card> chosenCardsToTribute;

    public SummonEvent(Player player, Monster monster, SummonType summonType){
        this.player = player;
        this.monster = monster;
        this.summonType = summonType;
        requiredTributes = monster.getNumberOfRequiredTribute();
        tributeCondition = SelectConditions.myMonsterFromMyMonsterZone(player);
    }

    public SummonEvent(Player player, Monster monster, SummonType summonType, MonsterState monsterState) {
        this.player = player;
        this.monster = monster;
        this.summonType = summonType;
        this.monsterState = monsterState;
        requiredTributes = monster.getNumberOfRequiredTribute();
        tributeCondition = SelectConditions.myMonsterFromMyMonsterZone(player);
    }

    public SummonEvent(Player player, Monster monster, SummonType summonType, int requiredTributes, SelectCondition tributeCondition) {
        this.player = player;
        this.monster = monster;
        this.summonType = summonType;
        this.requiredTributes = requiredTributes;
        this.tributeCondition = tributeCondition;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to summon %s?", monster.getName());
    }

    @Override
    public String getDescription(){
        return "summon monster";
    }
}
