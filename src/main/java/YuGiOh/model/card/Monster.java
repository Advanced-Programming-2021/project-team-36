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
    @Getter
    @Setter
    protected MonsterState monsterState;
    @Getter
    protected int level;
    @Getter
    @Setter
    protected boolean allowAttack = true;

    public Monster(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price);
        this.attackDamage = attackDamage;
        this.defenseRate = defenseRate;
        this.attribute = attribute;
        this.monsterType = monsterType;
        this.monsterCardType = monsterCardType;
        this.monsterState = null;
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
        cloned.monsterState = null;
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

    public int getAttackDamageOnCard() {
        return attackDamage;
    }

    public int getDefenseRateOnCard() {
        return defenseRate;
    }

    public final int getAttackDamage() {
        int affects = 0;
        for (int i = 1; i <= 5; i++) {
            CardAddress cardAddress = new CardAddress(ZoneType.MAGIC, i, this.owner);
            Magic magic = (Magic) GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
            if (magic != null)
                affects += magic.affectionOnAttackingMonster(this);
        }
        PlayerController playerController = GameController.getInstance().getCurrentPlayerController();
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
            CardAddress cardAddress = new CardAddress(ZoneType.MAGIC, i, this.owner);
            Magic magic = (Magic) GameController.getInstance().getGame().getCardByCardAddress(cardAddress);
            if (magic != null)
                affects += magic.affectionOnDefensiveMonster(this);
        }
        Spell spell = (Spell) GameController.getInstance().getPlayerControllerByPlayer(this.owner).getPlayer().getBoard().getFieldZoneCard();
        if (spell != null)
            affects += spell.affectionOnDefensiveMonster(this);
        return getDefenseRateOnCard() + affects;
    }

    public final void damageStep(Monster attacker) throws RoundOverExceptionEvent {
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
            GameController.getInstance().decreaseLifePoint(opponent, this.getAttackDamage());
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
    public boolean isFacedUp() {
        return monsterState.equals(MonsterState.OFFENSIVE_OCCUPIED) || monsterState.equals(MonsterState.DEFENSIVE_OCCUPIED);
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

    @Override
    public int getSpeed() {
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
