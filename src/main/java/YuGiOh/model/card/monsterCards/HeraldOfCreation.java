package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.Monster;

public class HeraldOfCreation extends Monster {
    public HeraldOfCreation(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    @Override
    public boolean canActivateEffect(){
        return lastTurnActivated != GameController.instance.getGame().getTurn();
    }

    @Override
    public Effect activateEffect() throws LogicException {
        if(lastTurnActivated == GameController.instance.getGame().getTurn())
            throw new LogicException("you can only activate this once in a turn");

        return ()->{
            PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(this.owner);
            Card discarded;
            Monster monster;
            try {
                discarded = controller.chooseKCards(
                        "choose 1 card to discard from your hand",
                        1,
                        Conditions.getInPlayersHandCondition(this.owner)
                )[0];
            } catch (ResistToChooseCard e){
                CustomPrinter.println("canceled", Color.Default);
                return;
            }
            try {
                monster = (Monster) controller.chooseKCards(
                        "choose 1 level 7 or higher monster from your graveyard",
                        1,
                        Conditions.getInPlayerGraveYardMonster(this.owner, 7)
                )[0];
            } catch (ResistToChooseCard e){
                CustomPrinter.println("canceled", Color.Default);
                return;
            }
            this.owner.getBoard().removeFromHand(discarded);
            this.owner.getBoard().addCardToHand(monster);
            lastTurnActivated = GameController.instance.getGame().getTurn();
        };
    }

    @Override
    public Monster clone(){
        HeraldOfCreation cloned = (HeraldOfCreation) super.clone();
        cloned.lastTurnActivated = -1;
        return cloned;
    }
}
