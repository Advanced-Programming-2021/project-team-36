package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MonsterReborn extends Spell {

    public MonsterReborn(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            assert canActivateEffect();
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Monster monster = (Monster) playerController.chooseKCards("Choose 1 monster in GraveYard to special summon it",
                    1,
                    Conditions.getMonsterFromGraveYard()
            )[0];
            playerController.summon(monster, 0, true);
            CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        Player current = this.owner;
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
        Player opponent = GameController.getInstance().getOtherPlayerController(playerController).getPlayer();
        List<Card> cards = new ArrayList<>();
        cards.addAll(current.getBoard().getGraveYard());
        cards.addAll(opponent.getBoard().getGraveYard());
        for (Card card : cards) {
            if (card instanceof Monster) {
                return true;
            }
        }
        return false;
    }
}