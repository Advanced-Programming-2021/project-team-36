package YuGiOh.model.card;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.controller.events.RoundOverExceptionEvent;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.CardAddress;
import YuGiOh.model.Player.Player;
import YuGiOh.model.card.action.Action;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.enums.*;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;

public class Monster extends Card {
    @Setter
    protected int attackDamage;
    @Setter
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
        GameController.getInstance().getPlayerControllerByPlayer(this.getOwner()).moveCardToGraveYard(this);
    }

    public void tryToSendToGraveYard(Monster monster) {
        monster.tryToSendToGraveYardOfMe();
    }

    public void tryToDecreaseLifePointOfMe(int amount) {
        GameController.getInstance().decreaseLifePoint(this.getOwner(), amount, true);
    }

    public void tryToDecreaseLifePoint(Monster monster, int amount) {
        monster.tryToDecreaseLifePointOfMe(amount);
    }

    public int getAttackDamageOnCard() {
        return attackDamage;
    }

    public int getDefenseRateOnCard() {
        return defenseRate;
    }

    public final int getAttackDamage() {
        int affects = 0;
        for (int i = 1; i <= 5; i++) {
            CardAddress cardAddress = new CardAddress(ZoneType.MAGIC, i, this.getOwner());
            Magic magic = (Magic) GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
            if (magic != null && magic.isFacedUp())
                affects += magic.affectionOnAttackingMonster(this);
        }
        PlayerController playerController = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
        Spell spell = (Spell) playerController.getPlayer().getBoard().getFieldZoneCard();
        if (spell != null)
            affects += spell.affectionOnAttackingMonster(this);
        spell = (Spell) GameController.getInstance().getOtherPlayerController(playerController).getPlayer().getBoard().getFieldZoneCard();
        if (spell != null)
            affects += spell.affectionOnAttackingMonster(this);
        return getAttackDamageOnCard() + affects;
    }

    public final int getDefenseRate() {
        int affects = 0;
        for (int i = 1; i <= 5; i++) {
            CardAddress cardAddress = new CardAddress(ZoneType.MAGIC, i, this.getOwner());
            Magic magic = (Magic) GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
            if (magic != null)
                affects += magic.affectionOnDefensiveMonster(this);
        }
        Spell spell = (Spell) GameController.getInstance().getPlayerControllerByPlayer(this.getOwner()).getPlayer().getBoard().getFieldZoneCard();
        if (spell != null)
            affects += spell.affectionOnDefensiveMonster(this);
        return getDefenseRateOnCard() + affects;
    }

    public final void damageStep(Monster attacker) throws RoundOverExceptionEvent {
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
        return () -> {
            if (getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN))
                setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
        };
    }

    public void flip() throws LogicException {
        if (getMonsterState().equals(MonsterState.DEFENSIVE_HIDDEN))
            throw new LogicException("can't flip and defensive hidden monster");
        if (getMonsterState().equals(MonsterState.DEFENSIVE_OCCUPIED))
            setMonsterState(MonsterState.OFFENSIVE_OCCUPIED);
        else if (getMonsterState().equals(MonsterState.OFFENSIVE_OCCUPIED))
            setMonsterState(MonsterState.DEFENSIVE_OCCUPIED);
    }

    protected void specialEffectWhenBeingAttacked(Monster attacker) throws ResistToChooseCard, LogicException {
        damageStep(attacker);
    }

    public Effect directAttack(Player player) {
        Player opponent = GameController.getInstance().getGame().getOtherPlayer(player);
        return () -> {
            if (GameController.getInstance().getGame().getCardZoneType(this).equals(ZoneType.GRAVEYARD)) {
                CustomPrinter.println(this.getName() + " is dead so it cannot attack", Color.Yellow);
                return;
            }
            GameController.getInstance().decreaseLifePoint(opponent, this.getAttackDamage(), true);
            this.setAllowAttack(false);
        };
    }

    protected void startOfBeingAttackedByMonster() {
    }

    protected void endOfBeingAttackedByMonster() {
    }

    public final Effect onBeingAttackedByMonster(Monster attacker) {
        return () -> {
            if (GameController.getInstance().getGame().getCardZoneType(attacker).equals(ZoneType.GRAVEYARD)) {
                CustomPrinter.println(this.getName() + " is dead so it cannot attack", Color.Yellow);
                return;
            }
            startOfBeingAttackedByMonster();
            changeFromHiddenToOccupiedIfCanEffect().run();
            specialEffectWhenBeingAttacked(attacker);
            endOfBeingAttackedByMonster();
        };
    }

    @Override
    public BooleanBinding facedUpProperty() {
        // todo if it is not in the middle of the game, we get runtime error because monsterState is null
        return Bindings.when(
                monsterStateProperty.isEqualTo(MonsterState.OFFENSIVE_OCCUPIED).or(monsterStateProperty.isEqualTo(MonsterState.DEFENSIVE_OCCUPIED)))
                .then(true).otherwise(false);
    }

    @Override
    public Effect activateEffect() throws LogicException {
        return () -> {
        };
    }

    @Override
    public boolean hasEffect() {
        return true;
    }

    public BooleanBinding isDefensive() {
        return monsterStateProperty.isEqualTo(MonsterState.DEFENSIVE_HIDDEN).or(monsterStateProperty.isEqualTo(MonsterState.DEFENSIVE_OCCUPIED));
    }

    @Override
    public boolean canActivateEffect() {
        return false;
    }

    @Override
    public final void startOfNewTurn() {
        setAllowAttack(true);
    }

    public void validateSpecialSummon() throws LogicException, ResistToChooseCard {
        throw new LogicException("You can't special summon " + this.getName());
    }

    public Action specialSummonAction() {
        return null;
    }

    public MonsterState getMonsterState(){
        return monsterStateProperty.get();
    }
    public void setMonsterState(MonsterState state){
        monsterStateProperty.set(state);
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    // todo not clean :))
    public String asEffect() {
        return String.format("%s (Monster - %s) : %s", getName(), getMonsterCardType(), getDescription());
    }

    @Override
    public Card outOfBattle() {
        super.outOfBattle();
        setMonsterState(null);
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (Monster - %s, Level <%d>, Attribute <%s>, Monster Type <%s>, Attack <%d>, Defense <%s>) %s", getName(), getMonsterCardType(), getLevel(), getAttribute(), getMonsterType(), getAttackDamageOnCard(), getDefenseRateOnCard(), getDescription());
    }
}
