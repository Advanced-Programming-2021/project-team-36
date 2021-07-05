package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.Game;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.action.MagicActivation;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;

public class SupplySquad extends Spell {

    public SupplySquad(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    private boolean isActivated = false;

    @Override
    public void onDestroyMyMonster() {
        if (!isActivated) {
            isActivated = true;
            GameController gameController = GameController.getInstance();
            try {
                gameController.getPlayerControllerByPlayer(this.getOwner()).drawCard();
                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully. ", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this, Color.Gray);
            } catch (LogicException ignored) {
            }
        }
    }

    @Override
    public void startOfNewTurn() {
        isActivated = false;
    }

    @Override
    protected Effect getEffect() {
        return () -> {};
    }

    @Override
    public boolean canActivateEffect() {
        return false;
    }
}