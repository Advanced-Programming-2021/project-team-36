package YuGiOh.model.exception;

import YuGiOh.model.exception.GameException;

public class ValidateResult extends GameException {
    public ValidateResult(String message) {
        super(message);
    }
}
