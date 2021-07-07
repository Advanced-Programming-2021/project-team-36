package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.action.SummonAction;
import YuGiOh.model.card.action.ValidateResult;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.archive.view.cardSelector.SelectConditions;
import YuGiOh.archive.view.cardSelector.ResistToChooseCard;

public class TexChanger extends Monster {
    public TexChanger(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    @Override
    protected void specialEffectWhenBeingAttacked(Monster attacker) throws ResistToChooseCard, LogicException {
        PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(getOwner());

        if (lastTurnActivated != GameController.instance.getGame().getTurn() &&
                controller.askRespondToQuestion("Do you want to activate the effect of tex changer?", "yes", "no")) {

            lastTurnActivated = GameController.instance.getGame().getTurn();
            try {
                Monster chosen = (Monster) controller.chooseKCards(
                        "choose 1 cyberse monster from your hand or graveyard or deck",
                        1,
                        SelectConditions.and(
                                SelectConditions.getMonsterCardTypeCondition(getOwner(), MonsterCardType.NORMAL),
                                SelectConditions.getMonsterTypeCondition(getOwner(), MonsterType.CYBERSE),
                                SelectConditions.getIsPlayersCard(getOwner()),
                                SelectConditions.or(
                                        SelectConditions.getInZoneCondition(ZoneType.HAND),
                                        SelectConditions.getInZoneCondition(ZoneType.GRAVEYARD),
                                        SelectConditions.getInZoneCondition(ZoneType.DECK)
                                )
                        )
                )[0];
                SummonAction action = new SummonAction(
                        new SummonEvent(this.getOwner(), chosen, SummonType.SPECIAL, 0, SelectConditions.noCondition)
                );
                action.validateEffect();
                action.runEffect();
                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this.asEffect(), Color.Gray);
            } catch (ResistToChooseCard e) {
                CustomPrinter.println("We didn't summoned monster", Color.Red);
            } catch (ValidateResult e) {
                CustomPrinter.println(e.getMessage(), Color.Red);
            }
        } else {
            damageStep(attacker);
        }
    }
}
