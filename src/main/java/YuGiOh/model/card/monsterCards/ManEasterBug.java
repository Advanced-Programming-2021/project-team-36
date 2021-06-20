package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.enums.Color;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
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
    public Effect activateEffect(){
        return ()->{
            try {
                // todo this probably shouldn't be current player. probably player.chooseKCards is better
                Monster monster = (Monster) GameController.getInstance().getPlayerControllerByPlayer(this.owner).chooseKCards(
                        "choose a monster card to flip",
                        1,
                        Conditions.flippableInMonsterZone
                )[0];
                try {
                    monster.flip();
                } catch (LogicException e){
                    throw new Error("Error. You must not have tried to flip this monster!");
                }
            } catch (ResistToChooseCard e){
                CustomPrinter.println("canceled", Color.Default);
            }
        };
    }
}
