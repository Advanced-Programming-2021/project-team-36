package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.card.action.SummonAction;
import YuGiOh.model.card.event.SummonEvent;
import YuGiOh.model.enums.*;
import YuGiOh.model.card.Monster;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.model.exception.ResistToChooseCard;

import java.util.concurrent.CompletableFuture;

public class TexChanger extends Monster {
    public TexChanger(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;

    @Override
    protected CompletableFuture<Void> specialEffectWhenBeingAttacked(Monster attacker) {
        PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(getOwner());
        if (lastTurnActivated != GameController.instance.getGame().getTurn()) {
            return controller.askRespondToQuestion("Do you want to activate the effect of tex changer?", "yes", "no").thenCompose(res->{
                if(res) {
                    lastTurnActivated = GameController.instance.getGame().getTurn();
                    return controller.chooseKCards("choose 1 cyberse monster from your hand or graveyard or deck",
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
                    ).thenCompose(cards->{
                            Monster chosen = (Monster) cards.get(0);
                            SummonAction action = new SummonAction(
                                    new SummonEvent(this.getOwner(), chosen, SummonType.SPECIAL, 0, SelectConditions.noCondition)
                            );
                            return action.runEffect(() -> {
                                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                                CustomPrinter.println(this.asEffect(), Color.Gray);
                            }, () -> {
                                CustomPrinter.println("We didn't summoned monster", Color.Red);
                            });
                        }
                    );
                } else {
                    damageStep(attacker);
                    return CompletableFuture.completedFuture(null);
                }
            });
        } else {
            damageStep(attacker);
            return CompletableFuture.completedFuture(null);
        }
    }
}
