package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;
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
                        SelectConditions.and(
                                SelectConditions.getMonsterCardTypeCondition(owner, MonsterCardType.NORMAL),
                                SelectConditions.getMonsterTypeCondition(owner, MonsterType.CYBERSE),
                                SelectConditions.getIsPlayersCard(owner),
                                SelectConditions.or(
                                        SelectConditions.getInZoneCondition(ZoneType.HAND),
                                        SelectConditions.getInZoneCondition(ZoneType.GRAVEYARD),
                                        SelectConditions.getInZoneCondition(ZoneType.DECK)
                                )
                        )
                )[0];
                controller.summon(chosen, true);
                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.owner.getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this.asEffect(), Color.Gray);
            } catch (LogicException | ResistToChooseCard e){
                CustomPrinter.println("We didn't summoned monster", Color.Red);
            }
        } else {
            damageStep(attacker);
        }
    }
}
