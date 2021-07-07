package YuGiOh.model.card.action;

import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.MagicActivation;

import java.net.SocketException;

public class MagicActivationAction extends Action {
    public MagicActivationAction(MagicActivation event, Effect effect) {
        super(event, effect);
    }

    public void validateEffect() throws ValidateResult {
        MagicActivation event = (MagicActivation) getEvent();
        if(event.getMagic() instanceof Spell)
            ValidateTree.checkActivateMagic((Spell) event.getMagic());
    }

    protected void preprocess() {
    }
}
