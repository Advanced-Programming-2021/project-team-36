package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Board;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.MonsterState;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

import java.util.ArrayList;
import java.util.List;

public class Terraforming extends Spell {

    public Terraforming(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            Player player = this.owner;
            List<Card> cards = player.getBoard().getMainDeck().getCards();
            for (Card card : cards)
                if (card instanceof Spell) {
                    Spell spell = (Spell) card;
                    if (spell.getIcon().equals(Icon.FIELD)) {
                        cards.remove(spell);
                        player.getBoard().getCardsOnHand().add(spell);
                        CustomPrinter.println(String.format("new card added to the hand : <%s>", spell.getName()), Color.Blue);
                        break;
                    }
                }
            CustomPrinter.println("Terraforming activated successfully", Color.Green);
        };
    }

    @Override
    public boolean canActivateEffect() {
        Player player = this.owner;
        List<Card> cards = player.getBoard().getMainDeck().getCards();
        for (Card card : cards)
            if (card instanceof Spell) {
                Spell spell = (Spell) card;
                if (spell.getIcon().equals(Icon.FIELD))
                    return true;
            }
        CustomPrinter.println("You can't activate Terraforming (you don't have any Field card in your deck)", Color.Red);
        return false;
    }
}