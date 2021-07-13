package YuGiOh.model.card.action;

import YuGiOh.model.card.event.MonsterActivation;
import YuGiOh.model.enums.Color;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class MonsterActivationAction extends Action {
    public MonsterActivationAction(MonsterActivation event) {
        super(event, ()->{
            CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", event.getMonster().getOwner().getUser().getUsername(), event.getMonster().getName()), Color.Blue);
            return event.getMonster().activateEffect().run();
        });
    }

    public void validateEffect() throws ValidateResult {
        MonsterActivation event = (MonsterActivation) getEvent();
        ValidateTree.checkActivateMonster(event.getMonster());
    }
}
