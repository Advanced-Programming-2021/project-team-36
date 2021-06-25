package YuGiOh.model.card.magicCards.traps;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.Game;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;
import YuGiOh.model.card.Trap;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.card.action.SummonEvent;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;

public class SolemnWarning extends Trap {
    public SolemnWarning(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    @Override
    protected Effect getEffect() {
        assert canActivateEffect();
        return () -> {
            PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            playerController.moveCardToGraveYard(this);
            Action action = getChain().pop();
            playerController.getPlayer().decreaseLifePoint(2000);
            GameController.getInstance().checkBothLivesEndGame();
            Monster monster = ((SummonEvent) action.getEvent()).getMonster();
            GameController.getInstance().getOtherPlayerController(playerController).moveCardToGraveYard(monster);
            CustomPrinter.println("Solemn Warning activated and negated summon", Color.Green);
        };
    }

    @Override
    public boolean canActivateEffect() {
        if (getChain().isEmpty())
            return false;
        Action action = getChain().peek();
        return action.getEvent() instanceof SummonEvent;
    }
}