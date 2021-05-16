package model.card;

import controller.GameController;
import controller.LogicException;
import controller.events.GameOverEvent;
import jdk.jshell.execution.Util;
import lombok.Getter;
import lombok.Setter;
import model.enums.MonsterAttribute;
import model.enums.MonsterCardType;
import model.enums.MonsterType;
import model.enums.MonsterState;
import utils.CustomPrinter;
import utils.ClassFinder;

import java.util.TreeMap;

public class Monster extends Card {
    @Setter
    @Getter
    protected int attackDamage;
    @Getter
    protected int defenseRate;
    @Getter
    protected MonsterAttribute attribute;
    @Getter
    protected MonsterType monsterType;
    @Getter
    protected MonsterCardType monsterCardType;
    @Getter
    @Setter
    protected MonsterState monsterState = null;
    @Getter
    protected int level;
    @Getter
    @Setter
    protected boolean allowAttack = true;
    // todo allowAttack should be a function that gets monster and tells whether you can attack it or not

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
        monsterData.put("Name", Utils.formatClassName(monsterData.get("Name")));
        monsterData.put("Attribute", monsterData.get("Attribute").toUpperCase());
        monsterData.put("Monster Type", monsterData.get("Monster Type").toUpperCase().replaceAll("-|\\s", ""));
        monsterData.put("Card Type", monsterData.get("Card Type").toUpperCase());
        monstersData.put(monsterData.get("Name"), monsterData);
        Utils.addCard("Monster", monsterData.get("Name"));
    }

    public static Monster getMonster(String name) {
        // todo remove this? @Kasra. Why? @Shayan?
        name = Utils.formatClassName(name);
        TreeMap<String, String> monsterData = monstersData.get(name);
        if (specialMonstersClasses != null)
            for (Class specialMonsterClass : specialMonstersClasses) {
                if (specialMonsterClass.getName().replaceAll(".*\\.", "").equals(name))
                    try {
                        return (Monster) specialMonsterClass.getConstructors()[0].newInstance(
                                monsterData.get("Name"),
                                monsterData.get("Description"),
                                Integer.parseInt(monsterData.get("Price")),
                                Integer.parseInt(monsterData.get("Atk")),
                                Integer.parseInt(monsterData.get("Def")),
                                MonsterAttribute.valueOf(monsterData.get("Attribute")),
                                MonsterType.valueOf(monsterData.get("Monster Type")),
                                MonsterCardType.valueOf(monsterData.get("Card Type")),
                                Integer.parseInt(monsterData.get("Level")));
                    } catch (Exception exception) {
                        return null;
                    }
            }
        /*if (!monsterData.get("Card Type").equals("Normal"))
            return null;*/ // TODO : Temporarily commented this out but it should be present in production.
        return new Monster(monsterData.get("Name"),
                monsterData.get("Description"),
                Integer.parseInt(monsterData.get("Price")),
                Integer.parseInt(monsterData.get("Atk")),
                Integer.parseInt(monsterData.get("Def")),
                MonsterAttribute.valueOf(monsterData.get("Attribute")),
                MonsterType.valueOf(monsterData.get("Monster Type")),
                MonsterCardType.valueOf(monsterData.get("Card Type")),
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

    public boolean canSummonNormally() {
        // todo is this ok now?
        // what about special summon?
        return !getMonsterCardType().equals(MonsterCardType.RITUAL);
    }

    public boolean isFacedUp() {
        // todo if it is not in the middle of the game, we get runtime error because monsterState is null
        return monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED) || monsterState.equals(MonsterState.DEFENSIVE_OCCUPIED);
    }

    public void tryToSendToGraveYardOfMe() {
        GameController.getInstance().moveCardToGraveYard(this);
    }

    public void tryToSendToGraveYard(Monster monster) {
        monster.tryToSendToGraveYardOfMe();
    }

    public void tryToDecreaseLifePointOfMe(int amount) {
        GameController.getInstance().decreaseLifePoint(this.owner, amount);
    }

    public void tryToDecreaseLifePoint(Monster monster, int amount) {
        monster.tryToDecreaseLifePointOfMe(amount);
    }

    public void damageStep(Monster attacker) throws GameOverEvent {
        // todo are the responses ok? maybe we have to swap your and mine?
        // todo remove this System.outs!
        if (monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED)) {
            if (attacker.getAttackDamage() > this.getAttackDamage()) {
                int difference = attacker.getAttackDamage() - this.getAttackDamage();
                CustomPrinter.println(String.format("Your monster card is destroyed and you received %s battle damage", difference));
                tryToSendToGraveYard(this);
                tryToDecreaseLifePoint(this, difference);
            } else if (attacker.getAttackDamage() == this.getAttackDamage()) {
                CustomPrinter.println("both you and your opponent monster cards are destroyed and no one receives damage");
                tryToSendToGraveYard(this);
                tryToSendToGraveYard(attacker);
            } else {
                int difference = this.getAttackDamage() - attacker.getAttackDamage();
                CustomPrinter.println(String.format("your opponent’s monster is destroyed and your opponent receives %d battle damage", difference));
                tryToSendToGraveYard(attacker);
                tryToDecreaseLifePoint(attacker, difference);
            }
        } else if (monsterState.equals(MonsterState.DEFENSIVE_OCCUPIED)) {
            // todo complete this
        } else if (monsterState.equals(MonsterState.DEFENSIVE_HIDDEN)) {
        }

        GameController.getInstance().checkBothLivesEndGame();
        attacker.setAllowAttack(false);
    }

    public Effect changeFromHiddenToOccupiedIfCanEffect() {
        return () -> {
            if (this.monsterState.equals(MonsterState.DEFENSIVE_HIDDEN))
                this.monsterState = MonsterState.DEFENSIVE_OCCUPIED;
        };
    }

    public void flip() throws LogicException {
        // todo is it correct?
        if (monsterState.equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new LogicException("can't flip and defensive hidden monster");
        if (monsterState.equals(MonsterState.DEFENSIVE_OCCUPIED))
            monsterState = MonsterState.OFFENSIVE_OCCUPIED;
        else if (monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED))
            monsterState = MonsterState.DEFENSIVE_OCCUPIED;

    }

    public Effect activateEffect() throws LogicException {
        return () -> {
        };
    }

    public Effect onBeingAttackedByMonster(Monster attacker) {
        return () -> {
            changeFromHiddenToOccupiedIfCanEffect().run();
            damageStep(attacker);
        };
    }
}
