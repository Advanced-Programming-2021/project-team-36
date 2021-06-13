package edu.sharif.nameless.in.seattle.yugioh.model.card;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.events.RoundOverExceptionEvent;
import edu.sharif.nameless.in.seattle.yugioh.model.Player.Player;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.*;
import edu.sharif.nameless.in.seattle.yugioh.utils.CustomPrinter;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;

public class Monster extends Card {
    @Setter
    @Getter
    protected int attackDamage;
    @Setter
    @Getter
    protected int defenseRate;
    @Getter
    protected MonsterAttribute attribute;
    @Getter
    protected MonsterType monsterType;
    @Getter
    protected MonsterCardType monsterCardType;
    protected SimpleObjectProperty<MonsterState> monsterStateProperty;
    @Getter
    protected int level;
    @Getter
    @Setter
    protected boolean allowAttack = true;
    // todo allowAttack should be a function that gets monster and tells whether you can attack it or not

    public Monster(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price);
        this.attackDamage = attackDamage;
        this.defenseRate = defenseRate;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.monsterCardType = monsterCardType;
        this.monsterStateProperty = new SimpleObjectProperty<>(null);
        this.level = level;
    }

    @Override
    public Monster clone() {
        Monster cloned = (Monster) super.clone();
        cloned.attackDamage = attackDamage;
        cloned.defenseRate = defenseRate;
        cloned.attribute = attribute;
        cloned.monsterType = monsterType;
        cloned.monsterCardType = monsterCardType;
        cloned.monsterStateProperty = new SimpleObjectProperty<>(null);
        cloned.level = level;
        return cloned;
    }

    public int getNumberOfRequiredTribute() {
        if (level < 5)
            return 0;
        if (level < 7)
            return 1;
        return 2;
    }

    public void tryToSendToGraveYardOfMe() {
        GameController.getInstance().getPlayerControllerByPlayer(this.owner).moveCardToGraveYard(this);
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

    public void damageStep(Monster attacker) throws RoundOverExceptionEvent {
        // todo are the responses ok? maybe we have to swap your and mine?
        // todo remove this System.outs!
        if (getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED)) {
            if (attacker.getAttackDamage() > this.getAttackDamage()) {
                int difference = attacker.getAttackDamage() - this.getAttackDamage();
                CustomPrinter.println(String.format("your opponent’s monster is destroyed and your opponent received %d battle damage", difference), Color.Yellow);
                tryToSendToGraveYard(this);
                tryToDecreaseLifePoint(this, difference);
            } else if (attacker.getAttackDamage() == this.getAttackDamage()) {
                CustomPrinter.println("both you and your opponent monster cards are destroyed and no one receives damage", Color.Yellow);
                tryToSendToGraveYard(this);
                tryToSendToGraveYard(attacker);
            } else {
                int difference = this.getAttackDamage() - attacker.getAttackDamage();
                CustomPrinter.println(String.format("your monster is destroyed and you receive %d battle damage", difference), Color.Yellow);
                tryToSendToGraveYard(attacker);
                tryToDecreaseLifePoint(attacker, difference);
            }
        } else if (getMonsterState().equals(MonsterState.DEFENSIVE_OCCUPIED)) {
            if (attacker.getAttackDamage() > this.getDefenseRate()) {
                CustomPrinter.println("the defense position monster is destroyed", Color.Yellow);
                tryToSendToGraveYard(this);
            } else if (attacker.getAttackDamage() == this.getDefenseRate()) {
                CustomPrinter.println("no card is destroyed", Color.Yellow);
            } else {
                int difference = this.getDefenseRate() - attacker.getAttackDamage();
                CustomPrinter.println(String.format("no card is destroyed and you receive %d battle damage", difference), Color.Yellow);
                tryToDecreaseLifePoint(attacker, difference);
            }
        }
        GameController.getInstance().checkBothLivesEndGame();
        attacker.setAllowAttack(false);
    }

    public Effect changeFromHiddenToOccupiedIfCanEffect() {
        // todo remove this
        return () -> {
            if (getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN))
                setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
        };
    }

    public void flip() throws LogicException {
        // todo is it correct?
        if (getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new LogicException("can't flip and defensive hidden monster");
        if (getMonsterState().equals(MonsterState.DEFENSIVE_OCCUPIED))
            setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
        else if (getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
            setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
    }

    public Effect activateEffect() throws LogicException {
        return () -> {
        };
    }

    public Effect directAttack(Player player) {
        Player opponent = GameController.getInstance().getGame().getOtherPlayer(player);
        return () -> {
            GameController.getInstance().decreaseLifePoint(opponent, this.getAttackDamage());
            this.setAllowAttack(false);
        };
    }

    // todo make sure that all monsters have taken care of changing from hidden to occupied if can
    public Effect onBeingAttackedByMonster(Monster attacker) {
        return () -> {
            changeFromHiddenToOccupiedIfCanEffect().run();
            damageStep(attacker);
        };
    }

    @Override
    public BooleanBinding facedUpProperty() {
        // todo if it is not in the middle of the game, we get runtime error because monsterState is null
        return Bindings.when(
                monsterStateProperty.isEqualTo(MonsterState.OFFENSIVE_OCCUPIED).or(monsterStateProperty.isEqualTo(MonsterState.DEFENSIVE_OCCUPIED)))
                .then(true).otherwise(false);
    }

    public BooleanBinding isDefensive() {
        return monsterStateProperty.isEqualTo(MonsterState.DEFENSIVE_HIDDEN).or(monsterStateProperty.isEqualTo(MonsterState.DEFENSIVE_OCCUPIED));
    }

    public MonsterState getMonsterState(){
        return monsterStateProperty.get();
    }
    public void setMonsterState(MonsterState state){
        monsterStateProperty.set(state);
    }

    @Override
    public int getSpeed(){
        return 1;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name + '\'' +
                ", \ndescription='" + description + '\'' +
                ", \nattackDamage=" + attackDamage +
                ", \ndefenseRate=" + defenseRate +
                ", \nattribute=" + attribute +
                ", \nmonsterType=" + monsterType +
                ", \nmonsterCardType=" + monsterCardType +
                ", \nlevel=" + level +
                "\n}";
    }
}
