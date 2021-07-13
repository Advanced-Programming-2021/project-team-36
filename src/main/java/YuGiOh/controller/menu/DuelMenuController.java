package YuGiOh.controller.menu;

import YuGiOh.controller.*;
import YuGiOh.model.exception.GameException;
import YuGiOh.model.exception.ModelException;
import YuGiOh.model.exception.eventException.RoundOverExceptionEvent;
import YuGiOh.model.exception.LogicException;
import YuGiOh.view.game.GuiReporter;
import YuGiOh.view.game.event.RoundOverEvent;
import YuGiOh.view.menu.DuelMenuView;
import YuGiOh.model.Duel;
import YuGiOh.model.card.*;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.CardAddress;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;
import YuGiOh.model.enums.MonsterState;
import lombok.Setter;

public class DuelMenuController extends BaseMenuController {
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

    public void printCurrentPhase() {
        CustomPrinter.println("phase: " + duel.getCurrentGame().getPhase().getVerboseName(), Color.Blue);
    }

    public void showBoard() {
        CustomPrinter.println(duel.getCurrentGame().getOpponentPlayer().getUser().getNickname() + ":" + duel.getCurrentGame().getOpponentPlayer().getLifePoint(), Color.Purple);
        CustomPrinter.println(duel.getCurrentGame().getOpponentPlayer().getBoard().toRotatedString(), Color.Purple);
        CustomPrinter.println();
        CustomPrinter.println("--------------------------", Color.Purple);
        CustomPrinter.println();
        CustomPrinter.println(duel.getCurrentGame().getCurrentPlayer().getBoard().toString(), Color.Purple);
        CustomPrinter.println(duel.getCurrentGame().getCurrentPlayer().getUser().getNickname() + ":" + duel.getCurrentGame().getCurrentPlayer().getLifePoint(), Color.Purple);
    }

    public void finishGame(RoundOverExceptionEvent roundOverExceptionEvent) {
        try {
            duel.goNextRound(roundOverExceptionEvent);
            if(!duel.isFinished())
                getReadyForNewGame();
        } catch (ModelException | LogicException exception){
            exception.printStackTrace();
        }
        GuiReporter.getInstance().report(new RoundOverEvent(roundOverExceptionEvent));
    }
    public void getReadyForNewGame() throws LogicException {
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
