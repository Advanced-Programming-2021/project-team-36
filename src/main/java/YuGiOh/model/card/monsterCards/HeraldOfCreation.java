package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;

public class HeraldOfCreation extends Monster {
    public HeraldOfCreation(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    private Card discardedCard;
    private Monster toSummonMonster;

    @Override
    public boolean canActivateEffect() {
        return lastTurnActivated != GameController.instance.getGame().getTurn() &&
                SelectConditions.countOfAllCardCanSelect(SelectConditions.getInPlayersHandCondition(this.getOwner())) >= 1 &&
                SelectConditions.countOfAllCardCanSelect(SelectConditions.getInPlayerGraveYardMonster(this.getOwner(), 7)) >= 1;
    }

    @Override
    public Effect activateEffect() {
        return () -> {
            PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
            return controller.chooseKCards(
                        "choose 1 card to discard from your hand",
                        1,
                        SelectConditions.getInPlayersHandCondition(this.getOwner())
            ).thenAccept(cards-> {
                discardedCard = cards.get(0);
            }).thenCompose(dum-> {
                return controller.chooseKCards(
                        "choose 1 level 7 or higher monster from your graveyard",
                        1,
                        SelectConditions.getInPlayerGraveYardMonster(this.getOwner(), 7)
                );
            }).thenAccept(cards->{
                toSummonMonster = (Monster) cards.get(0);
            }).thenRun(()-> {
                this.getOwner().getBoard().moveCardNoError(discardedCard, ZoneType.GRAVEYARD);
                this.getOwner().getBoard().moveCardNoError(toSummonMonster, ZoneType.HAND);
                lastTurnActivated = GameController.instance.getGame().getTurn();
                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this.asEffect(), Color.Gray);
            });
        };
    }

    @Override
    public Monster clone() {
        HeraldOfCreation cloned = (HeraldOfCreation) super.clone();
        cloned.lastTurnActivated = -1;
        return cloned;
    }
}
