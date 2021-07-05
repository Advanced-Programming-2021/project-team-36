package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.card.action.Effect;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.card.Monster;

public class ManEaterBug extends Monster {
    public ManEaterBug(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }


    @Override
    public void preprocessForEffect() {

    }

    @Override
    public Effect changeFromHiddenToOccupiedIfCanEffect(){
        return () -> {
            if (getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN)) {
                try {
                    setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
                    GameController gameController = GameController.getInstance();
                    PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(getOwner());
                    if (controller.askRespondToQuestion("Do you want to activate effect of man easter bug?", "yes", "no")) {
                        Monster monster = (Monster) controller.chooseKCards(
                                "choose a monster card to kill",
                                1,
                                SelectConditions.getInZoneCondition(ZoneType.MONSTER)
                        )[0];
                        gameController.moveCardToGraveYard(monster);
                        CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                        CustomPrinter.println(this.asEffect(), Color.Gray);
                    }
                } catch (ResistToChooseCard e){
                    CustomPrinter.println("canceled", Color.Default);
                }
            }
        };
    }
}
