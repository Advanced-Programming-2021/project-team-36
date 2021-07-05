package YuGiOh.model.card.action;

import YuGiOh.model.card.event.Event;
import YuGiOh.model.card.event.SetMagic;

public class SetMagicAction extends Action {
    public SetMagicAction(Event event, Effect effect) {
        super(event, effect);
    }

    @Override
    public void validateEffect() throws ValidateResult {
        SetMagic event = (SetMagic) getEvent();
        ValidateTree.checkSetMagic(event.getMagic());
    }
}
