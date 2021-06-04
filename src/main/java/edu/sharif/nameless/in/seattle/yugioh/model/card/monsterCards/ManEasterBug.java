package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.Color;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector.Conditions;
import edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

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
