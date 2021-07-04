package YuGiOh.model.card.action;

import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.MagicActivation;

public class MagicActivationAction extends Action {
    public MagicActivationAction(MagicActivation event, Effect effect) {
        super(event, effect);
    }

    @Override
    public void validateEffect() throws ValidateResult {
        MagicActivation event = (MagicActivation) getEvent();
        ValidateTree.checkActivateMagic(event.getCard());
        Spell spell = (Spell) event.getCard();
    //    spell.validateEffect();
    }
}
