package YuGiOh.model.card.action;

import YuGiOh.model.exception.ValidateResult;

public interface Validation {
    void run() throws ValidateResult;
}
