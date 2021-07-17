package YuGiOh.network;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException() {
        super("not authenticated!");
    }
}
