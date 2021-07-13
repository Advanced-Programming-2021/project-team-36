package YuGiOh.model.card.action;

import YuGiOh.model.card.event.Event;
import YuGiOh.model.card.event.SetMagic;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MagicState;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class SetMagicAction extends Action {
    public SetMagicAction(SetMagic event) {
        super(event, ()->{
            event.getPlayer().getBoard().moveCardNoError(event.getMagic(), ZoneType.MAGIC);
            event.getMagic().setMagicState(MagicState.HIDDEN);
            CustomPrinter.println(String.format("<%s> set magic <%s> successfully", event.getPlayer().getUser().getUsername(), event.getMagic().getName()), Color.Green);
            return CompletableFuture.completedFuture(null);
        });
    }

    @Override
    public SetMagic getEvent() {
        return (SetMagic) super.getEvent();
    }

    public void validateEffect() throws ValidateResult {
        SetMagic event = (SetMagic) getEvent();
        ValidateTree.checkSetMagic(event.getMagic());
    }
}
