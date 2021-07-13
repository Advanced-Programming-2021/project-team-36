package YuGiOh.model.card.action;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.event.MagicActivation;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MagicState;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class SpellActivationAction extends MagicActivationAction {
    public SpellActivationAction(Spell spell) {
        super(spell, ()->{
            CustomPrinter.println(String.format("<%s> wants to activate the effect of <%s>", spell.getOwner().getUser().getUsername(), spell.getName()), Color.Blue);
            if (spell.getOwner().hasInHand(spell))
                GameController.getInstance().addCardToBoard(spell);
            spell.setMagicState(MagicState.OCCUPIED);
            return spell.activateEffect().run();
        });
    }
}
