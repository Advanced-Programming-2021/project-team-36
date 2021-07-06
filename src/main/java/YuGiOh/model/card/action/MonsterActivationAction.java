package YuGiOh.model.card.action;

import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.card.event.MonsterActivation;

public class MonsterActivationAction extends Action {
    public MonsterActivationAction(MonsterActivation event, Effect effect) {
        super(event, effect);
    }

    public void validateEffect() throws ValidateResult {
        MonsterActivation event = (MonsterActivation) getEvent();
        ValidateTree.checkActivateMonster((Monster) event.getCard());
        // todo activation check
    }

    protected void preprocess() {
//        MagicActivation event = (MagicActivation) getEvent();
//        Spell spell = (Spell) event.getCard();
    //    spell.preprocessForEffect();
    };
}
