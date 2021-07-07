package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.card.event.Event;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.view.gui.GuiReporter;
import YuGiOh.view.gui.event.GameActionEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

public abstract class Action {
    @Getter
    protected final Event event;
    protected Effect effect;

    public Action(Event event) {
        this.event = event;
    }

    public Action(Event event, Effect effect){
        this.event = event;
        this.effect = effect;
    }

    public void runEffect() throws ResistToChooseCard {
        if (GuiReporter.getInstance() == null)
            System.out.println("shit");
        GuiReporter.getInstance().report(new GameActionEvent(this));
        try {
            validateEffect();
            effect.run();
            GameController.getInstance().checkBothLivesEndGame();
        } catch (LogicException | ValidateResult e){
            CustomPrinter.println(
                    "We cannot run effect of " + this.event.getDescription() + " because something had changed in chain!",
                    Color.Red
            );
        }
    }

    protected abstract void preprocess() throws ResistToChooseCard;

    public abstract void validateEffect() throws ValidateResult;
}
