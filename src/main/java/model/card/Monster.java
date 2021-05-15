package model.card;

import controller.GameController;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.MonsterState;
import utils.CustomPrinter;
import utils.ClassFinder;

import java.util.TreeMap;

public class Monster extends Card {
    // this should be abstract too. todo // no it should not.
    protected int attackDamage;
    protected int defenseRate;
    protected MonsterAttribute attribute;
    protected MonsterType monsterType;
    protected MonsterCardType monsterCardType;
    protected MonsterState monsterState = null;
    protected int level;
    private static TreeMap<String, TreeMap<String, String>> monstersData = new TreeMap<>();
    private static Class[] specialMonstersClasses = ClassFinder.getClasses("model.card.monsterCards");

    protected Monster(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price);
        this.attackDamage = attackDamage;
        this.defenseRate = defenseRate;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.monsterCardType = monsterCardType;
        this.monsterState = null;
        this.level = level;
    }

    public static void addMonsterData(TreeMap<String, String> monsterData) {
        Card.addCard("Monster", monsterData.get("Name"));
        monstersData.put(monsterData.get("Name"), monsterData);
    }

    public static Monster getMonster(String name) {
        System.out.println(name);
        if (specialMonstersClasses != null)
            for (Class specialMonsterClass : specialMonstersClasses) {
                if (specialMonsterClass.getName().replaceAll(".*\\.", "").equals(Utils.formatClassName(name)))
                    try {
                        return (Monster) specialMonsterClass.getConstructor().newInstance();
                    } catch (Exception exception) {
                        return null;
                    }
            }
        TreeMap<String, String> monsterData = monstersData.get(name);
        if (!monsterData.get("Card Type").equals("Normal"))
            return null;
        return new Monster(monsterData.get("Name"),
                monsterData.get("Description"),
                Integer.parseInt(monsterData.get("Price")),
                Integer.parseInt(monsterData.get("Atk")),
                Integer.parseInt(monsterData.get("Def")),
                MonsterAttribute.valueOf(monsterData.get("Attribute")),
                MonsterType.valueOf(monsterData.get("Monster Type").toUpperCase().replaceAll("-", "")),
                MonsterCardType.valueOf(monsterData.get("Card Type").toUpperCase()),
                Integer.parseInt(monsterData.get("Level")));
    }

    @Override
    public Monster clone() {
        Monster cloned = (Monster) super.clone();
        cloned.attackDamage = attackDamage;
        cloned.defenseRate = defenseRate;
        cloned.attribute = attribute;
        cloned.monsterType = monsterType;
        cloned.monsterCardType = monsterCardType;
        cloned.monsterState = monsterState;
        cloned.level = level;
        return cloned;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getDefenseRate() {
        return defenseRate;
    }

    public void setMonsterState(MonsterState monsterState) {
        this.monsterState = monsterState;
    }

    public int getLevel() {
        return level;
    }

    public MonsterState getState() {
        return monsterState;
    }

    public boolean canSummonNormally() {
        // todo why is it always true?
        return true;
    }

    public Effect onBeingAttackedByMonster(Monster attacker) {
        return () -> {
            // todo are the responses ok? maybe we have to swap your and mine?
            // todo remove this System.outs!
            if (monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED)) {
                if (attacker.getAttackDamage() > this.getAttackDamage()) {
                    int difference = attacker.getAttackDamage() - this.getAttackDamage();
                    CustomPrinter.println(String.format("your opponent’s monster is destroyed and your opponent receives %d battle damage", difference));
                    GameController.getInstance().moveCardToGraveYard(this);
                    GameController.getInstance().decreaseLifePoint(this.owner, difference);
                } else if (attacker.getAttackDamage() == this.getAttackDamage()) {
                    CustomPrinter.println("both you and your opponent monster cards are destroyed and no one receives damage");
                    GameController.getInstance().moveCardToGraveYard(attacker);
                    GameController.getInstance().moveCardToGraveYard(this);
                } else {
                    int difference = this.getAttackDamage() - attacker.getAttackDamage();
                    CustomPrinter.println(String.format("Your monster card is destroyed and you received %s battle damage", difference));
                    GameController.getInstance().moveCardToGraveYard(attacker);
                    GameController.getInstance().decreaseLifePoint(attacker.owner, difference);
                }
            } else if (monsterState.equals(MonsterState.DEFENSIVE_OCCUPIED)) {
                // todo complete this
            } else if (monsterState.equals(MonsterState.DEFENSIVE_HIDDEN)) {
            }
        };
    }
}
