package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.model.card.action.Effect;
import YuGiOh.view.cardSelector.Conditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.card.Monster;

public class ManEasterBug extends Monster {
    public ManEasterBug(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    @Override
    public Effect changeFromHiddenToOccupiedIfCanEffect(){
        return () -> {
            if (getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN)) {
                try {
                    setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
                    PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(owner);
                    if (controller.askRespondToQuestion("Do you want to activate effect of man easter bug?", "yes", "no")) {
                        Monster monster = (Monster) controller.chooseKCards(
                                "choose a monster card to kill",
                                1,
                                Conditions.getInZoneCondition(ZoneType.MONSTER)
                        )[0];
                        if(!owner.getBoard().getAllCards().contains(monster))
                            GameController.getInstance().getOtherPlayerController(controller).moveCardToGraveYard(monster);
                        else
                            controller.moveCardToGraveYard(monster);
                    }
                } catch (ResistToChooseCard e){
                    CustomPrinter.println("canceled", Color.Default);
                }
            }
        };
    }
}
