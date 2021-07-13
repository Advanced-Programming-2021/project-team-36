package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;

import java.util.concurrent.CompletableFuture;

public class SpellAbsorption extends Spell {

    public SpellAbsorption(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    public void onSpellResolve() {
        GameController.getInstance().increaseLifePoint(this.getOwner(), 500);
        CustomPrinter.println(String.format("<%s>'s <%s> activated successfully.", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
        CustomPrinter.println(this, Color.Gray);
    }

    // todo what is this shit

    @Override
    protected Effect getEffect() {
        return ()->
                CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean canActivateEffect() {
        return false;
    }
}