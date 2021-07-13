package YuGiOh.model.card.action;

import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.exception.ValidateResult;

abstract public class SpecialSummonAction extends SummonAction {

    public SpecialSummonAction(SummonEvent event) {
        super(event);
    }
    abstract public void specialValidate() throws ValidateResult;

    @Override
    public void validateEffect() throws ValidateResult {
        super.validateEffect();
        specialValidate();
    }
}
