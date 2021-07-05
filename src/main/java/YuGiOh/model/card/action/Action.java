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

public class Action {
    @Getter
    protected Event event;
    protected Effect effect;

    public Action(Event event) {
        this.event = event;
        this.effect = () -> {};
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
            effect.run();
            GameController.getInstance().checkBothLivesEndGame();
        } catch (LogicException e){
            CustomPrinter.println(
                    "We cannot run effect of " + this.event.getDescription() + " because something had changed in chain!",
                    Color.Red
            );
        }
    }

    public void validateEffect() throws ValidateResult {
    }
}
