package YuGiOh.controller.menu;

import YuGiOh.controller.*;
import YuGiOh.model.Player.Player;
import YuGiOh.model.User;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.event.NonGameEvent;
import YuGiOh.model.exception.DuelHasNotStarted;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.ModelException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.exception.LogicException;
import YuGiOh.network.packet.Request;
import YuGiOh.view.game.GuiReporter;
import YuGiOh.view.game.event.RoundOverEvent;
import YuGiOh.view.menu.DuelMenuView;
import YuGiOh.model.Duel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class DuelMenuController {
    @Getter
    private final Duel duel;
    private GameController gameController;

    // todo fix this view

    @Setter @Getter
    private DuelMenuView view;

    private static final Map<Duel, DuelMenuController> duelToController = new HashMap<>();

    public DuelMenuController(Duel duel){
        this.duel = duel;
        duelToController.put(duel, this);
    }
    public void finishGame(RoundOverExceptionEvent roundOverExceptionEvent) {
        try {
            duel.goNextRound(roundOverExceptionEvent);
            if(!duel.isFinished())
                newGameRequest();
        } catch (ModelException | LogicException exception){
            exception.printStackTrace();
        }
        GuiReporter.getInstance().report(new RoundOverEvent(roundOverExceptionEvent));
    }
    public void newGameRequest() throws LogicException {
        if(duel.isFinished())
            throw new LogicException("duel has ended!");
        if(this.gameController == null || !this.gameController.getGame().equals(duel.getCurrentGame()))
            this.gameController = new GameController(duel.getCurrentGame());
    }

    // todo when to start new game?
    public void runNewGame() {
        if(this.gameController == null || !this.gameController.getGame().equals(duel.getCurrentGame()))
            throw new Error("you haven't started the game");
        this.gameController.doGameStep();
    }
    public void requestToDoAction(Request request, Action action) throws GameException {
        if(getPlayer(request).equals(gameController.getGame().getCurrentPlayer())){
            if(action.getEvent() instanceof NonGameEvent)
                action.runEffect();
            else
                gameController.startChain(action);
            // todo handle end of game
        } else {
            throw new LogicException("you can't do stuff in opponent's turn");
        }
    }
    private boolean validateAction(Action action) {
        return action.isValid();
    }
    private static Player getPlayer(Request request) {
        return Player.getPlayerByUser(request.getUser());
    }
    private static DuelMenuController getInstance(User user) {
        Optional<Duel> opt = Duel.getUserLastDuel(user);
        if(opt.isEmpty() || duelToController.get(opt.get()) == null)
            throw new DuelHasNotStarted();
        return duelToController.get(opt.get());
    }
}
