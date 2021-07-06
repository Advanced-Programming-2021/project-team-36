package YuGiOh.model.card.action;

import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.MagicActivation;

public class MagicActivationAction extends Action {
    public MagicActivationAction(MagicActivation event, Effect effect) {
        super(event, effect);
    }

    public MagicActivationAction(MagicActivation event) {
        super(event);
    }

    public void validateEffect() throws ValidateResult {
        MagicActivation event = (MagicActivation) getEvent();
        ValidateTree.checkActivateMagic((Spell) event.getCard());
        Spell spell = (Spell) event.getCard();
    //    spell.validateEffect();
    }

    protected void preprocess() {
        MagicActivation event = (MagicActivation) getEvent();
        Spell spell = (Spell) event.getCard();
    //    spell.preprocessForEffect();
    };
}
