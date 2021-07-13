package YuGiOh.model.card.action;

import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.SummonType;

public class NormalSummonAction extends SummonAction {
    public NormalSummonAction(Player player, Monster monster) {
        super(new SummonEvent(player, monster, SummonType.NORMAL));
    }
}
