package YuGiOh.model.card.event;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.archive.view.cardSelector.SelectCondition;
import YuGiOh.archive.view.cardSelector.SelectConditions;
import lombok.Getter;
import YuGiOh.model.card.Monster;
import lombok.Setter;

import java.util.List;

public class SetMonster extends Event {
    @Getter
    private final Player player;
    @Getter
    private final Monster monster;
    @Getter
    private final int requiredTributes;
    @Getter
    private final SelectCondition tributeCondition;
    @Getter
    @Setter
    private List<Card> chosenCardsToTribute;

    public SetMonster(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        requiredTributes = monster.getNumberOfRequiredTribute();
        tributeCondition = SelectConditions.myMonsterFromMyMonsterZone(player);
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public String getActivationQuestion() {
        return String.format("Do you want to set %s?", monster.getName());
    }

    @Override
    public String getDescription(){
        return "set monster";
    }
}
