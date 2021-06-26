package YuGiOh.model.card.action;

import YuGiOh.controller.LogicException;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import lombok.Getter;

public class Action {
    @Getter
    private final Event event;
    private final Effect effect;

    public Action(Event event, Effect effect){
        this.event = event;
        this.effect = effect;
    }

    public void runEffect() throws ResistToChooseCard {
        try {
            effect.run();
        } catch (LogicException e){
            CustomPrinter.println(
                    "We cannot run effect of " + this.event.getDescription() + " because something had changed in chain!",
                    Color.Red
            );
        }
    }
}
