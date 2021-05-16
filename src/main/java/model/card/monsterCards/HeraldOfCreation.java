package model.card.monsterCards;

import utils.CustomPrinter;
import controller.GameController;
import controller.LogicException;
import controller.cardSelector.Conditions;
import controller.cardSelector.ResistToChooseCard;
import controller.player.PlayerController;
import model.card.Card;
import model.card.Effect;
import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;

public class HeraldOfCreation extends Monster {
    public HeraldOfCreation(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    @Override
    public Effect activateEffect() throws LogicException {
        if(lastTurnActivated == GameController.instance.getGame().getTurn())
            throw new LogicException("you can only activate this once in a turn");
        lastTurnActivated = GameController.instance.getGame().getTurn();

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
                CustomPrinter.println("canceled");
                return;
            }
            try {
                monster = (Monster) controller.chooseKCards(
                        "choose 1 level 7 or higher monster from your graveyard",
                        1,
                        Conditions.getInPlayerGraveYardMonster(this.owner, 7)
                )[0];
            } catch (ResistToChooseCard e){
                CustomPrinter.println("canceled");
                return;
            }
            this.owner.getBoard().removeFromHand(discarded);
            this.owner.getBoard().addCardToHand(monster);
        };
    }

    @Override
    public Monster clone(){
        HeraldOfCreation cloned = (HeraldOfCreation) super.clone();
        cloned.lastTurnActivated = -1;
        return cloned;
    }
}
