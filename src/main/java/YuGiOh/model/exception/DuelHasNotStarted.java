package YuGiOh.model.exception;

public class DuelHasNotStarted extends RuntimeException {
    public DuelHasNotStarted() {
        super("Duel has not started!");
    }
}
