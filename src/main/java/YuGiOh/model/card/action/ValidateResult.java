package YuGiOh.model.card.action;

import lombok.Getter;

public class ValidateResult extends Exception {
    public ValidateResult(String message) {
        super(message);
    }
}
