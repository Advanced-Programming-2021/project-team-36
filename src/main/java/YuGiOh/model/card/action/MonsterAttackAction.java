package YuGiOh.model.card.action;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.event.MonsterAttackEvent;

public class MonsterAttackAction extends Action {
    public MonsterAttackAction(MonsterAttackEvent event, Effect effect) {
        super(event, effect);
    }

    @Override
    public void validateEffect() throws ValidateResult {
        MonsterAttackEvent event = (MonsterAttackEvent) getEvent();
        ValidateTree.checkMonsterAttack(event.getAttacker(), event.getDefender());
        Card attacker = event.getAttacker();
    }
}
