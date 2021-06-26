package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.controller.LogicException;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.view.cardSelector.Conditions;
import YuGiOh.view.cardSelector.ResistToChooseCard;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Monster;

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
    public boolean canActivateEffect(){
        return lastTurnActivated != GameController.instance.getGame().getTurn();
    }

    @Override
    public Effect activateEffect() throws LogicException {
        if(lastTurnActivated == GameController.instance.getGame().getTurn())
            throw new LogicException("you can only activate this once in a turn");
        return ()-> {
            try {
                copiedMonster = (Monster) GameController.getInstance().getCurrentPlayerController().chooseKCards(
                        "choose a monster to copy",
                        1,
                        Conditions.OpponentMonsterFromGraveYard
                )[0];
                copiedMonster = (Monster) copiedMonster.clone().readyForBattle(this.owner);
                lastTurnActivated = GameController.instance.getGame().getTurn();
            } catch (ResistToChooseCard ignored) {
            }
        };
    }

    @Override
    public int getAttackDamageOnCard(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getAttackDamageOnCard();
        return attackDamage;
    }

    @Override
    public int getDefenseRateOnCard(){
        if(copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getDefenseRateOnCard();
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
