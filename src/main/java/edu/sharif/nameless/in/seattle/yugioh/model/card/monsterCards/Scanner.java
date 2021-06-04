package edu.sharif.nameless.in.seattle.yugioh.model.card.monsterCards;

import edu.sharif.nameless.in.seattle.yugioh.controller.GameController;
import edu.sharif.nameless.in.seattle.yugioh.controller.LogicException;
import edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector.Conditions;
import edu.sharif.nameless.in.seattle.yugioh.controller.cardSelector.ResistToChooseCard;
import edu.sharif.nameless.in.seattle.yugioh.model.card.action.Effect;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterCardType;
import edu.sharif.nameless.in.seattle.yugioh.model.card.Monster;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterAttribute;
import edu.sharif.nameless.in.seattle.yugioh.model.enums.MonsterType;

public class Scanner extends Monster {
    public Scanner(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    // todo will the card also get special abilities of its copy?
    // also do we have to delete the other card from graveyard?
    // todo what if copied monster dies?

    int lastTurnActivated = -1;
    Monster copiedMonster;

    @Override
    public Effect activateEffect() throws LogicException {
        if(lastTurnActivated == GameController.instance.getGame().getTurn())
            throw new LogicException("you can only activate this once in a turn");
        lastTurnActivated = GameController.instance.getGame().getTurn();
        return ()-> {
            try {
                copiedMonster = (Monster) GameController.getInstance().getCurrentPlayerController().chooseKCards(
                        "choose a monster to copy",
                        1,
                        Conditions.OpponentMonsterFromGraveYard
                )[0];
                copiedMonster = (Monster) copiedMonster.clone().readyForBattle(this.owner);
            } catch (ResistToChooseCard ignored) {
            }
        };
    }

    @Override
    public int getAttackDamage(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getAttackDamage();
        return attackDamage;
    }

    @Override
    public int getDefenseRate(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getDefenseRate();
        return defenseRate;
    }

    @Override
    public MonsterAttribute getAttribute(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getAttribute();
        return attribute;
    }

    @Override
    public String getName(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getName();
        return name;
    }

    @Override
    public int getLevel(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getLevel();
        return level;
    }

    @Override
    public Scanner clone(){
        Scanner cloned = (Scanner) super.clone();
        cloned.lastTurnActivated = -1;
        cloned.copiedMonster = null;
        return cloned;
    }
}
