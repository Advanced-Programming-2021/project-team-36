package YuGiOh.model.card.action;

import java.util.concurrent.CompletableFuture;

public interface Effect {
    CompletableFuture<Void> run();
}
