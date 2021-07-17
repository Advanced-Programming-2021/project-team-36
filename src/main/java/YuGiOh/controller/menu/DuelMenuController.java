package YuGiOh.controller.menu;

import YuGiOh.controller.*;
import YuGiOh.model.exception.ModelException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.exception.LogicException;
import YuGiOh.view.game.GuiReporter;
import YuGiOh.view.game.event.RoundOverEvent;
import YuGiOh.view.menu.DuelMenuView;
import YuGiOh.model.Duel;
import lombok.Getter;
import lombok.Setter;

public class DuelMenuController {
    @Getter
    public static DuelMenuController instance;
    @Getter
    private final Duel duel;
    private GameController gameController;

    @Setter @Getter
    private DuelMenuView view;

    public DuelMenuController(Duel duel){
        this.duel = duel;
        instance = this;
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
    public void runNewGame() {
        if(this.gameController == null || !this.gameController.getGame().equals(duel.getCurrentGame()))
            throw new Error("you haven't started the game");
        this.gameController.doGameStep();
    }
}
