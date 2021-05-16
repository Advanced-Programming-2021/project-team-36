package model.card.monsterCards;

import controller.GameController;
import controller.LogicException;
import controller.cardSelector.Conditions;
import controller.cardSelector.ResistToChooseCard;
import model.card.Effect;
import model.card.Monster;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import utils.CustomPrinter;

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
                CustomPrinter.println("canceled");
            }
        };
    }
}
