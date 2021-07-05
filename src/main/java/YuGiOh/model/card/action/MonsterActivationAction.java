package YuGiOh.model.card.action;

import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.MagicActivation;

public class MonsterActivationAction extends Action {
    public MonsterActivationAction(MagicActivation event, Effect effect) {
        super(event, effect);
    }

    public MonsterActivationAction(MagicActivation event) {
        super(event);
    }

    public void validateEffect() throws ValidateResult {
        MagicActivation event = (MagicActivation) getEvent();
        ValidateTree.checkActivateMonster((Monster) event.getCard());
        Spell spell = (Spell) event.getCard();
    //    spell.validateEffect();
    }

    protected void preprocess() {
        MagicActivation event = (MagicActivation) getEvent();
        Spell spell = (Spell) event.getCard();
    //    spell.preprocessForEffect();
    };
}
