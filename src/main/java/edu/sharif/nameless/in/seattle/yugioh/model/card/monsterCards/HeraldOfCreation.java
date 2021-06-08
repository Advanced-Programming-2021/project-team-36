package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.Conditions;
import edu.sharif.nameless.in.seattle.yugioh.view.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.controller.player.PlayerController;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Card;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

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
        };
    }

    @Override
    public Monster clone(){
        HeraldOfCreation cloned = (HeraldOfCreation) super.clone();
        cloned.lastTurnActivated = -1;
        return cloned;
    }
}
