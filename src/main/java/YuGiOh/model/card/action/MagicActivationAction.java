package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.enums.Color;
import YuGiOh.model.exception.LogicException;
import YuGiOh.model.exception.ValidateResult;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class MagicActivationAction extends Action {
    public MagicActivationAction(Magic magic) {
        this(magic, ()->{
            CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", magic.getOwner().getUser().getUsername(), magic.getName()), Color.Blue);
            magic.activateEffect().run();
            return CompletableFuture.completedFuture(null);
        });
    }
    protected MagicActivationAction(Magic magic, Effect effect) {
        super(new MagicActivation(magic), effect);
    }


    public void validateEffect() throws ValidateResult {
        MagicActivation event = (MagicActivation) getEvent();
        if(event.getMagic() instanceof Spell)
            ValidateTree.checkActivateMagic((Spell) event.getMagic());
        else if(event.getMagic() instanceof Trap && GameController.getInstance().getGame().getChain().isEmpty())
            throw new ValidateResult("trap's can't be activated");
    }
}
