package YuGiOh.model.card.action;

import YuGiOh.model.card.event.Event;
import YuGiOh.model.card.event.SetMagic;
import YuGiOh.model.card.event.SetMonster;

public class SetMonsterAction extends Action {
    public SetMonsterAction(SetMonster event, Effect effect) {
        super(event, effect);
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SetMonster event = (SetMonster) getEvent();
    }
}
