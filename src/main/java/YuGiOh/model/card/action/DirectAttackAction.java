package YuGiOh.model.card.action;

import YuGiOh.model.card.event.DirectAttackEvent;

public class DirectAttackAction extends Action {
    public DirectAttackAction(DirectAttackEvent event, Effect effect) {
        super(event, effect);
    }

    public void validateEffect() throws ValidateResult {
        DirectAttackEvent event = (DirectAttackEvent) getEvent();
        ValidateTree.checkDirectAttack(event.getAttacker(), event.getPlayer());
    }

    public void preprocess() {
    }
}
