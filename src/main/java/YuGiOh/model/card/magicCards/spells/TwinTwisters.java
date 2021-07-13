package YuGiOh.model.card.magicCards.spells;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Magic;
import YuGiOh.model.card.Spell;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.Icon;
import YuGiOh.model.enums.Status;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.view.cardSelector.SelectConditions;

import java.util.concurrent.CompletableFuture;

public class TwinTwisters extends Spell {

    public TwinTwisters(String name, String description, int price, Icon icon, Status status) {
        super(name, description, price, icon, status);
    }

    Card discard;
    Card[] kill;

    private CompletableFuture<Void> preprocess() {
        GameController gameController = GameController.getInstance();
        PlayerController playerController = gameController.getPlayerControllerByPlayer(this.getOwner());
        int _upto = 0;
        for (Card card : GameController.getInstance().getGame().getAllCardsOnBoard()) {
            if (card instanceof Magic && card != this)
                _upto = Math.min(_upto + 1, 2);
        }
        if(_upto == 0)
            throw new Error("this must never happen if we validate before run");

        final int upto = _upto;

        return playerController.chooseKCards("Discard one card from your hand",
                1,
                SelectConditions.getCardFromPlayerHand(this.getOwner(), this)
        ).thenAccept(cards ->
                discard = cards.get(0)
        ).thenCompose(dum ->
                playerController.askRespondToQuestion("How many spell and trap card you want to destroy?", "1", Integer.toString(upto))
        ).thenApply(res -> {
            if (res)
                return  1;
            else
                return upto;
        }).thenCompose(number ->
            playerController.chooseKCards(String.format("Destroy %s spell and magic on field", number),
                    number,
                    SelectConditions.getMagicFromField(this))
        ).thenAccept(cards ->
            kill = cards.toArray(Card[]::new)
        );
    }

    @Override
    protected Effect getEffect() {
        return () ->
            preprocess().thenRun(()->{
                GameController gameController = GameController.getInstance();
                gameController.moveCardToGraveYard(discard);
                for (Card card : kill)
                    gameController.moveCardToGraveYard(card);
                CustomPrinter.println(String.format("<%s> activated <%s> successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this, Color.Gray);
            });
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