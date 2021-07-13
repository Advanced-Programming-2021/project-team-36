package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.card.action.Effect;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.model.card.Monster;

import java.util.concurrent.CompletableFuture;

public class ManEaterBug extends Monster {
    public ManEaterBug(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public Effect changeFromHiddenToOccupiedIfCanEffect(){
        return ()->{
            if(MonsterState.DEFENSIVE_HIDDEN.equals(getMonsterState())) {
                setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
                GameController gameController = GameController.getInstance();
                PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(getOwner());
                return controller.askRespondToQuestion("Do you want to activate effect of man easter bug?", "yes", "no")
                        .thenCompose(res -> {
                            if(res) {
                                return controller.chooseKCards("choose a monster card to kill", 1, SelectConditions.getInZoneCondition(ZoneType.MONSTER))
                                    .thenAccept(cards-> {
                                        Monster monster = (Monster) cards.get(0);
                                        gameController.moveCardToGraveYard(monster);
                                        CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                                        CustomPrinter.println(this.asEffect(), Color.Gray);
                                    });
                            }
                            return CompletableFuture.completedFuture(null);
                        });
            }
            return CompletableFuture.completedFuture(null);
        };
    }
}
