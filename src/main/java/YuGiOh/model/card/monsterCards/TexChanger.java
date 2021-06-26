package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.Card;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.Conditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;

public class TexChanger extends Monster {
    public TexChanger(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    @Override
    protected void specialEffectWhenBeingAttacked(Monster attacker) throws ResistToChooseCard, LogicException {
        PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(owner);

        if(lastTurnActivated != GameController.instance.getGame().getTurn() &&
                controller.askRespondToQuestion("Do you want to activate the effect of tex changer?", "yes", "no")) {

            lastTurnActivated = GameController.instance.getGame().getTurn();
            try {
                Monster chosen = (Monster) controller.chooseKCards(
                        "choose 1 cyberse monster from your hand or graveyard or deck",
                        1,
                        Conditions.and(
                                Conditions.getMonsterCardTypeCondition(owner, MonsterCardType.NORMAL),
                                Conditions.getMonsterTypeCondition(owner, MonsterType.CYBERSE),
                                Conditions.getIsPlayersCard(owner),
                                Conditions.or(
                                        Conditions.getInZoneCondition(ZoneType.HAND),
                                        Conditions.getInZoneCondition(ZoneType.GRAVEYARD),
                                        Conditions.getInZoneCondition(ZoneType.DECK)
                                )
                        )
                )[0];
                controller.summon(chosen);
            } catch (LogicException | ResistToChooseCard e){
                CustomPrinter.println("We didn't summoned monster", Color.Red);
            }
        } else {
            damageStep(attacker);
        }
    }
}
