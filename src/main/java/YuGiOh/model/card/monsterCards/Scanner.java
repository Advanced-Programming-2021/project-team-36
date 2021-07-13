package YuGiOh.model.card.monsterCards;

import YuGiOh.controller.GameController;
import YuGiOh.model.exception.LogicException;
import YuGiOh.controller.player.PlayerController;
import YuGiOh.model.enums.MonsterAttribute;
import YuGiOh.model.enums.MonsterCardType;
import YuGiOh.model.enums.MonsterType;
import YuGiOh.model.enums.Color;
import YuGiOh.utils.CustomPrinter;
import YuGiOh.view.cardSelector.SelectConditions;
import YuGiOh.model.exception.ResistToChooseCard;
import YuGiOh.model.card.action.Effect;
import YuGiOh.model.card.Monster;

public class Scanner extends Monster {
    public Scanner(String name, String description, int price, int attackDamage, int defenseRate, MonsterAttribute attribute, MonsterType monsterType, MonsterCardType monsterCardType, int level) {
        super(name, description, price, attackDamage, defenseRate, attribute, monsterType, monsterCardType, level);
    }

    int lastTurnActivated = -1;
    Monster copiedMonster;

    @Override
    public boolean canActivateEffect(){
        return lastTurnActivated != GameController.instance.getGame().getTurn();
    }

    @Override
    public Effect activateEffect() {
        return ()-> {
            PlayerController controller = GameController.getInstance().getPlayerControllerByPlayer(this.getOwner());
            return controller.chooseKCards(
                    "choose a monster to copy",
                    1,
                    SelectConditions.OpponentMonsterFromGraveYard
            ).thenAccept(cards-> {
                Monster copiedMonster = (Monster) cards.get(0).clone().readyForBattle(this.getOwner());
                lastTurnActivated = GameController.instance.getGame().getTurn();
                CustomPrinter.println(String.format("<%s>'s <%s> activated successfully", this.getOwner().getUser().getUsername(), this.getName()), Color.Yellow);
                CustomPrinter.println(this.asEffect(), Color.Gray);
            });
        };
    }

    @Override
    public int getAttackDamageOnCard(){
        if(getOwner() != null && copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getAttackDamageOnCard();
        return attackDamage;
    }

    @Override
    public int getDefenseRateOnCard(){
        if(getOwner() != null && copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getDefenseRateOnCard();
        return defenseRate;
    }

    @Override
    public MonsterAttribute getAttribute(){
        if(getOwner() != null && copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getAttribute();
        return attribute;
    }

    @Override
    public String getName(){
        if(getOwner() != null && copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
            return copiedMonster.getName();
        return name;
    }

    @Override
    public int getLevel(){
        if(getOwner() != null && copiedMonster != null && lastTurnActivated == GameController.getInstance().getGame().getTurn())
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
