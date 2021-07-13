package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.controller.menu.DuelMenuController;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.card.event.Event;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.view.game.GameCardFrameManager;
import YuGiOh.view.game.GuiReporter;
import YuGiOh.view.game.component.GameField;
import YuGiOh.view.game.event.GameActionEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.game.event.RoundOverEvent;
import YuGiOh.view.menu.DuelMenuView;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

public abstract class Action {
    @Getter
    private final Event event;
    private final Effect effect;
    private boolean completedAction = false;

    public Action(Event event, Effect effect){
        this.event = event;
        this.effect = effect;
    }

    public CompletableFuture<Void> runEffectThrows(Runnable onSuccess) throws ValidateResult, GameException {
        System.out.println("BEFORE RUNNING EFFECT OF " + getClass().getName() + "  ");
        if(completedAction)
            throw new ValidateResult("effect was completed before");
        validateEffect();
        if (GuiReporter.getInstance() == null)
            System.out.println("shit");

        CompletableFuture<Void> ret = preprocess()
                .thenCompose(res -> {
                    System.out.println("RUNNING GUI");
                    return gui();
                })
                .thenRun(() -> {
                    System.out.println("RUN EFFECT!");
                    GuiReporter.getInstance().report(new GameActionEvent(this));
                }).thenCompose(res -> effect.run())
                .thenCompose(res-> GameField.getInstance().getCardFrameManager().refresh())
                .thenRun(()->{
                    onSuccess.run();
                    GameController.getInstance().checkBothLivesEndGame();
                    DuelMenuView.getInstance().resetSelector();
                }).thenRun(()->{
                    System.out.println("completed action " + getClass().getName());
                }).exceptionally(ex -> {
                    if(ex.getCause() instanceof RoundOverExceptionEvent) {
                        DuelMenuController.getInstance().finishGame((RoundOverExceptionEvent) ex.getCause());
                        throw (RoundOverExceptionEvent) ex.getCause();
                    } else {
                        CustomPrinter.println("process of running effect of " + getClass().getName() + " failed. and we catched it. " + "exception thrown was ", Color.Red);
                        ex.printStackTrace();
                        return null;
                    }
                });
        completedAction = true;
        return ret;
    }

    // todo what happens to guys who use this and are not async?
    public CompletableFuture<Void> runEffectThrows() throws GameException, ValidateResult {
        return runEffectThrows(()->{});
    }

    public CompletableFuture<Void> runEffectThrowsValidateResult() throws ValidateResult {
        try {
            return runEffectThrows();
        } catch (GameException ignored) {
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<Void> runEffect(Runnable onSuccess, Runnable onFail) {
        try {
            return runEffectThrows(onSuccess);
        } catch (LogicException e){
            if(this.event != null) {
                CustomPrinter.println(
                        "We cannot run effect of " + this.event.getDescription() + " because something had changed in chain!",
                        Color.Red
                );
            }
            onFail.run();
        } catch (ValidateResult v) {
            // this should be reason of failure
            onFail.run();
        } catch (ResistToChooseCard r) {
            onFail.run();
        } catch (GameException ignored) {
            onFail.run();
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<Void> runEffect() {
        return runEffect(()->{}, ()->{});
    }

    public boolean isValid() {
        try {
             validateEffect();
            return true;
        } catch (ValidateResult e) {
            return false;
        }
    }

    protected GameField getGameField() {
        return GameField.getInstance();
    }

    protected CompletableFuture<Void> gui() {
        return CompletableFuture.completedFuture(null);
    }

    protected CompletableFuture<Void> preprocess() {
        return CompletableFuture.completedFuture(null);
    }

    public abstract void validateEffect() throws ValidateResult, ValidateResult;
}
