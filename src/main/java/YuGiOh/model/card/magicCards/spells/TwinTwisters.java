package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;
import YuGiOh.archive.view.cardSelector.SelectConditions;

public class TwinTwisters extends Spell {

    public TwinTwisters(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    Card discard;
    Card[] kill;

    private void preprocess() throws ResistToChooseCard, LogicException {
        GameController gameController = GameController.getInstance();
        PlayerController playerController = gameController.getPlayerControllerByPlayer(this.getOwner());
        int upto = 0;
        for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard())
            if (card instanceof Magic && card != this)
                upto = Math.min(upto + 1, 2);
        if (upto == 0)
            throw new LogicException("there is no spell card in field");

        discard = playerController.chooseKCards("Discard one card from your hand",
                1,
                SelectConditions.getCardFromPlayerHand(this.getOwner(), this))[0];

        boolean askUser = playerController.askRespondToQuestion("How many spell and trap card you want to destroy?", "1", Integer.toString(upto));
        int number;
        if (askUser)
            number = 1;
        else
            number = upto;

        kill = playerController.chooseKCards(String.format("Destroy %s spell and magic on field", number),
                number,
                SelectConditions.getMagicFromField(this));
    }

    @Override
    protected Effect getEffect() {
        return () -> {
            GameController gameController = GameController.getInstance();
            preprocess();
            gameController.moveCardToGraveYard(discard);
            for (Card card : kill)
                gameController.moveCardToGraveYard(card);
            CustomPrinter.println(String.format("<%s> activated <%s> successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
            CustomPrinter.println(this, Color.Gray);
        };
    }

    @Override
    public boolean canActivateEffect() {
        boolean canDiscard = false;
        for (Card card : this.getOwner().getBoard().getCardsOnHand())
            if (card != this) {
                canDiscard = true;
                break;
            }
        if (!canDiscard)
            return false;
        for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard())
            if (card instanceof Magic && card != this)
                return true;
        return false;
    }
}