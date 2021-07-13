package YuGiOh.model.card.magicCards.spells;

import YuGiOh.model.card.Card;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.model.enums.ZoneType;
import YuGiOh.utils.CustomPrinter;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Terraforming extends Spell {

    public Terraforming(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            Optional<Card> opt = getOwner().getBoard().getMainDeck().getCards().stream()
                    .filter(card-> card instanceof Spell && ((Spell) card).getIcon().equals(Icon.FIELD))
                    .findFirst();
            if(opt.isPresent()) {
                CustomPrinter.println(String.format("new card added to <%s>'s hand : <%s>", getOwner().getUser().getUsername(), opt.get()), Color.Blue);
                CustomPrinter.println(String.format("<%s> activated <%s> successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this, Color.Gray);
                getOwner().getBoard().moveCardNoError(opt.get(), ZoneType.HAND);
            }
            return CompletableFuture.completedFuture(null);
        };
    }

    @Override
    public boolean canActivateEffect() {
        return getOwner().getBoard().getMainDeck().getCards().stream()
                .anyMatch(card-> card instanceof Spell && ((Spell) card).getIcon().equals(Icon.FIELD));
    }
}