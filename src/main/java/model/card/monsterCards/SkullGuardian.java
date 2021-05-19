package model.card.monsterCards;

import controller.GameController;
import controller.LogicException;
import controller.cardSelector.Conditions;
import controller.cardSelector.ResistToChooseCard;
import model.card.Monster;
import model.card.action.Effect;
import model.enums.Color;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import utils.CustomPrinter;

public class SkullGuardian extends Monster {
    public SkullGuardian(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }
}
